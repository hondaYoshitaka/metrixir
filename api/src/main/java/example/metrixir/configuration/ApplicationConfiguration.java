package example.metrixir.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import enkan.Application;
import enkan.application.WebApplication;
import enkan.endpoint.ResourceEndpoint;
import enkan.middleware.*;
import enkan.middleware.devel.HttpStatusCatMiddleware;
import enkan.middleware.devel.StacktraceMiddleware;
import enkan.middleware.devel.TraceWebMiddleware;
import enkan.middleware.doma2.DomaTransactionMiddleware;
import enkan.system.inject.ComponentInjector;
import example.metrixir.controller.metrics.MetricsController;
import example.metrixir.controller.user.VisitorController;
import kotowari.middleware.*;
import kotowari.routing.Routes;

import java.util.Collections;
import java.util.Set;

import static enkan.util.BeanBuilder.builder;
import static enkan.util.Predicates.NONE;
import static enkan.util.Predicates.envIn;

public class ApplicationConfiguration implements enkan.config.ApplicationFactory {

    private static final Set<String> CORS_ORIGINS = Collections.singleton("http://localhost:63342");

    @Override
    public Application create(ComponentInjector injector) {
        final WebApplication app = new WebApplication();

        final Routes routes = Routes.define(r -> {
            r.scope("", view -> {
                view.get("/metrics").to(MetricsController.class, "index");
            });
            r.scope("/api", api -> {
                api.get("/metrics").to(MetricsController.class, "fetchMetrics");
                api.post("/metrics").to(MetricsController.class, "create");
                api.get("/visitors").to(VisitorController.class, "index");
            });
        }).compile();

        app.use(new DefaultCharsetMiddleware());
        app.use(NONE, new ServiceUnavailableMiddleware<>(new ResourceEndpoint("/public/html/503.html")));

        app.use(envIn("development"), new StacktraceMiddleware());
        app.use(envIn("development"), new TraceWebMiddleware());

        app.use(new TraceMiddleware<>());
        app.use(new ContentTypeMiddleware());
        app.use(envIn("development"), new HttpStatusCatMiddleware());
        app.use(new ParamsMiddleware());
        app.use(new MultipartParamsMiddleware());
        app.use(new MethodOverrideMiddleware());
        app.use(new NormalizationMiddleware());
        app.use(new NestedParamsMiddleware());
        app.use(new CookiesMiddleware());
        app.use(new SessionMiddleware());
        app.use(new FlashMiddleware());
        app.use(new ContentNegotiationMiddleware());

        final CorsMiddleware corsMiddleware = new CorsMiddleware();
        corsMiddleware.setOrigins(CORS_ORIGINS);
        app.use(corsMiddleware);

        app.use(new ResourceMiddleware());
        app.use(new RenderTemplateMiddleware());
        app.use(new RoutingMiddleware(routes));

        app.use(new DomaTransactionMiddleware<>());
        app.use(new FormMiddleware());

        final ObjectMapper objectMapper = objectMapper();

        app.use(builder(new SerDesMiddleware())
                // TODO: service loaderで読み込んだ`JacksonJsonProvider`に負ける...
//                .set(SerDesMiddleware::setBodyReaders, new JacksonJsonProvider(objectMapper))
//                .set(SerDesMiddleware::setBodyWriters, new JacksonJsonProvider(objectMapper))
                .build());
        app.use(new ValidateFormMiddleware());
        app.use(new ControllerInvokerMiddleware(injector));

        return app;
    }

    private ObjectMapper objectMapper() {
        final JavaTimeModule module = new JavaTimeModule();

        return new ObjectMapper().registerModule(module);
    }
}

package example.metrixir.configuration;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.common.collect.ImmutableSet;
import enkan.Application;
import enkan.Env;
import enkan.application.WebApplication;
import enkan.endpoint.ResourceEndpoint;
import enkan.middleware.*;
import enkan.middleware.devel.HttpStatusCatMiddleware;
import enkan.middleware.devel.StacktraceMiddleware;
import enkan.middleware.devel.TraceWebMiddleware;
import enkan.middleware.doma2.DomaTransactionMiddleware;
import enkan.system.inject.ComponentInjector;
import example.metrixir.component.jaxrs.ext.JsonBodyReader;
import example.metrixir.component.jaxrs.ext.JsonBodyWriter;
import example.metrixir.controller.IndexController;
import example.metrixir.controller.metrics.MetricsController;
import kotowari.middleware.*;
import kotowari.middleware.serdes.ToStringBodyWriter;
import kotowari.routing.Routes;

import javax.ws.rs.ext.MessageBodyWriter;
import java.util.Set;

import static enkan.util.BeanBuilder.builder;
import static enkan.util.Predicates.NONE;
import static enkan.util.Predicates.envIn;

public class ApplicationConfiguration implements enkan.config.ApplicationFactory {

    private static final Set<String> CORS_ORIGINS = ImmutableSet.of(
            Env.getString("CORS_ORIGIN", "http://localhost:3000"));

    @Override
    public Application create(ComponentInjector injector) {
        final WebApplication app = new WebApplication();

        final Routes routes = Routes.define(r -> {
            r.scope("", view -> {
                view.get("/").to(IndexController.class, "index");
                view.get("/hosts/:hostId/metrics").to(MetricsController.class, "index");
            });
            r.scope("/api", api -> {
                api.post("/metrics").to(MetricsController.class, "create");
            });
        }).compile();

        app.use(new DefaultCharsetMiddleware());
        app.use(NONE,
                new ServiceUnavailableMiddleware<>(new ResourceEndpoint("/public/html/503.html")));

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

        final ObjectMapper mapper = createObjectMapper();
        app.use(createSerDesMiddleware(mapper));
        app.use(new ValidateBodyMiddleware<>());
        app.use(new ControllerInvokerMiddleware(injector));

        return app;
    }

    private ObjectMapper createObjectMapper() {
        return new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                .configure(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS, true)
                .configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    private SerDesMiddleware createSerDesMiddleware(final ObjectMapper mapper) {
        return builder(new SerDesMiddleware())
                .set(SerDesMiddleware::setBodyWriters, new MessageBodyWriter[]{
                        new ToStringBodyWriter(), new JsonBodyWriter(mapper)})
                .set(SerDesMiddleware::setBodyReaders, new JsonBodyReader(mapper))
                .build();
    }
}

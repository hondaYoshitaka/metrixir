package example.metrixir.component.thymeleaf;

import enkan.component.ComponentLifecycle;
import enkan.data.HttpResponse;
import enkan.exception.MisconfigurationException;
import enkan.util.HttpResponseUtils;
import kotowari.component.TemplateEngine;
import kotowari.data.TemplatedHttpResponse;
import kotowari.io.LazyRenderInputStream;
import lombok.Setter;
import nz.net.ultraq.thymeleaf.LayoutDialect;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;

/**
 * {@link LayoutDialect} を利用するための拡張Engineクラス.
 *
 * @author y_honda_
 * @see enkan.component.thymeleaf.ThymeleafTemplateEngine
 */
public class ThymeleafLayoutTemplateEngine extends TemplateEngine {
    private String prefix = "templates/";
    private String suffix = ".html";
    private ClassLoader classLoader;
    private String encoding = "UTF-8";

    @Setter
    private org.thymeleaf.TemplateEngine thymeleafEngine;

    @Override
    public HttpResponse render(final String name,
                               final Object... keyOrVals) {

        TemplatedHttpResponse response = TemplatedHttpResponse.create(name, keyOrVals);
        response.setBody(new LazyRenderInputStream(() -> {
            Context ctx = new Context(Locale.US, response.getContext());

            try {
                return new ByteArrayInputStream(
                        thymeleafEngine.process(name, ctx).getBytes(encoding));
            } catch (UnsupportedEncodingException e) {
                throw new MisconfigurationException("core.UNSUPPORTED_ENCODING", encoding, e);
            }
        }));

        HttpResponseUtils.contentType(response, "text/html");
        return response;
    }

    @Override
    public Object createFunction(Function<List, Object> func) {
        return func;
    }

    @Override
    protected ComponentLifecycle<ThymeleafLayoutTemplateEngine> lifecycle() {
        return new ComponentLifecycle<ThymeleafLayoutTemplateEngine>() {
            @Override
            public void start(final ThymeleafLayoutTemplateEngine component) {
                if (classLoader == null) {
                    classLoader = Thread.currentThread().getContextClassLoader();
                }
                ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver(classLoader);
                resolver.setTemplateMode(TemplateMode.HTML);
                resolver.setPrefix(prefix);
                resolver.setSuffix(suffix);
                resolver.setCharacterEncoding(encoding);

                // dialectを差し込む
                org.thymeleaf.TemplateEngine templateEngine = new org.thymeleaf.TemplateEngine();
                templateEngine.addDialect(new LayoutDialect());
                templateEngine.setTemplateResolver(resolver);

                component.setThymeleafEngine(templateEngine);
            }

            @Override
            public void stop(ThymeleafLayoutTemplateEngine component) {
                component.classLoader = null;
                component.thymeleafEngine = null;
            }
        };
    }
}

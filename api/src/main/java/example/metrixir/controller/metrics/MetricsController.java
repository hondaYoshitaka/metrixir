package example.metrixir.controller.metrics;

import com.fasterxml.jackson.core.JsonProcessingException;
import enkan.collection.Headers;
import enkan.collection.Multimap;
import enkan.collection.Parameters;
import enkan.component.doma2.DomaProvider;
import enkan.data.Cookie;
import enkan.data.HttpRequest;
import enkan.data.HttpResponse;
import example.metrixir.dao.metrics.MetricsDao;
import example.metrixir.dao.metrics.VisitorMetricsDao;
import example.metrixir.dao.user.VisitorDao;
import example.metrixir.model.entity.metrics.Metrics;
import example.metrixir.model.entity.metrics.VisitorMetrics;
import example.metrixir.model.entity.user.Visitor;
import example.metrixir.model.form.metrics.MetricsCreateForm;
import example.metrixir.model.response.metrics.MetricsResponseDto;
import kotowari.component.TemplateEngine;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MetricsController {
    /**
     * 訪問者cookie名
     */
    private static final String VISITOR_COOKIE_NAME = "visitor_id";
    /**
     * 訪問者cookieの生存時間 (sec)
     */
    private static final int VISITOR_COOKIE_MAX_AGE = (int) TimeUnit.MINUTES.toSeconds(30);

    @Inject
    private DomaProvider daoProvider;
    @Inject
    private TemplateEngine templateEngine;

    private MetricsDao metricsDao;

    private VisitorDao visitorDao;

    private VisitorMetricsDao visitorMetricsDao;

    @PostConstruct
    public void postConstruct() {
        metricsDao = daoProvider.getDao(MetricsDao.class);
        visitorDao = daoProvider.getDao(VisitorDao.class);
        visitorMetricsDao = daoProvider.getDao(VisitorMetricsDao.class);
    }

    public HttpResponse index() {
        final Object[] params = new Object[]{"metricsList", metricsDao.findAll()};

        return templateEngine.render("metrics/index", params);
    }

    public MetricsResponseDto fetchMetrics(final Parameters parameters) throws JsonProcessingException {
        final String visitorId = parameters.get("visitorId");
        final List<Metrics> metricsList = metricsDao.findVisitorMetrics(visitorId);

        return createMetricsResponseDto(metricsList);
    }

    @Transactional
    public HttpResponse create(final HttpRequest request, final MetricsCreateForm form) {
        System.out.println(form);

        final Cookie cookie = Optional.ofNullable(request.getCookies().get(VISITOR_COOKIE_NAME))
                .orElse(createVisitorCookie(UUID.randomUUID().toString()));
        final Visitor visitor = Optional.ofNullable(visitorDao.findOne(cookie.getValue()))
                .orElseGet(() -> insertVisitor(cookie.getValue()));

        final Metrics metrics = new Metrics();
        metrics.setHost("localhost");
        metrics.setPath("path");
        metrics.setEvent("focus");
        metrics.setName("lastName");
        metrics.setClientEventAt(LocalDateTime.now());
        //FIXME if fixed bug of CorsMiddleware, remove comment-out.
//        metrics.setHost(form.getLocation().getHost());
//        metrics.setPath(form.getLocation().getPath());
//        metrics.setEvent(form.getEvent());
//        metrics.setName(form.getName());
//        metrics.setClientEventAt(
//                LocalDateTime.ofInstant(Instant.ofEpochMilli(form.getClientTime()), ZoneId.systemDefault()));
        metrics.setCreatedAt(LocalDateTime.now());

        final int metricsCount = metricsDao.insert(metrics);
        if (metricsCount != 1) {
            throw new RuntimeException("expected count 1, but actual: " + metricsCount);
        }

        final VisitorMetrics relation = new VisitorMetrics();
        relation.setVisitorId(visitor.getId());
        relation.setMetricsId(metrics.getId());

        final int relationCount = visitorMetricsDao.insert(relation);
        if (relationCount != 1) {
            throw new RuntimeException("expected count 1, but actual: " + relationCount);
        }

        // HACK: allow cookie on CORS (should set this at middleware layer)
        final HttpResponse<String> response = HttpResponse.of("{}");
        response.setCookies(Multimap.of(VISITOR_COOKIE_NAME, cookie));
        response.setHeaders(Headers.of(
                "Access-Control-Allow-Credentials", "true"
        ));
        return response;
    }

    /**
     * @param id 訪問者ID
     * @return visitor
     */
    private Visitor insertVisitor(final String id) {
        final Visitor entity = new Visitor();
        entity.setId(id);

        final int count = visitorDao.insert(entity);
        if (count != 1) {
            throw new RuntimeException("expected count 1, but actual: " + count);
        }
        return entity;
    }

    /**
     * @param visitorId 訪問者ID
     * @return cookie
     */
    private static Cookie createVisitorCookie(final String visitorId) {
        final Cookie cookie = Cookie.create(VISITOR_COOKIE_NAME, visitorId);
        cookie.setDomain("localhost");
        cookie.setPath("/");
        cookie.setMaxAge(VISITOR_COOKIE_MAX_AGE);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);

        return cookie;
    }

    /**
     * HACK:
     */
    private MetricsResponseDto createMetricsResponseDto(final List<Metrics> metricsList) {
        final MetricsResponseDto dto = new MetricsResponseDto();

        if (metricsList.isEmpty()) {
            return dto;
        }
        final Map<String, List<Metrics>> groupBy = metricsList.stream()
                .collect(Collectors.groupingBy(Metrics::getName));

        for (final Map.Entry<String, List<Metrics>> entry : groupBy.entrySet()) {
            // focus + blur
            final List<MetricsResponseDto.MetricsDto> events = divide(entry.getValue(), 2)
                    .stream()
                    .map(pair -> new MetricsResponseDto.MetricsDto(
                            pair.get(0).getClientEventAt(),
                            pair.get(1).getClientEventAt()))
                    .collect(Collectors.toList());

            dto.getMetricsMap().put(entry.getKey(), events);
        }
        return dto;
    }

    public static <T> List<List<T>> divide(List<T> origin, int size) {
        if (origin == null || origin.isEmpty() || size <= 0) {
            return Collections.emptyList();
        }
        int block = origin.size() / size + (origin.size() % size > 0 ? 1 : 0);

        return IntStream.range(0, block)
                .boxed()
                .map(i -> {
                    int start = i * size;
                    int end = Math.min(start + size, origin.size());
                    return origin.subList(start, end);
                })
                .collect(Collectors.toList());
    }
}

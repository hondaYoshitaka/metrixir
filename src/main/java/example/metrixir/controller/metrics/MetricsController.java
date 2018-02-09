package example.metrixir.controller.metrics;

import enkan.Env;
import enkan.collection.Multimap;
import enkan.collection.Parameters;
import enkan.component.doma2.DomaProvider;
import enkan.data.Cookie;
import enkan.data.HttpRequest;
import enkan.data.HttpResponse;
import example.metrixir.dao.client.ClientHostDao;
import example.metrixir.dao.metrics.MetricsDao;
import example.metrixir.dao.metrics.VisitorMetricsDao;
import example.metrixir.dao.user.VisitorDao;
import example.metrixir.model.entity.client.ClientHost;
import example.metrixir.model.entity.metrics.Metrics;
import example.metrixir.model.entity.metrics.MetricsWithVisitor;
import example.metrixir.model.entity.metrics.VisitorMetrics;
import example.metrixir.model.entity.user.Visitor;
import example.metrixir.model.form.metrics.HostMetricsFetchForm;
import example.metrixir.model.form.metrics.MetricsCreateForm;
import example.metrixir.model.response.metrics.MetricsResponseDto;
import kotowari.component.TemplateEngine;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.groupingBy;

public class MetricsController {
    /**
     * 訪問者cookie名
     */
    private static final String VISITOR_COOKIE_NAME = "visitor_id";
    /**
     * 訪問者cookieの生存時間 (sec)
     */
    private static final int VISITOR_COOKIE_MAX_AGE = (int) TimeUnit.DAYS.toSeconds(2);

    @Inject
    private DomaProvider daoProvider;
    @Inject
    private TemplateEngine templateEngine;

    private MetricsDao metricsDao;

    private VisitorDao visitorDao;

    private VisitorMetricsDao visitorMetricsDao;

    private ClientHostDao clientHostDao;

    @PostConstruct
    public void init() {
        metricsDao = daoProvider.getDao(MetricsDao.class);
        visitorDao = daoProvider.getDao(VisitorDao.class);
        visitorMetricsDao = daoProvider.getDao(VisitorMetricsDao.class);
        clientHostDao = daoProvider.getDao(ClientHostDao.class);
    }

    /**
     * Metrics表示用のviewを表示します.
     *
     * @return view
     */
    @Deprecated
    public HttpResponse index(final Parameters parameters) {
        final String tag = parameters.get("tag");

        final List<Visitor> visitors = visitorDao.findAllByTag(tag);

        return templateEngine.render("metrics/index",
                new Object[]{"tag", tag, "visitors", visitors});
    }

    @Deprecated
    public MetricsResponseDto fetchMetrics(final Parameters parameters) {
        final String visitorId = parameters.get("visitorId");
        final String hostTag = parameters.get("tag");

        final List<Metrics> metricsList = metricsDao.findVisitorMetricsByTag(visitorId, hostTag);

        return createMetricsResponseDto(metricsList);
    }

    public HttpResponse showHostMetrics(final HostMetricsFetchForm form) {
        final List<MetricsWithVisitor> metricsList = metricsDao.findAllByPage(form.getHostId());
        final LinkedHashMap<String, List<MetricsWithVisitor>> sortedMetricsMap =
            metricsList.stream().collect(groupingBy(Metrics::getTransactionId))
                       .entrySet().stream()
                       .sorted(Entry.comparingByValue((list1, list2) -> {
                           final LocalDateTime time1 = list1.get(0).getClientEventAt();
                           final LocalDateTime time2 = list2.get(0).getClientEventAt();
                           if (time1.isEqual(time2)) {
                               return 0;
                           } else if (time1.isAfter(time2)) {
                               return -1;
                           } else {
                               return 1;
                           }
                       }))
                       .collect(Collectors.toMap(Entry::getKey,
                                                 Entry::getValue,
                                                 (oldValue, newValue) -> oldValue,
                                                 LinkedHashMap::new));
        final Object[] params = {
            "host", clientHostDao.findById(form.getHostId()),
            "metricsMap", sortedMetricsMap};

        return templateEngine.render("metrics/hostMetrics", params);
    }

    @Transactional
    public HttpResponse create(final HttpRequest request, final MetricsCreateForm form) {
        final Cookie cookie = Optional.ofNullable(request.getCookies().get(VISITOR_COOKIE_NAME))
                .orElse(createVisitorCookie(UUID.randomUUID().toString()));
        final String tag = Optional.ofNullable(form.getHostTag()).orElse("");

        final Visitor visitor = fetchVisitorBy(cookie.getValue());
        final ClientHost clientHost = fetchClientHostBy(form.getLocation().getHost(), tag);

        final Metrics metrics = new Metrics();
        metrics.setTransactionId(form.getTransactionId());
        metrics.setEvent(form.getEvent());
        metrics.setPath(form.getLocation().getPath());
        metrics.setName(form.getName());
        metrics.setClientEventAt(
                LocalDateTime.ofInstant(Instant.ofEpochMilli(form.getClientTime()), ZoneId.systemDefault()));
        metrics.setCreatedAt(LocalDateTime.now());

        final int metricsCount = metricsDao.insert(metrics);
        if (metricsCount != 1) {
            throw new RuntimeException("expected count 1, but actual: " + metricsCount);
        }

        final VisitorMetrics relation = new VisitorMetrics();
        relation.setVisitorId(visitor.getId());
        relation.setClientHostId(clientHost.getId());
        relation.setMetricsId(metrics.getId());

        final int relationCount = visitorMetricsDao.insert(relation);
        if (relationCount != 1) {
            throw new RuntimeException("expected count 1, but actual: " + relationCount);
        }

        final HttpResponse<String> response = HttpResponse.of("{}");
        response.setCookies(Multimap.of(VISITOR_COOKIE_NAME, cookie));

        return response;
    }

    @NotNull
    private ClientHost fetchClientHostBy(final String host, final String tag) {
        final ClientHost entity = clientHostDao.findByHostAndTag(host, tag);

        if (entity == null) {
            return insertClientHost(host, tag);
        }
        return entity;
    }

    @NotNull
    private ClientHost insertClientHost(final String host, final String tag) {
        final ClientHost entity = new ClientHost();
        entity.setHost(host);
        entity.setTag(tag);

        final int count = clientHostDao.insert(entity);
        if (count != 1) {
            throw new RuntimeException("expected count 1, but actual: " + count);
        }
        return entity;
    }

    @NotNull
    private Visitor fetchVisitorBy(final String id) {
        final Visitor entity = visitorDao.findOne(id);

        if (entity == null) {
            return insertVisitor(id);
        }
        return entity;
    }

    @NotNull
    private Visitor insertVisitor(final String id) {
        final Visitor entity = new Visitor();
        entity.setId(id);

        final int count = visitorDao.insert(entity);
        if (count != 1) {
            throw new RuntimeException("expected count 1, but actual: " + count);
        }
        return entity;
    }

    @NotNull
    private static Cookie createVisitorCookie(final String visitorId) {
        final Cookie cookie = Cookie.create(VISITOR_COOKIE_NAME, visitorId);
        cookie.setDomain(Env.getString("SERVER_DOMAIN", "localhost"));
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
                .collect(groupingBy(Metrics::getName));

        for (final Map.Entry<String, List<Metrics>> entry : groupBy.entrySet()) {
            // focus + blur
            final List<MetricsResponseDto.MetricsDto> events = divide(entry.getValue(), 2)
                    .stream()
                    .filter(pair -> pair.size() == 2)
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

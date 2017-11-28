SELECT
m.id,
m.event,
m.name,
m.host,
m.path,
m.client_event_at,
m.created_at

FROM metrics m

INNER JOIN visitor_metrics vm
ON vm.metrics_id = m.id

WHERE
vm.visitor_id = /*visitorId*/''

ORDER BY created_at ASC
;
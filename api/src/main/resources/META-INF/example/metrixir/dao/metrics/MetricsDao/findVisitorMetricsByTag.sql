SELECT
m.id,
m.event,
m.name,
m.path,
m.client_event_at,
m.created_at

FROM metrics m

INNER JOIN visitor_metrics vm
ON vm.metrics_id = m.id

INNER JOIN client_host ch
ON vm.client_host_id = ch.id

WHERE
vm.visitor_id = /*visitorId*/''
AND
ch.tag = /*tag*/''

ORDER BY m.id ASC
;
SELECT
DISTINCT v.id

FROM visitor v

INNER JOIN visitor_metrics vm
ON vm.visitor_id = v.id

INNER JOIN client_host ch
ON vm.client_host_id = ch.id

WHERE
ch.tag = /*tag*/''
;
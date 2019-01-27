SELECT
  COUNT(DISTINCT m.transaction_id)

FROM metrics m

INNER JOIN visitor_metrics v
    ON v.metrics_id = m.id

WHERE
  v.host_id = /*hostId*/1
;
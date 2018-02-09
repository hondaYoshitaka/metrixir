SELECT
  m.id,
  m.transaction_id,
  m.name,
  m.event,
  m.path,
  m.client_event_at,
  m.created_at,
  v.visitor_id

FROM metrics m

  INNER JOIN visitor_metrics v
    ON v.metrics_id = m.id

WHERE
  v.client_host_id = /*hostId*/1

ORDER BY m.client_event_at ASC;

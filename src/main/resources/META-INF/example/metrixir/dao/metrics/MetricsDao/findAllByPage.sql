SELECT
  m.id,
  m.transaction_id,
  m.name,
  m.event,
  m.path,
  m.client_event_at,
  m.created_at,
  temp.visitor_id

FROM metrics m

INNER JOIN (
    SELECT
      m2.transaction_id,
      v.visitor_id

     FROM metrics m2

    INNER JOIN visitor_metrics v
      ON v.metrics_id = m2.id

    WHERE
      v.client_host_id = /*hostId*/259
      AND
      m2.event = 'load'

    ORDER BY m2.client_event_at DESC

    LIMIT /*limit*/10
    OFFSET /*offset*/0

  ) temp
  ON m.transaction_id = temp.transaction_id

;
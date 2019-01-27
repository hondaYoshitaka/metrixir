SELECT
  m.id,
  m.metrics_group_id,
  m.name,
  m.event,
  m.path,
  m.client_event_at,
  m.created_at,
  mg.visitor_id

FROM  (
    SELECT
      g.id,
      g.visitor_id

    FROM metrics_groups g

    WHERE
      g.host_id = /*hostId*/1

    ORDER BY g.id DESC

    LIMIT /*limit*/20
    OFFSET /*offset*/0

  ) mg

INNER JOIN metrics m
  ON mg.id = m.metrics_group_id

ORDER BY m.client_event_at ASC
;

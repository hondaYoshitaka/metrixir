CREATE TABLE metrics (
  id                BIGSERIAL    PRIMARY KEY,
  metrics_group_id  BIGSERIAL    NOT NULL,
  event             VARCHAR(255) NOT NULL,
  name              VARCHAR(255) NOT NULL,
  path              VARCHAR(512) NOT NULL,
  client_event_at   TIMESTAMP    NOT NULL,
  created_at        TIMESTAMP    NOT NULL,

  FOREIGN KEY (metrics_group_id) REFERENCES metrics_groups (id)
);

CREATE TABLE metrics_groups (
  id          BIGSERIAL PRIMARY KEY,
  host_id     BIGSERIAL NOT NULL,
  visitor_id  BIGSERIAL NOT NULL,

  FOREIGN KEY (host_id)     REFERENCES hosts (id),
  FOREIGN KEY (visitor_id)  REFERENCES visitors (id)
);

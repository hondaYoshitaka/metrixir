CREATE TABLE visitor (
  id VARCHAR(255) PRIMARY KEY
);

CREATE TABLE visitor_metrics (
  visitor_id     VARCHAR(255) NOT NULL,
  client_host_id BIGINT       NOT NULL,
  metrics_id     BIGINT       NOT NULL,
  FOREIGN KEY (visitor_id) REFERENCES visitor (id),
  FOREIGN KEY (client_host_id) REFERENCES client_host (id),
  FOREIGN KEY (metrics_id) REFERENCES metrics (id)
);
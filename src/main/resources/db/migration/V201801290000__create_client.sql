CREATE TABLE client_host (
  id   BIGSERIAL PRIMARY KEY,
  host VARCHAR(255) NOT NULL,
  tag  VARCHAR(255) NOT NULL
);

ALTER TABLE client_host
  ADD UNIQUE (host, tag);
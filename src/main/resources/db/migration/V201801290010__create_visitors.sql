CREATE TABLE visitors (
  id       BIGSERIAL PRIMARY KEY,
  host_id  BIGSERIAL NOT NULL,

  FOREIGN KEY (host_id) REFERENCES hosts (id)
);

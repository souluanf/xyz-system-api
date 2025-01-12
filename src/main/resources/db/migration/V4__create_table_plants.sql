CREATE TABLE IF NOT EXISTS plants
(
    id               SERIAL PRIMARY KEY ,
    code             BIGINT                                  NOT NULL UNIQUE,
    description      VARCHAR(10),
    created_at       TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at       TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by       VARCHAR(50)                             NOT NULL,
    last_modified_by VARCHAR(50)
);
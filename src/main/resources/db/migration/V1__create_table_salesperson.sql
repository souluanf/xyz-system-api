CREATE TABLE IF NOT EXISTS salespeople
(
    id               SERIAL PRIMARY KEY,
    created_at       TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at       TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by       VARCHAR(50)    NOT NULL,
    last_modified_by VARCHAR(50),
    name             VARCHAR(50)    NOT NULL,
    age              INT            NOT NULL,
    salary           NUMERIC(10, 2) NOT NULL
);

INSERT INTO salespeople (id, name, age, salary,created_by)
VALUES (1, 'Abe', 61, 140000,'admin'),
       (2, 'Bob', 34, 44000,'admin'),
       (5, 'Chris', 34, 40000,'admin'),
       (7, 'Dan', 41, 52000,'admin'),
       (8, 'Ken', 57, 115000,'admin'),
       (11, 'Joe', 38, 38000,'admin');
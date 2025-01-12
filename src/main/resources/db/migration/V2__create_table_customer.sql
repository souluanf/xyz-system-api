CREATE TABLE IF NOT EXISTS customers
(
    id               SERIAL PRIMARY KEY,
    created_at       TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at       TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by       VARCHAR(50) NOT NULL,
    last_modified_by VARCHAR(50),
    name             VARCHAR(50) NOT NULL,
    city             VARCHAR(50) NOT NULL,
    industry_type    CHAR(1)     NOT NULL
);


INSERT INTO customers (id, name, city, industry_type, created_by)
VALUES (4, 'Samsonic', 'Pleasant', 'J', 'admin'),
       (6, 'Panasung', 'Oaktown', 'J', 'admin'),
       (7, 'Samony', 'Jackson', 'B', 'admin'),
       (9, 'Orange', 'Jackson', 'B', 'admin');
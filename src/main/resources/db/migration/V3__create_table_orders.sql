CREATE TABLE IF NOT EXISTS orders
(
    id               SERIAL PRIMARY KEY,
    created_at       TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at       TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by       VARCHAR(50)    NOT NULL,
    last_modified_by VARCHAR(50),
    order_date       DATE           NOT NULL,
    customer_id      INT            NOT NULL,
    salesperson_id   INT            NULL,
    amount           NUMERIC(10, 2) NOT NULL,
    CONSTRAINT fk_customer FOREIGN KEY (customer_id) REFERENCES customers (id),
    CONSTRAINT fk_salesperson FOREIGN KEY (salesperson_id) REFERENCES salespeople (id) ON DELETE SET NULL
);


INSERT INTO orders (id, order_date, customer_id, salesperson_id, amount,created_by)
VALUES (10, '1996-08-02', 4, 2, 540,'admin'),
       (20, '1999-01-30', 4, 8, 1800,'admin'),
       (30, '1995-07-14', 9, 1, 460,'admin'),
       (40, '1998-01-29', 7, 2, 2400,'admin'),
       (50, '1998-02-03', 6, 7, 600,'admin'),
       (60, '1998-03-02', 6, 7, 720,'admin'),
       (70, '1998-05-06', 9, 7, 150,'admin');
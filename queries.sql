--- a. Retornar os nomes de todos os vendedores que não têm nenhum pedido com a Samsonic
SELECT s.name
FROM salespeople s
WHERE NOT EXISTS (SELECT 1
                  FROM orders o
                           JOIN customers c ON o.customer_id = c.id
                  WHERE c.name = 'Samsonic'
                    AND o.salesperson_id = s.id);

-- b. Atualizar os nomes dos vendedores que têm 2 ou mais pedidos (adicionar ‘*’ no final do nome)
UPDATE salespeople
SET name =
        CASE
            WHEN id IN (
                SELECT salesperson_id
                FROM orders
                GROUP BY salesperson_id
                HAVING COUNT(*) >= 2
            ) THEN
                CASE
                    WHEN name LIKE '%*' THEN name
                    ELSE name || '*'
                    END
            ELSE
                CASE
                    WHEN name LIKE '%*' THEN SUBSTRING(name FROM 1 FOR LENGTH(name) - 1)
                    ELSE name
                    END
            END
WHERE id IN (
    SELECT id
    FROM salespeople
);

-- c. Excluir todos os vendedores que fizeram pedidos para a cidade de Jackson
DELETE
FROM salespeople
WHERE id IN (
    SELECT DISTINCT s.id
    FROM salespeople s
             JOIN orders o ON s.id = o.salesperson_id
             JOIN customers c ON o.customer_id = c.id
    WHERE c.city = 'Jackson'
);

-- d. Total de vendas para cada vendedor (exibir 0 se o vendedor não tiver vendido nada)
SELECT s.id,s.name, COALESCE(SUM(o.amount), 0) AS total_sales
FROM salespeople s
         LEFT JOIN orders o ON s.id = o.salesperson_id
GROUP BY s.id, s.name
ORDER BY s.name;
------
INSERT INTO PRODUCT (id, name, stock,updated_by)
SELECT 1, 'Iphone', 3, 'admin'
    WHERE NOT EXISTS (SELECT 1 FROM PRODUCT WHERE id = 1);

INSERT INTO PRODUCT (id, name, stock, updated_by)
SELECT 2, 'Macbook', 10, 'admin'
    WHERE NOT EXISTS (SELECT 1 FROM PRODUCT WHERE id = 2);
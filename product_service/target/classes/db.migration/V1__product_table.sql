-- V1__create_product_table.sql
CREATE TABLE IF NOT EXISTS product (
    product_id CHAR(36) PRIMARY KEY,
    product_name VARCHAR(255) NOT NULL,
    product_description TEXT,
    product_price decimal NOT NULL,
    product_quantity INT NOT NULL,
    url TEXT,
    purchase_price decimal NOT NULL,
    product_status varchar(50) not null,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    category_id decimal not null,
    store_id  varchar(255) not null


);



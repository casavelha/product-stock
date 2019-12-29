-- DROP TABLE IF EXISTS products;


CREATE TABLE IF NOT EXISTS products (
    id INT AUTO_INCREMENT PRIMARY KEY,
    product VARCHAR(250) NOT NULL,
    quantity INT NOT NULL,
    price DECIMAL (20,2) NOT NULL,
    type VARCHAR(250),
    industry VARCHAR(250),
    origin VARCHAR(250),
    source VARCHAR(250) NOT NULL,
    source_line INT NOT NULL
);

CREATE UNIQUE INDEX IF NOT EXISTS products_source_idx
	ON products (source, source_line);
	
CREATE INDEX IF NOT EXISTS products_product
	ON products (product);

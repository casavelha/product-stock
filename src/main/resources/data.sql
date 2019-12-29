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


--CREATE TABLE IF NOT EXISTS billionaires (
--  id INT AUTO_INCREMENT  PRIMARY KEY,
--  first_name VARCHAR(250) NOT NULL,
--  last_name VARCHAR(250) NOT NULL,
--  career VARCHAR(250) DEFAULT NULL
--);

--INSERT INTO billionaires (first_name, last_name, career) VALUES
--  ('Aliko', 'Dangote', 'Billionaire Industrialist'),
--  ('Bill', 'Gates', 'Billionaire Tech Entrepreneur'),
--  ('Folrunsho', 'Alakija', 'Billionaire Oil Magnate');
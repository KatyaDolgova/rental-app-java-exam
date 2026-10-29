CREATE TABLE properties (
    id INT AUTO_INCREMENT PRIMARY KEY,
    owner_id INT NOT NULL,
    city VARCHAR(100),
    country VARCHAR(100),
    description TEXT,
    price_per_night DECIMAL(10,2),
    FOREIGN KEY (owner_id) REFERENCES users(id)
);
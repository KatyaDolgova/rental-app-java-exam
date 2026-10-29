CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(255),
    city VARCHAR(100),
    country VARCHAR(100),
    nickname VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    birth_date DATE,
    email VARCHAR(100) UNIQUE NOT NULL,
    rating DOUBLE DEFAULT 5.0,
    is_blocked BOOLEAN DEFAULT FALSE
);
ALTER TABLE users 
ADD COLUMN rating_sum DOUBLE DEFAULT 0.0,
ADD COLUMN rating_count INT DEFAULT 0;
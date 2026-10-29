CREATE TABLE bookings (
    id INT AUTO_INCREMENT PRIMARY KEY,
    property_id INT NOT NULL,
    renter_id INT NOT NULL, 
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    completed BOOLEAN DEFAULT FALSE,
    rating_given_by_renter INT NULL CHECK (rating_given_by_renter BETWEEN 1 AND 5),
    rating_given_by_owner INT NULL CHECK (rating_given_by_owner BETWEEN 1 AND 5),
    FOREIGN KEY (property_id) REFERENCES properties(id),
    FOREIGN KEY (renter_id) REFERENCES users(id)
);

ALTER TABLE bookings ADD COLUMN status VARCHAR(20) DEFAULT 'ACTIVE';
ALTER TABLE bookings ADD COLUMN new_start_date DATE;
ALTER TABLE bookings ADD COLUMN new_end_date DATE;
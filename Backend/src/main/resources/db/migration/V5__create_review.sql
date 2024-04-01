CREATE TABLE IF NOT EXISTS review (
    id INT AUTO_INCREMENT PRIMARY KEY,
    customer_id INT NOT NULL,
    service_id INT NOT NULL,
    description VARCHAR(255),
    rating DOUBLE NOT NULL,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    isActive BOOLEAN,
    FOREIGN KEY (customer_id) REFERENCES user (id),
    FOREIGN KEY (service_id) REFERENCES service (id)
);
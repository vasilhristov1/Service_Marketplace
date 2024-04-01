CREATE TABLE IF NOT EXISTS offer (
    id INT AUTO_INCREMENT PRIMARY KEY,
    request_id INT NOT NULL,
    description VARCHAR(255) NOT NULL,
    offer_price DOUBLE NOT NULL,
    offer_status VARCHAR(50) NOT NULL,
    created_at DATETIME,
    updated_at DATETIME,
    FOREIGN KEY (request_id) REFERENCES request (id)
);
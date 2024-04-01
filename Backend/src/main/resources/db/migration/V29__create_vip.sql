CREATE TABLE vip_services (
    id INT AUTO_INCREMENT PRIMARY KEY,
    service_id INT NOT NULL,
    start_date DATETIME NOT NULL,
    end_date DATETIME NOT NULL,
    stripe_id VARCHAR(255) NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    FOREIGN KEY (service_id) REFERENCES service (id)
);

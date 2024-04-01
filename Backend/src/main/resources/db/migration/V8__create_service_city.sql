CREATE TABLE IF NOT EXISTS service_city (
    service_id INT NOT NULL,
    city_id INT NOT NULL,
    FOREIGN KEY (service_id) REFERENCES service (id),
    FOREIGN KEY (city_id) REFERENCES city (id)
);
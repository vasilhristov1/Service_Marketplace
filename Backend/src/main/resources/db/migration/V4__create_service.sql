CREATE TABLE IF NOT EXISTS service (
    id INT AUTO_INCREMENT PRIMARY KEY,
    provider_id int,
    category_id int,
    title VARCHAR(50) NOT NULL,
    description VARCHAR(255),
    service_status VARCHAR(50) NOT NULL,
    price DOUBLE NOT NULL,
    created_at DATETIME,
    updated_at DATETIME,
    FOREIGN KEY (provider_id) REFERENCES user (id),
    FOREIGN KEY (category_id) REFERENCES category (id)
);
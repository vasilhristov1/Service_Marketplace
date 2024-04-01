CREATE TABLE IF NOT EXISTS transactions (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    offer_id INT NOT NULL,
    amount DOUBLE NOT NULL,
    timestamp DATETIME NOT NULL,
    transaction_status VARCHAR(50) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES user (id),
    FOREIGN KEY (offer_id) REFERENCES offer (id)
);
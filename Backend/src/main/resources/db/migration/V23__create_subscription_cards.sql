CREATE TABLE IF NOT EXISTS subscription (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    start_date DATETIME NOT NULL,
    end_date DATETIME NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    stripe_id VARCHAR(255) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES user (id)
);

CREATE TABLE IF NOT EXISTS cards (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    card_token VARCHAR(255) NOT NULL,
    isDeleted BOOLEAN NOT NULL DEFAULT FALSE,
    FOREIGN KEY (user_id) REFERENCES user (id)
);
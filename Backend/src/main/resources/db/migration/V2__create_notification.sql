CREATE TABLE IF NOT EXISTS notification (
    id INT AUTO_INCREMENT,
    user_id INT NOT NULL,
    type VARCHAR(50) NOT NULL,
    content VARCHAR(255) NOT NULL,
    status VARCHAR(50),
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES user (id)
);
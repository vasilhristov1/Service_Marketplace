CREATE TABLE IF NOT EXISTS files (
    id INT AUTO_INCREMENT,
    review_id INT ,
    url VARCHAR(255),
    expiration_date DATETIME,
    PRIMARY KEY (id),
    FOREIGN KEY (review_id) REFERENCES review (id)
);
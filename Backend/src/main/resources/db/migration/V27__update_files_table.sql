alter table files
add column service_id INT,
ADD CONSTRAINT fk_service_id
    FOREIGN KEY (service_id)
    REFERENCES service(id);

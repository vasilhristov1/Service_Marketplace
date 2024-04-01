
alter table request
drop COLUMN is_active;

alter table request
add column request_status VARCHAR(50) ;

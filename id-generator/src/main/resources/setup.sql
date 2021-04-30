create table if not exists id_detail (
   serial_number bigserial primary key,
   id varchar(8) unique,
   status varchar(32),
   instance_id varchar(32)
);

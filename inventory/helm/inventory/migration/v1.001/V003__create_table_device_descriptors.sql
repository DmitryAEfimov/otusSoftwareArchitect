CREATE TABLE device_descriptors
(
    id                uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
    vendor            varchar(64),
    model_name        varchar(128),
    role              varchar(64),
    eth_port_cnt      integer,
    eth_port_type     varchar(128),
    optical_port_cnt  integer,
    optical_port_type varchar(128),
    slot_cnt          integer
);
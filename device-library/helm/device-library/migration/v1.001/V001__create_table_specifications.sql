CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TYPE t_device_type_enum AS ENUM ('Switch', 'Router', 'MediaConverter', 'MSAN');

CREATE TABLE specifications
(
    id                uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
    vendor            VARCHAR(64),
    model_name        VARCHAR(128),
    device_class      t_device_type_enum,
    eth_port_cnt      INTEGER,
    eth_port_type     VARCHAR(128),
    optical_port_cnt  INTEGER,
    optical_port_type VARCHAR(128),
    slot_cnt          INTEGER,
    CONSTRAINT unq_vendor_model UNIQUE (vendor, model_name)
);
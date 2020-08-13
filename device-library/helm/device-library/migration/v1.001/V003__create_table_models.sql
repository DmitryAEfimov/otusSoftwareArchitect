create table models
(
    id           uuid default uuid_generate_v4(),
    vendor       VARCHAR(256) not null,
    model_name   VARCHAR(64)  not null,
    device_class t_device_type_enum,
    constraint unq_vendor_model UNIQUE (vendor, model_name)
);
create table models
(
    id           uuid default uuid_generate_v4(),
    vendor       VARCHAR(256) not null,
    model        VARCHAR(64)  not null,
    device_class t_device_type_enum
);
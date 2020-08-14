create table regions
(
    id          uuid default uuid_generate_v4() primary key,
    region_name varchar(256) not null,
    region_type t_region_type_enum,
    constraint unq_region unique (region_name, region_type)
);
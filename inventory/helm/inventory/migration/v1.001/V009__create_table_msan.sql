create table multi_service_access_nodes
(
    id               uuid default uuid_generate_v4(),
    vendor           varchar(64)  not null,
    model            varchar(256) not null,
    dsl_slot_cnt     int  default 0,
    optical_slot_cnt int  default 0
);
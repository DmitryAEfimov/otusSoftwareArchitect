create table settlements
(
    id              uuid default uuid_generate_v4(),
    region_id       uuid         not null,
    settlement_name varchar(256) not null,
    settlement_type t_settlement_type_enum,
    constraint fk_settlement_region foreign key (region_id) references regions (id) on delete cascade,
    constraint unq_settlement unique (region_id, settlement_name, settlement_type)
);

create index idx_settlement_region on settlements (region_id);
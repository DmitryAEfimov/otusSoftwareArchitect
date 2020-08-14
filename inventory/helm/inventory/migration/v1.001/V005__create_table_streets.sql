create table streets
(
    id            uuid default uuid_generate_v4() primary key,
    region_id     uuid,
    settlement_id uuid,
    street_name   varchar(256) not null,
    street_type   t_street_type_enum,
    constraint fk_street_region foreign key (region_id) references regions (id) on delete cascade,
    constraint fk_street_settlement foreign key (settlement_id) references settlements (id) on delete cascade,
    constraint unq_street unique (region_id, settlement_id, street_name, street_type),
    constraint chk_street_parents check ( coalesce(region_id, settlement_id) is not null )
);

create index idx_street_region on streets (region_id);
create index idx_street_settlement on streets (settlement_id);
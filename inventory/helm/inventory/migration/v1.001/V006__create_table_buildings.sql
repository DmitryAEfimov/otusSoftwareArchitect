create table buildings
(
    id            uuid default uuid_generate_v4() primary key,
    settlement_id uuid,
    street_id     uuid,
    house_num     int,
    house_literal varchar(32),
    constraint fk_bld_settlement foreign key (settlement_id) references settlements (id) on delete cascade,
    constraint fk_bld_street foreign key (street_id) references streets (id) on delete cascade,
    constraint unq_building unique (settlement_id, street_id, house_num, house_literal),
    constraint chk_bld_parents check ( coalesce(settlement_id, street_id) is not null),
    constraint chk_bld_notnull check ( house_num is not null or house_literal is not null)
);

create index idx_building_settlement on buildings (settlement_id);
create index idx_building_street on buildings (street_id);
create table locations
(
    id          uuid default uuid_generate_v4() primary key,
    building_id uuid,
    longitude   decimal,
    latitude    decimal,
    description varchar(2048),
    constraint fk_location_bld foreign key (building_id) references buildings (id) on delete cascade,
    constraint unq_location unique (building_id, longitude, latitude),
    constraint chk_geo check ( coalesce(latitude, longitude) is null or
                               (latitude is not null and longitude is not null)),
    constraint chk_location_parents check ( building_id is not null or latitude is not null)
);

create index idx_location_building on locations (building_id);
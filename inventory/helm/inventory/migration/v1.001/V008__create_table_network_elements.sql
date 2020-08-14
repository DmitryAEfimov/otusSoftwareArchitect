create table network_elements
(
    id         uuid default uuid_generate_v4() primary key,
    snmp_agent varchar(256) not null,
    ip         varchar(64)  not null,
    location   uuid         not null,
    constraint unq_ne unique (snmp_agent, ip),
    constraint fk_ne_location foreign key (location) references locations (id)
);

create index idx_ne_location on network_elements (location);
create table routers
(
    id                uuid default uuid_generate_v4(),
    vendor            varchar(64)  not null,
    model             varchar(256) not null,
    eth_ports_cnt     int  default 0,
    optical_ports_cnt int  default 0
);
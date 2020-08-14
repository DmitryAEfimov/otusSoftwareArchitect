create table media_converters
(
    id                uuid default uuid_generate_v4() primary key,
    vendor            varchar(64)  not null,
    model             varchar(256) not null,
    ports_cnt         int  default 0,
    ETH_PORT_TYPE     varchar(64),
    OPTICAL_PORT_TYPE varchar(64)
);
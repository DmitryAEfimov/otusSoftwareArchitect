CREATE TABLE routers
(
    id                  uuid PRIMARY KEY,
    vendor              VARCHAR(64)  NOT NULL,
    model               VARCHAR(256) NOT NULL,
    eth_ports_cnt       INT          DEFAULT 1,
    eth_ports_speed     VARCHAR(128) DEFAULT '10/100/1000BASE-T',
    optical_ports_cnt   INT          DEFAULT 1,
    optical_ports_speed VARCHAR(128) DEFAULT '1G SFP'
);
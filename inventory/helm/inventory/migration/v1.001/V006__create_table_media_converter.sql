CREATE TABLE media_converters
(
    id                  uuid        DEFAULT uuid_generate_v4() PRIMARY KEY,
    vendor              VARCHAR(64)  NOT NULL,
    model               VARCHAR(256) NOT NULL,
    ports_cnt           INT         DEFAULT 1,
    eth_ports_speed     VARCHAR(64) DEFAULT '10/100/1000BASE-T',
    optical_ports_speed VARCHAR(64) DEFAULT '1G SFP'
);
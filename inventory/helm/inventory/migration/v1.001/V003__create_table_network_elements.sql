CREATE TABLE network_elements
(
    id         uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
    snmp_agent VARCHAR(256) NOT NULL,
    ip         VARCHAR(64)  NOT NULL,
    location   uuid         NOT NULL,
    CONSTRAINT unq_ne UNIQUE (snmp_agent, ip),
    CONSTRAINT fk_ne_location FOREIGN KEY (location) REFERENCES locations (id)
);

CREATE INDEX idx_ne_location ON network_elements (location);
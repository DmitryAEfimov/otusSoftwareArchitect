CREATE TYPE t_network_statuses AS ENUM ('Undefined', 'Active', 'OnHold', 'PreUnavailable', 'Unavailable');

CREATE TABLE network_elements
(
    id                   uuid                        DEFAULT uuid_generate_v4() PRIMARY KEY,
    snmp_agent           VARCHAR(256)       NOT NULL,
    ip                   VARCHAR(64)        NOT NULL,
    device_descriptor_id uuid,
    location             uuid               NOT NULL,
    network_status       t_network_statuses NOT NULL DEFAULT 'Undefined',
    CONSTRAINT unq_ne UNIQUE (snmp_agent, ip),
    CONSTRAINT fk_ne_location FOREIGN KEY (location) REFERENCES locations (id),
    CONSTRAINT fk_ne_dev_descr FOREIGN KEY (device_descriptor_id) REFERENCES device_descriptors (id)
);

CREATE INDEX idx_ne_location ON network_elements (location);
CREATE INDEX idx_ne_dev_descr ON network_elements (device_descriptor_id);
CREATE TABLE locations
(
    id          uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
    address     VARCHAR(1024),
    longitude   DECIMAL,
    latitude    DECIMAL,
    description VARCHAR(2048),
    CONSTRAINT unq_location UNIQUE (address, longitude, latitude),
    CONSTRAINT chk_geo CHECK ( coalesce(latitude, longitude) IS NULL OR
                               (latitude IS NOT NULL AND longitude IS NOT NULL)),
    CONSTRAINT chk_location CHECK ( address IS NOT NULL OR latitude IS NOT NULL)
);
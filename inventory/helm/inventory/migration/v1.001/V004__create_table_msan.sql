CREATE TABLE multi_service_access_nodes
(
    id                uuid PRIMARY KEY,
    vendor            VARCHAR(64)  NOT NULL,
    model             VARCHAR(256) NOT NULL,
    dsl_slots_cnt     INT DEFAULT 1,
    optical_slots_cnt INT DEFAULT 1
);
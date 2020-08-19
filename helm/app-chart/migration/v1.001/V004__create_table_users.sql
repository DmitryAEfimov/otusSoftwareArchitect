CREATE TABLE users
(
    id         uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
    email      VARCHAR(32) UNIQUE NOT NULL,
    first_name VARCHAR(32),
    last_name  VARCHAR(128),
    location   VARCHAR(256)       NOT NULL
);
CREATE UNIQUE INDEX idx_usr_email ON users (email);
CREATE TABLE credentionals
(
    id            uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
    user_id       uuid         NOT NULL,
    user_login    VARCHAR(32)  NOT NULL,
    user_password VARCHAR(256) NOT NULL
);
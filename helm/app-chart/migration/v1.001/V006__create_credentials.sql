CREATE TABLE credentials
(
    id            uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
    user_id       uuid UNIQUE  NOT NULL,
    user_login    VARCHAR(32)  NOT NULL,
    user_password VARCHAR(256) NOT NULL,
    CONSTRAINT fk_cred_to_user FOREIGN KEY (user_id) REFERENCES users (id)
);
CREATE UNIQUE INDEX idx_cred_usr ON credentials (user_id);
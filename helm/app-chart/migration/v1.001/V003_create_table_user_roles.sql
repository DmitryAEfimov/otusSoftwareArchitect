CREATE TYPE t_dictionary_type AS ENUM ('User', 'Administrator');

CREATE TABLE user_roles
(
    user_id uuid NOT NULL,
    role  t_dictionary_type NOT NULL,
    CONSTRAINT usr_roles_to_user foreign key (user_id) REFERENCES users (id)
);
CREATE INDEX idx_usr_rol_user ON user_roles (user_id);
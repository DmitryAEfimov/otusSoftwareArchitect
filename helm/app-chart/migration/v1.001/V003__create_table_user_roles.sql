CREATE TYPE t_role_type AS ENUM ('User', 'Administrator');

CREATE TABLE user_roles
(
    user_id uuid NOT NULL,
    role  t_role_type NOT NULL,
    CONSTRAINT fk_user_roles_to_users foreign key (user_id) REFERENCES users (id)
);
CREATE INDEX idx_usr_rol_user ON user_roles (user_id);
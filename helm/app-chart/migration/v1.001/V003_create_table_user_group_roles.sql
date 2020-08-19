CREATE TABLE user_group_roles
(
    user_group_id uuid NOT NULL,
    role_id       uuid NOT NULL,
    CONSTRAINT usr_group_to_user foreign key (user_group_id) REFERENCES dictionary_items (id),
    CONSTRAINT usr_group_to_group foreign key (role_id) REFERENCES dictionary_items (id)
);
CREATE INDEX idx_usr_gr_rol_grp ON user_group_roles (user_group_id);
CREATE INDEX idx_usr_gr_rol_role ON user_group_roles (role_id);
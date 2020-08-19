CREATE TABLE user_groups
(
    user_id  uuid NOT NULL,
    group_id uuid NOT NULL,
    CONSTRAINT usr_group_to_user foreign key (user_id) REFERENCES users (id),
    CONSTRAINT usr_group_to_group foreign key (group_id) REFERENCES group_dictionary (id)

);
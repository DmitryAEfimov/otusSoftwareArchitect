CREATE TYPE t_group_type AS ENUM ('User', 'Administrator');

CREATE TABLE group_dictionary
(
    id        uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
    role_type t_group_type NOT NULL
);
CREATE TYPE t_dictionary_type AS ENUM ('USER_GROUPS', 'ROLES');

CREATE TABLE dictionaries
(
    id              uuid    DEFAULT uuid_generate_v4() PRIMARY KEY,
    dictionary_type t_dictionary_type NOT NULL,
    is_system       BOOLEAN DEFAULT FALSE
);

CREATE TABLE dictionary_items
(
    id            uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
    dictionary_id uuid         NOT NULL,
    item          VARCHAR(265) NOT NULL,
    is_system     BOOLEAN,
    CONSTRAINT fk_dict_items_dict FOREIGN KEY (dictionary_id) REFERENCES dictionaries (id)
);
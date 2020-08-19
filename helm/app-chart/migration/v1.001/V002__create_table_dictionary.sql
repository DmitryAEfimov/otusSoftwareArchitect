CREATE TYPE t_dictionary_type AS ENUM ('UserGroups', 'Roles');

CREATE TABLE dictionaries
(
    id              uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
    dictionary_type t_dictionary_type NOT NULL
);

CREATE TABLE dictionary_items
(
    id            uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
    dictionary_id uuid         NOT NULL,
    value         VARCHAR(265) NOT NULL,
    is_system     BOOLEAN,
    CONSTRAINT fk_dict_items_dict FOREIGN KEY (dictionary_id) REFERENCES dictionaries (id)
);
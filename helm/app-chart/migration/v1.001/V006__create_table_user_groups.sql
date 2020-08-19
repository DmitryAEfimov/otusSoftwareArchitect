CREATE TABLE user_groups
(
    user_id  uuid NOT NULL,
    group_id uuid NOT NULL,
    CONSTRAINT fk_usr_group_usr FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT fk_usr_group_grp FOREIGN KEY (group_id) REFERENCES dictionary_items (id),
    CONSTRAINT unq_user_group UNIQUE (user_id, group_id)
);
CREATE INDEX idx_usr_grp_user ON user_groups (user_id);
CREATE INDEX idx_usr_grp_group ON user_groups (group_id);

CREATE OR REPLACE FUNCTION usr_check_group() RETURNS trigger
    LANGUAGE plpgsql AS
$group_check$
DECLARE
    l_dummy integer;
BEGIN
    SELECT 1
    INTO l_dummy
    FROM dictionary_items di
             join dictionaries d on d.id = di.dictionary_id
    WHERE di.id = NEW.group_id
      AND d.dictionary_type = 'UserGroups';

    RETURN NEW;
EXCEPTION
    WHEN no_data_found THEN
        RAISE EXCEPTION 'Unknown group with id=%', NEW.group_id;
END;
$group_check$;

CREATE OR REPLACE FUNCTION set_default_group() RETURNS trigger
    LANGUAGE plpgsql AS
$set_dflt_group$
BEGIN
    IF (NEW.group_id IS NULL) THEN
        SELECT di.id
        INTO NEW.group_id
        FROM dictionary_items di
                 JOIN dictionaries d ON d.id = di.dictionary_id
        WHERE di.value = 'Users'
          AND d.dictionary_type = 'UserGroups';
    END IF;
END;
$set_dflt_group$;

CREATE TRIGGER trg_check_group
    BEFORE UPDATE
    ON user_groups
    FOR EACH ROW
EXECUTE PROCEDURE usr_check_group();

CREATE TRIGGER trg_set_dflt_group
    BEFORE INSERT
    ON user_groups
    FOR EACH ROW
EXECUTE PROCEDURE set_default_group();
CREATE OR REPLACE PROCEDURE insert_system_roles()
    LANGUAGE plpgsql
AS
$func$
DECLARE
    l_dict_usr_group uuid := uuid_generate_v4();
    l_dict_role      uuid := uuid_generate_v4();
BEGIN
    INSERT INTO dictionaries(id, dictionary_type, is_system) VALUES (l_dict_usr_group, 'USER_GROUPS', true);
    INSERT INTO dictionaries(id, dictionary_type, is_system) VALUES (l_dict_role, 'ROLES', true);

    WITH ins_role AS (
        INSERT INTO dictionary_items (dictionary_id, item, is_system) VALUES (l_dict_role, 'Administrator', true)
            RETURNING id),
         ins_group as (
             INSERT INTO dictionary_items (dictionary_id, item, is_system) VALUES (l_dict_usr_group, 'Administrators', true)
                 RETURNING id)
    INSERT
    INTO user_group_roles (user_group_id, role_id)
    SELECT g.id, r.id
    FROM ins_group g
             CROSS JOIN ins_role r;

    WITH ins_role AS (
        INSERT INTO dictionary_items (dictionary_id, item, is_system) VALUES (l_dict_role, 'User', true)
            RETURNING id),
         ins_group as (
             INSERT INTO dictionary_items (dictionary_id, item, is_system) VALUES (l_dict_usr_group, 'Users', true)
                 RETURNING id)
    INSERT
    INTO user_group_roles (user_group_id, role_id)
    SELECT g.id, r.id
    FROM ins_group g
             CROSS JOIN ins_role r;
END
$func$;

CALL insert_system_roles();
DROP PROCEDURE insert_system_roles();
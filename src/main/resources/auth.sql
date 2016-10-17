-- 查询用户的所有角色

SELECT *
FROM auth_user_role, auth_acct_role, auth_role
WHERE auth_user_role.auth_user_id = 1 -- userId
      AND auth_user_role.delete_datetime = NULL
      AND auth_acct_role.id = auth_user_role.auth_acct_role_id
      AND auth_acct_role.org_acct_id = 89 -- acctId
      AND auth_acct_role.delete_datetime = NULL
      AND auth_role.id = auth_acct_role.auth_role_id
      AND auth_role.delete_datetime = NULL;


-- 查询用户的所有资源

SELECT *
FROM auth_user_role, auth_acct_role, auth_role, auth_role_right, auth_resource
WHERE auth_user_role.auth_user_id = 1 -- userId
      AND auth_user_role.delete_datetime = NULL
      AND auth_acct_role.id = auth_user_role.auth_acct_role_id
      AND auth_acct_role.org_acct_id = 89 -- acctId
      AND auth_acct_role.delete_datetime = NULL
      AND auth_role.id = auth_acct_role.auth_role_id
      AND auth_role.delete_datetime = NULL
      AND auth_role_right.auth_role_id = auth_role.id
      AND auth_role_right.delete_datetime = NULL
      AND auth_resource.id = auth_role_right.auth_resource_id
      AND auth_resource.delete_datetime = NULL;
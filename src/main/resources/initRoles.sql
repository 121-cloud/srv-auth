
-- 设置企业
select @acctId:=89;

-- 设置用户
select @userId:=16;

-- 添加角色
insert INTO auth_role (role_name, role_type_code, entry_id, entry_datetime)
values ('企业管理员', 'ACCT_MANAGER', 0, NOW());

select @roleId:= LAST_INSERT_ID();

-- 添加企业中的角色
insert into auth_acct_role (org_acct_id, auth_role_id, auth_type_code, entry_id, entry_datetime)
VALUES (@acctId, @roleId, 'A', 0, NOW());

-- 获得最新的企业角色的ID
select @newId:= LAST_INSERT_ID();

-- 添加duan用户。
INSERT INTO auth_user_role (auth_user_id, auth_acct_role_id, auth_type_code, entry_id, entry_datetime)
VALUES (@userId, @newId, 'A', 0, NOW());

package otocloud.auth.mybatis.mapper;

import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import otocloud.auth.mybatis.MyBatisSqlSessionFactory;
import otocloud.auth.mybatis.entity.AuthRole;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * zhangyef@yonyou.com on 2016-01-04.
 */
public class AuthRoleMapperTest {
    protected Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    private SqlSessionFactory sqlSessionFactory;
    private SqlSession sqlSession;

    private AuthRoleMapper authRoleMapper;

    @Before
    public void setUp() {
        sqlSessionFactory = MyBatisSqlSessionFactory.getSqlSessionFactory();
        sqlSession = sqlSessionFactory.openSession();
        authRoleMapper = sqlSession.getMapper(AuthRoleMapper.class);
    }

    @After
    public void tearDown() {
        sqlSession.close();
    }

    /**
     * Notice! 执行测试前,请确保数据库中有相应数据.
     * 可以调用初始化脚本,插入数据.
     */
    @Test
    public void it_should_get_a_list_of_roles() {
        int userId = 17;
        int acctId = 89;
        List<AuthRole> roles = authRoleMapper.getRoles(userId, acctId);

        assertTrue(roles.size() > 0);
    }

    @Test
    public void it_should_insert_a_auth_role() {
        int userId = 16;
        int acctId = 89;

        String roleName = "企业管理员test";
        String roleTypeCode = "ACCT_MANAGER";

        //插入角色
        AuthRole role = new AuthRole();
        role.setRoleName(roleName);
        role.setRoleTypeCode(roleTypeCode);
        role.setEntryId(0);
        role.setEntryDatetime(new Date());

        authRoleMapper.insert(role);

        sqlSession.commit();

        int roleId = role.getId();

        assertTrue(roleId != 0);
    }
}

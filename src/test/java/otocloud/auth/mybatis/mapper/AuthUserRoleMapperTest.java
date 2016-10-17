package otocloud.auth.mybatis.mapper;

import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import otocloud.auth.mybatis.MyBatisSqlSessionFactory;
import otocloud.auth.mybatis.entity.AuthUserRole;

import java.util.Date;

import static org.junit.Assert.*;

/**
 * zhangyef@yonyou.com on 2016-01-07.
 */
public class AuthUserRoleMapperTest {
    protected Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    private SqlSessionFactory sqlSessionFactory;
    private SqlSession sqlSession;

    private AuthUserRoleMapper authUserRoleMapper;

    @Before
    public void setUp() {
        sqlSessionFactory = MyBatisSqlSessionFactory.getSqlSessionFactory();
        sqlSession = sqlSessionFactory.openSession();
        authUserRoleMapper = sqlSession.getMapper(AuthUserRoleMapper.class);
    }

    @After
    public void tearDown() {
        sqlSession.close();
    }

    @Test
    public void it_should_insert_a_user_role(){
        AuthUserRole authUserRole = new AuthUserRole();
        int userId = 12;
        int acctRoleId = 9;
        authUserRole.setAuthUserId(userId);
        authUserRole.setAuthAcctRoleId(acctRoleId);
        authUserRole.setEntryId(0);
        authUserRole.setAuthTypeCode("A");
        authUserRole.setEntryDatetime(new Date());

        authUserRoleMapper.insert(authUserRole);

        sqlSession.commit();

        assertTrue(authUserRole.getId() != 0);
    }
}

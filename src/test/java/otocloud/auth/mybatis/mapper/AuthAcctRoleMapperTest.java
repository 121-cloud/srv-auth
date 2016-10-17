package otocloud.auth.mybatis.mapper;

import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import otocloud.auth.mybatis.MyBatisSqlSessionFactory;
import otocloud.auth.mybatis.entity.AuthAcctRole;

import java.util.Date;

import static org.junit.Assert.*;

/**
 * zhangyef@yonyou.com on 2016-01-07.
 */
public class AuthAcctRoleMapperTest {
    protected Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    private SqlSessionFactory sqlSessionFactory;
    private SqlSession sqlSession;

    private AuthAcctRoleMapper authAcctRoleMapper;

    @Before
    public void setUp() {
        sqlSessionFactory = MyBatisSqlSessionFactory.getSqlSessionFactory();
        sqlSession = sqlSessionFactory.openSession();
        authAcctRoleMapper = sqlSession.getMapper(AuthAcctRoleMapper.class);
    }

    @After
    public void tearDown() {
        sqlSession.close();
    }

    @Test
    public void it_should_insert_a_acct_role(){
        AuthAcctRole authAcctRole = new AuthAcctRole();
        int acctId = 89;
        int roleId = 14;
        authAcctRole.setOrgAcctId(acctId);
        authAcctRole.setAuthRoleId(roleId);
        authAcctRole.setAuthTypeCode("A");
        authAcctRole.setEntryId(0);
        authAcctRole.setEntryDatetime(new Date());

        authAcctRoleMapper.insert(authAcctRole);

        sqlSession.commit();

        assertTrue(authAcctRole.getId() != 0);
    }

}

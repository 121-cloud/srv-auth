package otocloud.auth.mybatis.mapper;

import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import otocloud.auth.mybatis.MyBatisSqlSessionFactory;
import otocloud.auth.mybatis.entity.AuthUser;
import otocloud.auth.mybatis.entity.AuthUserExample;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * zhangyef@yonyou.com on 2015-10-29.
 */
public class AuthUserMapperTest {
    protected Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    private SqlSessionFactory sqlSessionFactory;
    private SqlSession sqlSession;
    private AuthUserMapper authUserMapper;

    @Before
    public void setUp() {
        sqlSessionFactory = MyBatisSqlSessionFactory.getSqlSessionFactory();
        sqlSession = sqlSessionFactory.openSession();
        authUserMapper = sqlSession.getMapper(AuthUserMapper.class);
    }

    @After
    public void tearDown() {
        sqlSession.close();
    }

    @Test
    public void it_should_get_a_list_of_users_with_mappter() {
        List<AuthUser> userList = authUserMapper.selectByAcctAndDept(123, 0);
        System.out.println(userList.size());
    }

    /**
     * 测试用户列表的分页查询.
     */
    @Test
    public void it_should_get_a_page_of_user_list() {
        int acctId = 89;
        int deptId = 0;
        AuthUserExample authUserExample = new AuthUserExample();
        authUserExample.createCriteria().andOrgAcctIdEqualTo(acctId).andOrgDeptIdEqualTo(deptId);

        int page_index = 0;
        int page_size = 5;
        RowBounds bounds = new RowBounds(page_index * page_size, page_size);

        List<AuthUser> list = authUserMapper.selectByExampleWithRowbounds(authUserExample, bounds);

        int size = list.size();
        assertTrue(size <= page_size);
    }

    @Test
    public void it_should_insert_a_user() {

        AuthUser authUser = new AuthUser();
        authUser.setName("inserted by mybatis");
        authUser.setPassword("*123");
        authUser.setCellNo("12312312399");
        authUser.setEntryDatetime(new Date());

        authUserMapper.insertSelective(authUser);

        sqlSession.commit();
    }

    @Test
    public void it_should_get_a_list_of_users() {
        List<AuthUser> userList = authUserMapper.selectByAcctAndDept(123, 45);

        if (userList.size() > 0) {
            logger.info(userList.get(0).toString());
            for (AuthUser user : userList) {
                Assert.assertEquals(123, user.getOrgAcctId().intValue());
                Assert.assertEquals(45, user.getOrgDeptId().intValue());
            }
        }

    }

    @Test
    public void it_should_be_a_duplicated_cell_no() {
        boolean existed = authUserMapper.isRegisteredCellNo("12345678901");
        System.out.println(existed);
        Assert.assertTrue(existed);
    }

    @Test
    public void it_should_be_a_unregistered_cell_no() {
        boolean existed = authUserMapper.isRegisteredCellNo("123456789!!");
        System.out.println(existed);
        Assert.assertFalse(existed);
    }

    @Test
    public void it_should_insert_a_list_of_users() {
        List<AuthUser> users = new LinkedList<>();
        AuthUser user = new AuthUser();
        user.setErpUserCode("erp_code");
        user.setConnectedWithErp("Y");
        users.add(user);

        AuthUser user2 = new AuthUser();
        user2.setErpUserCode("erp_code2");
        user2.setConnectedWithErp("Y");
        users.add(user2);

        int num = authUserMapper.insertBatch(users);

        sqlSession.commit();

        Assert.assertTrue(num > 0);
    }
}

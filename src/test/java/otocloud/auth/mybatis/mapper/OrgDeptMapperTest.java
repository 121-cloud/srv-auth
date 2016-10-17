package otocloud.auth.mybatis.mapper;

import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import otocloud.auth.mybatis.MyBatisSqlSessionFactory;
import otocloud.auth.mybatis.entity.OrgDept;

import java.util.List;

import static org.junit.Assert.*;

/**
 * zhangyef@yonyou.com on 2015-12-17.
 */
public class OrgDeptMapperTest {
    protected Logger logger = LoggerFactory.getLogger(OrgDeptMapperTest.class);

    private SqlSessionFactory sqlSessionFactory;
    private SqlSession sqlSession;
    private OrgDeptMapper orgDeptMapper;

    @Before
    public void setUp(){
        sqlSessionFactory = MyBatisSqlSessionFactory.getSqlSessionFactory();
        sqlSession = sqlSessionFactory.openSession();
        orgDeptMapper = sqlSession.getMapper(OrgDeptMapper.class);
    }

    @After
    public void tearDown() {
        sqlSession.close();
    }

    @Test
    public void it_should_get_a_list_of_departments_by_acctId(){
        List<OrgDept> deptList = orgDeptMapper.selectByAcct(89);

        if(deptList.isEmpty()){
            return;
        }

        OrgDept dept = deptList.get(0);
        assertNotNull(dept.getDeptName());
    }
}

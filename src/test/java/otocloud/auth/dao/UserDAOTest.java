package otocloud.auth.dao;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.sql.UpdateResult;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import otocloud.auth.entity.User;

/**
 * UserDAO Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>十月 15, 2015</pre>
 */

public class UserDAOTest extends OtoBaseDAOTest {

    private UserDAO userDAO;

    @Before
    public void before(TestContext context) throws Exception {
        super.before(context);
        userDAO = injector.getInstance(UserDAO.class);//.getBean(UserDAO.class);
    }

    @After
    public void after(TestContext context) throws Exception {
        super.after(context);
    }

    /**
     * Method: create(User user, Future<Void> future)
     */
    @Test
    public void testCreate(TestContext context) throws Exception {
        final Async async = context.async();
        User user = new User();
        user.setOrgAcctId(111111);
        user.setUserName("auth_test_userName");
        user.setPassword("auth_test_password*123");
        user.setCellNo("auth_test_cell_no");
        user.setEmail("auth_test_email");
        Future<UpdateResult> future = Future.future();

        //先清理已经存在的信息
        userDAO.deleteBy(new JsonObject().put("cell_no", "auth_test_cell_no"), future);

        future.setHandler(ret -> {
            Future<User> userFuture = Future.future();
            userDAO.create(user, userFuture);

            userFuture.setHandler(result -> {
                if (result.succeeded()) {
                    async.complete();
                }
            });
        });

    }

    @Test
    public void it_should_get_an_user(TestContext context){
        final Async async = context.async();

        Future<User> future = Future.future();
        userDAO.findBy("UpdatedName", "*123", future);
        future.setHandler(ret -> {
            if(ret.succeeded()){
                User user = ret.result();
                async.complete();
            }
        });
    }
    @Test
    public void it_should_merge_an_user(TestContext context){
        final Async async = context.async();

        User user = new User();
        user.setID(82);
        user.setOrgAcctId(25);
        user.setPassword("merged_password*123"); //修改密码
        user.setEmail("merged_email"); //修改邮箱

        Future<User> future = Future.future();
        userDAO.merge(user, future);

        future.setHandler(ret -> {
            if (ret.succeeded()) {
                System.out.println(ret.result());
                async.complete();
            }
        });
    }
    @Test
    public void it_should_update_an_user(TestContext context) {
        final Async async = context.async();

        User user = new User();
        user.setID(79);
        user.setOrgAcctId(25);
        user.setUserName("auth_test_userName");
        user.setPassword("auth_test_password*123");
        user.setCellNo("auth_test_cell_no");
        user.setEmail("auth_test_email");

        Future<User> future = Future.future();
        userDAO.update(user, future);

        future.setHandler(ret -> {
            if (ret.succeeded()) {
                System.out.println(ret.result());
                async.complete();
            }
        });
    }

    /**
     * Method: deleteById(String userId)
     *
     * @param context
     */
    @Test
    public void it_should_delete_an_user_by_id(TestContext context) {
        final Async async = context.async();
        int userId = 73;
        Future<Boolean> future = Future.future();
        userDAO.deleteById(userId, future);

        future.setHandler(ret -> {

            async.complete();
        });
    }


} 

/*package otocloud.auth.service;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import otocloud.auth.base.GuiceContextTestBase;
import otocloud.auth.common.RSAUtil;
import otocloud.auth.entity.User;



*//**
 * zhangyef@yonyou.com on 2015-10-30.
 *//*
@RunWith(VertxUnitRunner.class)
public class UserServiceTest extends GuiceContextTestBase {
    protected Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    private UserService userService;

    @Before
    public void setUp(TestContext context){
        final Async async = context.async();
        super.setUp();

        userService = injector.getInstance(UserService.class);
        async.complete();
    }

    @After
    public void tearDown(TestContext context){
        vertx.close(context.asyncAssertSuccess());
    }

    @Test
    public void it_should_delete_a_user(TestContext context){
        final Async async = context.async();

        int idForDelete = 8;

        Future<User> delFuture = Future.future();

        userService.deleteById(idForDelete, delFuture);

        delFuture.setHandler( ret -> {
            if(ret.succeeded()){
                User user = ret.result();
                if(user != null) {
                    Assert.assertNotNull(user.getOrgAcctId());
                }
                async.complete();
            }else{
                context.fail();
            }
        });
    }

    @Test
    public void it_should_get_a_duplicated_cellNo_message(TestContext context) {
        final Async async = context.async();
        Future<Boolean> dupFuture = Future.future();

        String cellNo = "12345678901";

        userService.verifyCellNo(cellNo, dupFuture);
        dupFuture.setHandler(ret -> {
            if(ret.succeeded()){

                context.assertTrue(ret.result());
                async.complete();

            }else {
                context.fail();
            }
        });

    }

    @Test
    public void it_should_be_a_unique_cellNo(TestContext context) {
        final Async async = context.async();
        Future<Boolean> uniqueFuture = Future.future();

        String cellNo = "~~";

        userService.verifyCellNo(cellNo, uniqueFuture);
        uniqueFuture.setHandler(ret -> {
            if(ret.succeeded()){
                context.assertFalse(ret.result());
                async.complete();

            }else {
                context.fail();
            }
        });
    }

    private String getPassword(){
        return RSAUtil.encrypt("*123");
    }

    @Test
    public void it_should_login_with_cellNO(TestContext context) {
        final Async async = context.async();
        Future<JsonObject> loginFuture = Future.future();
        String cellNo = "12345678901";
        String password = getPassword();

        userService.loginWithCellNo(cellNo, password, null, loginFuture);

        loginFuture.setHandler(ret -> {
            if (ret.succeeded()){
                JsonObject reply = ret.result();
                context.assertNotNull(reply.getString("user_openid"));
                context.assertNotNull(reply.getString("access_token"));

            }else{
                context.fail();
            }

            async.complete();
        });
    }

//    @Test
    public void it_should_print_colorful_lines() {
        logger.trace("Test Console Color - Trace");
        logger.debug("Test Console Color - Debug");
        logger.info("Test Console Color - Info");
        logger.warn("Test Console Color - Warn");
        logger.error("Test Console Color - Error");
        logger.fatal("Test Console Color - Fatal");
    }

    @Test
    public void it_should_add_a_manager_role(TestContext context){
        final Async async = context.async();

        User user = new User();
        user.setID(17);
        user.setOrgAcctId(89);
        Future<User> addRoleFuture = Future.future();

        userService.addManagerRole(user, addRoleFuture);

        addRoleFuture.setHandler(ret -> {


            async.complete();
        });
    }

}
*/
/*package otocloud.auth.command;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import org.junit.Assert;
import org.junit.Test;
import otocloud.auth.base.GuiceContextTestBase;
import otocloud.auth.command.framework.CommandContext;
import otocloud.auth.service.UserService;

import javax.validation.ConstraintViolationException;
import java.util.concurrent.CountDownLatch;

*//**
 * Created by zhangye on 2015-10-10.
 *//*
public class AddUserCommandTest extends GuiceContextTestBase{
//    AnnotationConfigApplicationContext ctx;

    @Test
    public void it_should_add_a_user() {
        final CountDownLatch latch = new CountDownLatch(1);

        AddUserCommand command = injector.getInstance(AddUserCommand.class);//ctx.getBean(AddUserCommand.class);

        JsonObject userInfo = new JsonObject()
                .put("userName", "zhang")
                .put("password", "*123")
                .put("cellNo", "12345678901");
        CommandContext context = new CommandContext();
        context.put("data", userInfo);

        Future<JsonObject> future = Future.future();
        command.executeFuture(context, future);

        future.setHandler(ret -> {
            if(ret.failed()){
                latch.countDown();
                return;
            }

            JsonObject event = ret.result();
            int errCode = event.getInteger("errCode");

            if (errCode == 0) {
                System.out.println(event.getJsonObject("data"));
            } else {
                System.out.println(event.getString("errMsg"));
            }

            latch.countDown();
        });

        try {
            latch.await();
        } catch (InterruptedException e) {

        }
    }

    @Test(expected = ConstraintViolationException.class)
    public void it_should_validate() {
        UserService service = injector.getInstance(UserService.class);//ctx.getBean("userService");

        Future<JsonObject> future = Future.future();
        service.login(null, null, null, future);
    }

    @Test
    public void it_should_have_an_empty_name_error() {
        final CountDownLatch latch = new CountDownLatch(3);

        AddUserCommand command = injector.getInstance(AddUserCommand.class);//ctx.getBean(AddUserCommand.class);

        JsonObject userInfo = new JsonObject().put("userName", "").put("password", "*123");

        tryAdd(latch, command, userInfo);

        userInfo = new JsonObject().put("password", "*123");
        tryAdd(latch, command, userInfo);

        userInfo = new JsonObject().put("userName", "name1");
        tryAdd(latch, command, userInfo);

        try {
            latch.await();
        } catch (InterruptedException e) {

        }
    }

    private void tryAdd(CountDownLatch latch, AddUserCommand command, JsonObject userInfo) {
        CommandContext context;
        context = new CommandContext();
        context.put("data", userInfo);

        Future<JsonObject> addFuture = Future.future();

        command.executeFuture(context, addFuture);
        addFuture.setHandler(ret -> {
            if(ret.failed()){
                latch.countDown();
                String err = ret.cause().getMessage();
                JsonObject errBody = new JsonObject(err);
                Assert.assertEquals("添加用户失败", 1, errBody.getInteger("errCode").intValue());
                return;
            }
            int errCode = ret.result().getInteger("errCode");
            Assert.assertEquals("添加用户失败", 1, errCode);
            latch.countDown();
        });
    }
}
*/
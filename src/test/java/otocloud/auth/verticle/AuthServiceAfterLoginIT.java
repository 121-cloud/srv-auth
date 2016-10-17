package otocloud.auth.verticle;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.config.EncoderConfig;
import com.jayway.restassured.config.RestAssuredConfig;
import com.jayway.restassured.response.Response;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.hamcrest.CoreMatchers;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import otocloud.auth.common.RSAUtil;
import otocloud.auth.entity.MonitorInfoSchema;

import java.util.UUID;

import static com.jayway.restassured.RestAssured.given;
import static org.junit.Assert.*;

/**
 * 测试用户登录后的功能.
 * zhangyef@yonyou.com on 2015-12-16.
 */
@RunWith(VertxUnitRunner.class)
public class AuthServiceAfterLoginIT {
    protected static final Logger logger = LoggerFactory.getLogger(AuthServiceAfterLoginIT.class);

    static AuthServiceDaemon authServiceDaemon;

    static int HTTP_PORT = 8081;

    static Vertx vertx;

    static String globalToken;

    @BeforeClass
    public static void setUp(TestContext context) {
        final Async async = context.async();

        vertx = Vertx.vertx();

        authServiceDaemon = new AuthServiceDaemon(vertx);
        Future<Void> daemonFuture = Future.future();
        authServiceDaemon.start(daemonFuture);

        daemonFuture.setHandler(ret -> {
            //执行登录
            Response response = given()
                    .body(makeLoginInfo().toString())
                    .post("/api/otocloud-auth/user-management/users/actions/login");

            JsonObject bodyJson = new JsonObject(response.asString());

            assertTrue("无法登录.", bodyJson.containsKey("access_token"));
            assertTrue(bodyJson.containsKey("user_openid"));

            String token = bodyJson.getString("access_token");

            //存储token
            globalToken = token;

            async.complete();
        });

        configureRestAssured();
    }

    private static String encryptPassword(String password) {
        return RSAUtil.encrypt(password);
    }

    private static JsonObject makeLoginInfo() {
        JsonObject loginInfo = new JsonObject();
        loginInfo.put("userName", "zhangye");
        loginInfo.put("password", encryptPassword("*123"));
        return loginInfo;
    }

    @AfterClass
    public static void tearDown(TestContext context) {
        Async async = context.async();

        Future<Void> stopFuture = Future.future();

        authServiceDaemon.stop(stopFuture);

        stopFuture.setHandler(ret -> {
            unconfigureRestAssured();

            logger.info("测试完毕。");

            async.complete();
        });

    }

    public static void unconfigureRestAssured() {
        RestAssured.reset();
    }

    public static void configureRestAssured() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = HTTP_PORT;

        EncoderConfig encoderConfig = new EncoderConfig().defaultContentCharset("UTF-8");
        RestAssured.config = new RestAssuredConfig().encoderConfig(encoderConfig);

        logger.info("系统端口号：" + RestAssured.port);
    }

    @Test
    public void it_should_has_a_token_after_login(TestContext context) {
        final Async async = context.async();
        assertNotNull(globalToken);
        async.complete();
    }


    /**
     * 获得登录账户所在企业的部门列表
     *
     * @param context
     */
    @Test
    public void it_should_get_a_list_of_departments(TestContext context) {
        final Async async = context.async();

        String url = "/api/otocloud-auth/user-management/departments";
        Response response = given()
                .params("token", globalToken)
                .get(url);
        JsonArray jsonArray = new JsonArray();

        try {
            jsonArray = new JsonArray(response.asString());
        } catch (Exception ignore) {
            fail("返回的结果中不包含JsonArray。");
        }

        if (!jsonArray.isEmpty()) {
            JsonObject element = jsonArray.getJsonObject(0);
            assertTrue(element.containsKey("id") && element.containsKey("dept_name"));
            assertNotNull(element.getInteger("id"));
            assertNotNull(element.getString("dept_name"));
        }

        async.complete();
    }

    /**
     * 测试用户列表的分页.
     *
     * @param context
     */
    @Test
    public void it_should_get_a_page_of_userList(TestContext context) {
        final Async async = context.async();
        String url = "/api/otocloud-auth/user-management/users/page";
        Response response = given()
                .queryParam("token", globalToken)
                .body(new JsonObject()
                        .put("page_index", 0)
                        .put("page_size", 5)
                        .put("department_id", 0).toString())
                .post(url);
        JsonObject replyData = new JsonObject(response.asString());

        if (!replyData.containsKey("errCode")) {
            assertTrue(replyData.containsKey("page_num"));
            assertTrue(replyData.containsKey("page_index"));
            assertTrue(replyData.containsKey("items_total_num"));
            assertTrue(replyData.containsKey("items_in_page"));

            JsonArray items = replyData.getJsonArray("items_in_page");
        } else {
            fail(replyData.getString("errMsg"));
        }

        async.complete();
    }

    @Test
    public void it_should_create_an_user_by_post(TestContext context) {
        final Async async = context.async();

        Response response = given().body(makeAnUser().toString())
                .queryParam("token", globalToken)
                .post("/api/otocloud-auth/user-management/users");

        JsonObject reply = new JsonObject(response.asString());
        if (reply.containsKey("errCode") && reply.getInteger("errCode") != 0) {
            context.fail(reply.getString("errMsg"));
        }
        context.assertNotNull(reply.getJsonObject("data").getInteger("userId"));

        async.complete();
    }

    @Test
    public void it_should_delete_an_user(TestContext context) {
        final Async async = context.async();

        int userId = 9;
        given().body(makeLoginInfo().toString())
                .queryParam("token", globalToken)
                .delete("/api/otocloud-auth/user-management/users/" + userId)
                .then().assertThat()
                .body("errCode", CoreMatchers.notNullValue())
                .body("errMsg", CoreMatchers.notNullValue());

        async.complete();
    }

    @Test
    public void it_should_bind_erp_account(TestContext context) {
        final Async async = context.async();
        JsonObject erpAccount = new JsonObject();
        erpAccount.put("erp_usercode", "bind test");
        erpAccount.put("erp_password", "bind password");
        Response response =
                given().body(erpAccount.toString())
                        .queryParam("token", globalToken)
                        .post("/api/otocloud-auth/erp-connection/users/bind");

        JsonObject reply = new JsonObject(response.asString());

        assertTrue(reply.containsKey("isBound"));
        assertEquals(reply.getBoolean("isBound"), true);

        async.complete();
    }

    @Test
    public void it_should_get_bind_info(TestContext context) {
        final Async async = context.async();
        Response response =
                given().queryParam("token", globalToken)
                        .get("/api/otocloud-auth/erp-connection/users/bind");

        JsonObject reply = new JsonObject(response.asString());
        assertTrue(reply.containsKey("isBound"));

        boolean bound = reply.getBoolean("isBound");
        if (bound) {
            assertTrue(reply.containsKey("erp_usercode"));
            assertNotNull(reply.getString("erp_usercode"));
        }
        System.out.println(reply);

        async.complete();
    }

    @Test
    public void it_should_unbind_erp_account(TestContext context) {
        final Async async = context.async();
        Response response =
                given().queryParam("token", globalToken)
                        .delete("/api/otocloud-auth/erp-connection/users/bind");

        JsonObject reply = new JsonObject(response.asString());
        assertTrue(reply.containsKey("isBound"));

        boolean bound = reply.getBoolean("isBound");
        assertFalse(bound);

        async.complete();
    }

    @Test
    public void it_should_create_a_biz_operator(TestContext context) {
        final Async async = context.async();

        JsonObject request = new JsonObject();
        request.put("user_name", "管理员手动创建的用户名");
        request.put("cell_no", "手机号");
        request.put("email", "someone@test.com");

        Response response =
                given().queryParam("token", globalToken)
                        .body(request.toString())
                        .post("/api/otocloud-auth/user-management/users/operators");

        logger.info(response.asString());

        JsonObject reply = new JsonObject(response.asString());
        async.complete();
    }

    @Test
    public void it_should_get_user_number(TestContext context) {
        final Async async = context.async();

        Response response =
                given().queryParam("token", globalToken)
                        .get("/api/otocloud-auth/query-management/users/statistics");

        JsonObject reply = new JsonObject(response.asString());

        System.out.println(reply);

        assertTrue(reply.containsKey("total_user_number"));
        assertTrue(reply.containsKey("active_user_number"));
        assertTrue(reply.containsKey("inactive_user_number"));
        assertTrue(reply.containsKey("forbidden_user_number"));
        assertTrue(reply.containsKey("unknown_user_number"));

        int total_user_number = reply.getInteger("total_user_number");
        int active_user_number = reply.getInteger("active_user_number");
        int inactive_user_number = reply.getInteger("inactive_user_number");
        int forbidden_user_number = reply.getInteger("forbidden_user_number");
        int unknown_user_number = reply.getInteger("unknown_user_number");

        assertTrue(total_user_number ==
                active_user_number + inactive_user_number + forbidden_user_number + unknown_user_number);

        async.complete();
    }


    /**
     * 要求管理员角色.
     *
     * @param context
     */
    @Test
    public void it_should_import_users_from_nc65(TestContext context) {
        final Async async = context.async();

        GatewayMockUp gatewayMockUp = new GatewayMockUp();
        gatewayMockUp.mockUpImport();

        //监听进度
        String monitorAddress = "otocloud-auth.user-management.users.erp.import.process.monitor";
        vertx.eventBus().<JsonObject>consumer(monitorAddress, msg -> {
            System.out.println(msg.body());

            JsonObject monitorInfo = msg.body();
            int finishedWork = monitorInfo.getInteger(MonitorInfoSchema.FINISHED_WORK);
            int totalWork = monitorInfo.getInteger(MonitorInfoSchema.TOTAL_WORK);
            if (finishedWork == totalWork) {
                gatewayMockUp.tearDownImport();
                async.complete();
            }
        });

        String importUrl = "/api/otocloud-auth/user-management/users/actions/erp/import";

        Response response = given().queryParam("token", globalToken).get(importUrl);
        JsonObject responseBody = new JsonObject(response.asString());

        assertTrue(responseBody.containsKey("result"));

//        assertTrue(responseBody.containsKey("progress_monitor_address"));
    }

    @Test
    public void it_should_get_import_monitor_address(TestContext context) {
        String importUrl = "/api/otocloud-auth/user-management/users/actions/erp/import?monitor_address";

        Response response = given().queryParam("token", globalToken).get(importUrl);
        JsonObject responseBody = new JsonObject(response.asString());

        assertTrue(responseBody.containsKey("progress_monitor_address"));
    }

    private JsonObject makeAnUser() {
        JsonObject userInfo = new JsonObject()
                .put("name", "zhangye")
                .put("password", "*123")
                .put("org_acct_id", 123)
                .put("cell_no", UUID.randomUUID().toString().substring(0, 11)) //随机生成用于测试的手机.
                .put("email", "test@yonyou.com");
        return userInfo;
    }

}

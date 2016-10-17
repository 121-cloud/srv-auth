package otocloud.auth.verticle;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpClientRequest;
import io.vertx.core.http.HttpClientResponse;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.sql.UpdateResult;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import mockit.Mock;
import mockit.MockUp;
import org.hamcrest.CoreMatchers;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import otocloud.auth.common.RSAUtil;
import otocloud.auth.dao.UserDAO;
import otocloud.auth.entity.User;
import otocloud.common.Command;
import otocloud.common.CommandResult;
import otocloud.common.CommandScheme;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;


/**
 * Created by zhangye on 2015-10-14.
 */
@RunWith(VertxUnitRunner.class)
public class AuthServiceIT {

    protected static final Logger logger = LoggerFactory.getLogger(AuthServiceIT.class);
    private static final int PORT = 8081;
    static Vertx vertx;
    static HttpClient client;

    static AuthServiceDaemon authServiceDaemon;

    @BeforeClass
    public static void setUp(TestContext context) {

        final Async async = context.async();

        vertx = Vertx.vertx();

        authServiceDaemon = new AuthServiceDaemon(vertx);
        Future<Void> daemonFuture = Future.future();
        authServiceDaemon.start(daemonFuture);

        daemonFuture.setHandler(ret -> {
            async.complete();
        });

        client = vertx.createHttpClient(new HttpClientOptions()
                .setDefaultHost("localhost").setDefaultPort(PORT));

        configureRestAssured();
    }

    public static void configureRestAssured() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = PORT;

        logger.info("系统端口号：" + RestAssured.port);
    }

    @AfterClass
    public static void tearDown(TestContext context) {
        //多层异步嵌套时，必须首先调用async()方法。
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


    /**
     * 测试Session的应用
     *
     * @param complete
     */
    private void startApp(Future<Void> complete) {
        String realAddress = "address.real";

        JsonObject apiInfo = new JsonObject();
        apiInfo.put("address", "address.template");
        apiInfo.put("decoratingAddress", "address.url_resolver");
        apiInfo.put("uri", "/applist");
        apiInfo.put("method", "get");
        apiInfo.put("messageFormat", "command");

        //注册API
        vertx.eventBus().send(AuthServiceDaemon.WEBSERVER_NAME + ".platform.register.rest.to.webserver", apiInfo,
                result -> {
                    if (result.succeeded()) {
                        logger.info("App的API注册成功.");
                        complete.complete();
                    }
                });

        //目标地址解析
        vertx.eventBus().<JsonObject>consumer("address.url_resolver", msg -> {
            JsonObject lookup = msg.body();
            String addressPattern = lookup.getString("addressPattern");
            Assert.assertNotNull(addressPattern);

            JsonObject session = lookup.getJsonObject("session");
            Integer acctId = session.getInteger("acctId");
            Assert.assertNotNull(acctId);

            JsonObject reply = new JsonObject().put("realAddress", realAddress);
            msg.reply(reply);
        });

        //应用的真实地址
        Command.consumer(vertx, realAddress, cmd -> {
            JsonObject reply = new JsonObject();
            reply.put("somedata", "data1");

            cmd.succeed(vertx, reply);
        });

    }

    @Test
    public void it_should_get_applist_with_session(TestContext context) {
        final Async async = context.async();

        Future<Void> voidFuture = Future.future();

        startApp(voidFuture);

        String requestURL = "/api/applist";

        HttpClient client = vertx.createHttpClient();

        String loginURL = "/api/otocloud-auth/user-management/users/actions/login";

        voidFuture.setHandler(ret -> {
            if (ret.failed()) {
                context.fail();
            }

            JsonObject loginInfo = makeLoginInfo();
            Future<JsonObject> loginFuture = Future.future();

            client.post(PORT, "localhost", loginURL, response -> {
                response.bodyHandler(body -> {
                    JsonObject reply = new JsonObject(body.toString());

                    context.assertNotNull(reply.getString("access_token"),
                            "没有登录成功,请检查数据库中是否存在用户:" + loginInfo.toString());

                    loginFuture.complete(reply);
                });

            }).end(loginInfo.toString());

            loginFuture.setHandler(loginRet -> {
                if (loginRet.succeeded()) {

                    JsonObject loginReply = loginRet.result();
                    String appRequestURL = requestURL + "?token=" + loginReply.getString("access_token");

                    client.getNow(PORT, "localhost", appRequestURL,
                            response -> {
                                response.bodyHandler(body -> {
                                    JsonObject appListBody = new JsonObject(body.toString());

                                    JsonArray datas = appListBody.getJsonArray("data");

                                    context.assertTrue(datas.size() > 0);

                                    JsonObject data1 = datas.getJsonObject(0);

                                    context.assertNotNull(data1.getString("somedata"));
                                });
                                async.complete();
                            });
                }
            });

        });

    }

    /**
     * 不需要登录,通过后台接口直接创建用户.
     * @param context
     */
    @Test
    public void it_should_create_an_user_by_eventbus(TestContext context) {
        final Async async = context.async();

        JsonObject requestMsg = new JsonObject();
        requestMsg.put("content", makeAnUser());

        EventBus bus = vertx.eventBus();
        bus.<JsonObject>send("otocloud-auth.user-management.users.post", requestMsg, result -> {
            if (result.succeeded()) {
                Message<JsonObject> message = result.result();
                JsonObject reply = message.body();
                context.assertEquals(0, reply.getInteger("errCode"));
                context.assertNotNull(reply.getJsonObject("data").getInteger("userId"));

                async.complete();
            } else {
                context.fail(result.cause());
            }
        });

    }

    @Test
    public void it_should_not_login(TestContext context) {
        final Async async = context.async();

        Response response = given()
                .body(makeWrongLoginInfo().toString())
                .post("/api/otocloud-auth/user-management/users/actions/login");

        response.then().assertThat()
                .body("errCode", CoreMatchers.notNullValue())
                .body("userName", CoreMatchers.nullValue())
                .body("accessToken", CoreMatchers.nullValue())
                .body("expiresIn", CoreMatchers.nullValue());

        async.complete();
    }

    private JsonObject makeWrongLoginInfo() {
        JsonObject loginInfo = new JsonObject();
        loginInfo.put("user_name", "a wrong name");
        loginInfo.put("password", "a wrong password");
        return loginInfo;
    }

    @Test
    public void testDebugInfo(TestContext context) {
        final Async async = context.async();
        logger.debug("testDebugInfo");
        logger.warn("testDebugInfo");
        logger.info("testDebugInfo");
        async.complete();
    }

    //        @Test
    public void it_should_login_and_get_token(TestContext context) {
        final Async async = context.async();

        MockUp mockUp = new MockUp<UserDAO>() {
            @Mock
            public void findBy(String userName, String password, Future<User> future) {
                User user = new User();
                user.setID(2);
                user.setOrgAcctId(89);
                user.setUserName("testName");

                future.complete(user);
            }

            @Mock
            public void setLoginDateTime(String userId, Future<UpdateResult> future) {
                JsonObject updated = new JsonObject();
                updated.put("updated", 1);
                updated.put("keys", new JsonArray().add("last_login_datetime"));
                UpdateResult updateResult = new UpdateResult(updated);

                future.complete(updateResult);

            }

            @Mock
            public void merge(User user, Future<User> future) {
                future.complete(user);
            }
        };

        Response response = given()
                .body(makeLoginInfo().toString())
                .post("/api/otocloud-auth/user-management/users/actions/login");

        JsonObject bodyJson = new JsonObject(response.asString());

        String token = bodyJson.getString("access_token");
        String openid = bodyJson.getString("user_openid");

        Response updateResponse =
                given()
                        .pathParam("openId", openid)
                        .queryParam("token", token)
                        .body(new JsonObject().put("data", "put").toString()).
                        when()
                        .put("/api/otocloud-auth/user-management/users/{openId}");

        String responseStr = updateResponse.asString();
        JsonObject updateBody = new JsonObject(responseStr);
        String originOpenId = updateBody.getString("userOpenId");

        Assert.assertEquals(openid, originOpenId);

        mockUp.tearDown();

        async.complete();
    }

    @Test
    public void it_should_login_successfully(TestContext context) {
        final Async async = context.async();

        Response response = given().body(makeLoginInfo().toString()).post
                ("/api/otocloud-auth/user-management/users/actions/login");

        response.then().assertThat()
                .body("user_openid", CoreMatchers.notNullValue())
                .body("user_name", CoreMatchers.notNullValue())
                .body("access_token", CoreMatchers.notNullValue())
                .body("expires_in", CoreMatchers.notNullValue());

        JsonObject resp = new JsonObject(response.asString());

        Assert.assertTrue(resp.containsKey("roles"));

        async.complete();
    }

    @Test
    public void it_should_login_by_erp_successfully(TestContext context) {
        final Async async = context.async();

        GatewayMockUp gatewayMockUp = new GatewayMockUp();
        gatewayMockUp.mockUpLogin();

        JsonObject loginInfo = new JsonObject();
        loginInfo.put("user_name", "c1");
        loginInfo.put("password", "yonyou2");
        loginInfo.put("acct_id", 123);

        Response response = given().body(loginInfo.toString())
                .post("/api/otocloud-auth/user-management/users/actions/erp/login");

        logger.info(response.asString());

        response.then().assertThat()
                .body("user_name", CoreMatchers.notNullValue())
                .body("access_token", CoreMatchers.notNullValue())
                .body("expires_in", CoreMatchers.notNullValue());

        gatewayMockUp.tearDownLogin();

        async.complete();
    }

    @Test
    public void it_should_login_successfully_with_cellNo(TestContext context) {
        final Async async = context.async();

        given().body(makeLoginWithCellNoInfo().toString()).post
                ("/api/otocloud-auth/user-management/users/actions/login")
                .then().assertThat()
                .body("user_openid", CoreMatchers.notNullValue())
                .body("user_name", CoreMatchers.notNullValue())
                .body("access_token", CoreMatchers.notNullValue())
                .body("expires_in", CoreMatchers.notNullValue());

        async.complete();
    }

    @Test
    public void it_should_logout_successfully(TestContext context) {
        final Async async = context.async();


        Handler<HttpClientResponse> logoutHandler = response -> {
            response.bodyHandler(body -> {
                System.out.println("it_should_logout_successfully: " + body.toString());

                JsonObject reply = new JsonObject(body.toString());
                int errCode = reply.getInteger("errCode");
                context.assertEquals(0, errCode);

                async.complete();
            });
        };

        Handler<HttpClientResponse> loginHandler = response -> {
            response.bodyHandler(body -> {
                JsonObject reply = new JsonObject(body.toString());

                String userOpenId = reply.getString("user_openid");

                context.assertNotNull(userOpenId);

                HttpClientRequest logoutPost =
                        client.post("/api/otocloud-auth/user-management/users/actions/logout", logoutHandler);

                logoutPost.end(new JsonObject().put("userOpenId", userOpenId).toString());

            });
        };

        HttpClientRequest loginPost =
                client.post("/api/otocloud-auth/user-management/users/actions/login", loginHandler);
        loginPost.end(makeLoginInfo().toString());

    }


    @Test
    public void it_should_update_an_user_by_post(TestContext context) {
        final Async async = context.async();

        HttpClient client = vertx.createHttpClient(new HttpClientOptions()
                .setDefaultHost("localhost").setDefaultPort(8081));
        CountDownLatch loginLatch = new CountDownLatch(1);
        String[] userOpenId = new String[1];

//        String[] token = new String[1];
        //首先登陆，获得一个OpenID.
        HttpClientRequest post = client.post("/api/otocloud-auth/user-management/users/actions/login", response
                -> {
            response.bodyHandler(body -> {
                JsonObject reply = new JsonObject(body.toString());

                context.assertNotNull(reply.getString("user_openid"));

                userOpenId[0] = reply.getString("user_openid");
//                token[0] = reply.getString("access_token");
                loginLatch.countDown();
            });
        });
        post.end(makeLoginInfo().toString());

        try {
            loginLatch.await();
        } catch (InterruptedException e) {
            context.fail();
        }

        //修改自己的信息
        HttpClientRequest put = client.put("/api/otocloud-auth/user-management/users/" + userOpenId[0], response -> {
            response.bodyHandler(body -> {
                JsonObject reply = new JsonObject(body.toString());
                System.out.println(reply.toString());
                async.complete();
            });
        });
        put.end(makeUpdatedUserInfo().toString());
    }


    @Test
    public void it_should_verify_a_duplicated_cellNo(TestContext context) {
        String cellNo = "12345678901";

        final Async async = context.async();
        Response response = given().get("/api/otocloud-auth/user-management/users/verify?cellNo=" + cellNo);
        response.then().assertThat().body("exists", equalTo(true));

        async.complete();
    }

    private JsonObject makeUpdatedUserInfo() {
        JsonObject userInfo = new JsonObject();
        userInfo.put("name", "UpdatedName");
        userInfo.put("email", "UpdatedName@yonyou.com");
        return userInfo;
    }

    private String encryptPassword(String password) {
        return RSAUtil.encrypt(password);
    }

    private JsonObject makeLoginInfo() {
        JsonObject loginInfo = new JsonObject();
        loginInfo.put("userName", "zhangye");
        loginInfo.put("password", encryptPassword("*123"));
        return loginInfo;
    }

    private JsonObject makeLoginWithCellNoInfo() {
        JsonObject loginInfo = new JsonObject();
        loginInfo.put("cellNo", "12345678901");
        loginInfo.put("password", encryptPassword("*123"));
        return loginInfo;
    }

    private JsonObject makeAnUser() {
        JsonObject userInfo = new JsonObject()
                .put("name", "zhangye")
                .put("password", encryptPassword("*123")) //加密用户密码
                .put("org_acct_id", 123)
                .put("cell_no", UUID.randomUUID().toString().substring(0, 11)) //随机生成用于测试的手机.
                .put("email", "test@yonyou.com");
        return userInfo;
    }

}

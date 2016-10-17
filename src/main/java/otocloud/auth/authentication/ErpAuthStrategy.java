package otocloud.auth.authentication;

import com.google.inject.Inject;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import otocloud.auth.common.session.SessionSchema;
import otocloud.auth.mybatis.entity.AuthUser;
import otocloud.auth.service.UserService;
import otocloud.common.Command;

import java.util.UUID;

/**
 * ERP登录策略.
 * <p>
 * zhangyef@yonyou.com on 2015-12-24.
 */
public class ErpAuthStrategy implements AuthStrategy {
    protected static Logger logger = LoggerFactory.getLogger(ErpAuthStrategy.class);

    private final String result_code = "result_code";
    private final String ADAPTER_ADDRESS_LOGIN = "otocloud-app-common-uid-ad-login";
    private Vertx vertx;

    @Inject
    private UserService userService;

    @Inject
    public ErpAuthStrategy(Vertx vertx) {
        this.vertx = vertx;
    }

    /**
     * 输入参数的格式校验安排在Handler中, 此处不进行格式校验.
     * <p>
     * 登录过程:
     * 1. 到AuthUser数据库中查询用户是否存在;
     * 2. 使用Command在网关上执行登录命令.
     *
     * @param loginInfo   登录信息.
     * @param loginFuture 登录后的返回结构.
     */
    @Override
    public void login(JsonObject loginInfo, Future<JsonObject> loginFuture) {
        try {
            loginInExceptionWrapper(loginInfo, loginFuture);
        } catch (Exception e) {
            loginFuture.fail(e.getMessage());
        }
    }

    /**
     * TODO 从ERP系统中退出.
     *
     * @param logoutInfo
     * @param logoutFuture
     */
    @Override
    public void logout(JsonObject logoutInfo, Future<JsonObject> logoutFuture) {

    }

    private void loginInExceptionWrapper(JsonObject loginInfo, Future<JsonObject> loginFuture) {
        if (!loginInfo.containsKey("acct_id")) {
            JsonObject reply = new JsonObject();
            reply.put("errCode", 5);
            reply.put("errMsg", "登录信息中缺少企业账户字段[acct_id]");

            loginFuture.fail(reply.toString());
            return;
        }
        String user_name = loginInfo.getString("user_name");
        String password = loginInfo.getString("password");
        int acct_id = loginInfo.getInteger("acct_id");

        JsonObject session = loginInfo.getJsonObject(SessionSchema.SESSION);
        String sessionId = session.getString(SessionSchema.SESSION_ID);

        Future<AuthUser> boundFuture = Future.future();
        userService.beBoundWithErp(acct_id, user_name, boundFuture);

        boundFuture.setHandler(ret -> {
            if (ret.succeeded()) {
                AuthUser authUser = ret.result();
                loginErp(user_name, password, acct_id, sessionId, authUser, loginFuture);
                logger.info("[ERP] 登录中...");
                return;
            }

            //错误处理
            logger.warn("登录失败(用户未与ERP账户绑定).");
            loginFuture.fail(ret.cause().getMessage());

        });
    }

    private void loginErp(String user_name, String password,
                          int acct_id, String sessionId,
                          AuthUser authUser, Future<JsonObject> loginFuture) {

        String token = UUID.randomUUID().toString();

        Command command = new Command(acct_id, ADAPTER_ADDRESS_LOGIN);
        command.setParams(new JsonObject().put("user_code", user_name).put("password", password));

        JsonObject session = new JsonObject()
                .put(SessionSchema.SESSION_ID, sessionId)
                .put(SessionSchema.CURRENT_USER_ID, authUser.getId())
                .put(SessionSchema.ORG_ACCT_ID, authUser.getOrgAcctId());
        //在网关执行时需要设置Session.
        command.setSessions(session);

        Future<Void> stayOnlineFuture = Future.future();
        stayOnlineFuture.setHandler(ret -> {
            if (ret.failed()) {
                String errMsg = "登录失败: 用户已经通过认证, 但无法记录用户的在线状态. 可能的原因是\"数据库连接出错.\"";
                logger.warn(errMsg);
                loginFuture.fail(errMsg);
                return;
            }

            JsonObject loginSuccess = new JsonObject();
            loginSuccess.put("user_name", authUser.getErpUserCode());
            loginSuccess.put("access_token", token);
            loginSuccess.put("expires_in", 7200);
            loginSuccess.put("acct_id", authUser.getOrgAcctId());

            loginSuccess.put(SessionSchema.SESSION, session);

            logger.info("登录成功. 响应信息是 " + loginSuccess);
            loginFuture.complete(loginSuccess);

        });
        command.executeOnGateway(vertx, cmdRet -> {
            JsonObject reply = cmdRet.getData();
            if (cmdRet.succeeded() && reply.containsKey(result_code)) {
                int result_code = reply.getInteger(this.result_code);
                if (result_code == 0) {

                    JsonObject userOnLine = userService.makeUserOnlineShape(authUser, token, sessionId);
                    userService.stayOnlineInfo(userOnLine, stayOnlineFuture);

                    return;
                }
            }

        });
    }

}

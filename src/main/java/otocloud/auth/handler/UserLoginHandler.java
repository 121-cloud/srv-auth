package otocloud.auth.handler;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import io.vertx.core.Future;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import otocloud.auth.command.LoginCommand;
import otocloud.auth.common.exception.ErrCode;
import otocloud.auth.common.framework.CommandContext;
import otocloud.auth.common.util.BusMessageChecker;
import otocloud.auth.verticle.UserComponent;
import otocloud.common.ActionURI;
import otocloud.framework.core.HandlerDescriptor;
import otocloud.framework.core.OtoCloudBusMessage;
import otocloud.framework.core.OtoCloudComponentImpl;
import otocloud.framework.core.OtoCloudEventHandlerImpl;

/**
 * Created by zhangye on 2015-10-15.
 */
public class UserLoginHandler extends OtoCloudEventHandlerImpl<JsonObject> {

    protected Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    /**
     * 企业账号
     */
    public static final String ACCT_ID = "acctId";
    public static final String USER_ID = "userId";

    @Inject
    private LoginCommand loginCommand;
    private static final String ACTION_URL = "users/actions/login";

    @Inject
    public UserLoginHandler(@Named("UserComponent") OtoCloudComponentImpl componentImpl) {
        super(componentImpl);
    }


    public static String getActionUrl(){
        return ACTION_URL;
    }
    /**
     * post /api/"服务名"/"组件名"/users/actions/login
     *
     * @return "users/actions/login"
     */
    @Override
    public HandlerDescriptor getHanlderDesc() {
        HandlerDescriptor handlerDescriptor = super.getHanlderDesc();
        ActionURI uri = new ActionURI(ACTION_URL, HttpMethod.POST);
        handlerDescriptor.setRestApiURI(uri);
        return handlerDescriptor;
    }

    /**
     * "服务名".user-management.users.login
     *
     *  serviceName.componentName.getEventAddress()
     *
     * @return "user-management.users.login"
     * @see otocloud.auth.verticle.UserComponent#MANAGE_USER_ADDRESS
     */
    @Override
    public String getEventAddress() {
        return UserComponent.MANAGE_USER_ADDRESS + ".login";
    }

    @Override
    public void handle(OtoCloudBusMessage<JsonObject> msg) {
        if(logger.isInfoEnabled()){
            logger.info("接收到用户登录请求,开始验证用户登录信息格式.");
        }

        boolean isLegal = BusMessageChecker.checkLoginInfo(msg.body(), errMsg -> {
            msg.fail(ErrCode.BUS_MSG_FORMAT_ERR.getCode(), errMsg);
        });

        if (!isLegal) {
            return;
        }

        if(logger.isInfoEnabled()){
            logger.info("用户登录信息格式正确,开始登录.");
        }

        JsonObject loginInfo = msg.body();
        if(logger.isDebugEnabled()){
            logger.debug("登录信息是: " + loginInfo);
        }

        CommandContext context = new CommandContext();
        JsonObject data = loginInfo.getJsonObject("content");        
        context.put("data", data);
        context.put("sessionId", loginInfo.getJsonObject("session").getString("id"));
        Future<JsonObject> future = Future.future();
        loginCommand.executeFuture(context, future);

        future.setHandler(ret -> {
            if (ret.succeeded()) {
                JsonObject reply = ret.result();

                //将企业账号取出，放入Session中
                int acctId = reply.getInteger(ACCT_ID);
                reply.remove(ACCT_ID);

                //取出用户ID，放入Session中。
                int userId = reply.getInteger(USER_ID);
                reply.remove(USER_ID);

                JsonObject session = loginInfo.getJsonObject("session");
                if(session == null)
                	session = new JsonObject();
                session.put(ACCT_ID, acctId);
                session.put(USER_ID, userId);

                reply.put("session", session);                
                

                if(logger.isInfoEnabled()){
                    logger.info("返回用户登录结果.");
                }

                msg.reply(ret.result());
            } else {
                msg.fail(ErrCode.INVALID_USERNAME_OR_PASSWORD.getCode(), ret.cause().getMessage());
            }
        });
    }
}

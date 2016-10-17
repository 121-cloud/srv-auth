package otocloud.auth.handler;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import io.vertx.core.Future;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import otocloud.auth.authentication.ErpAuthStrategy;
import otocloud.auth.authentication.LoginContext;
import otocloud.auth.common.session.SessionSchema;
import otocloud.auth.verticle.UserComponent;
import otocloud.common.ActionURI;
import otocloud.framework.core.HandlerDescriptor;
import otocloud.framework.core.OtoCloudBusMessage;
import otocloud.framework.core.OtoCloudComponentImpl;
import otocloud.framework.core.OtoCloudEventHandlerImpl;

/**
 * 通过ERP用户名和密码登录.
 * <p>
 * 1. 根据企业账户和用户名到121系统中查询用户是否存在.
 * 2. 如果用户名存在,则将用户名和密码转发到对应企业账户的网管上,实施登录.
 * <p>
 * zhangyef@yonyou.com on 2015-12-23.
 */
//@LazySingleton
public class ErpUserLoginHandler extends OtoCloudEventHandlerImpl<JsonObject> {

    protected Logger logger = LoggerFactory.getLogger(ErpUserLoginHandler.class);

    /**
     * ERP登录的URL.
     */
    private static final String ACTION_URL = "users/actions/erp/login";

//    @Inject
//    private Vertx vertx;

    @Inject
    private ErpAuthStrategy erpLoginStrategy;

    @Inject
    public ErpUserLoginHandler(@Named("UserComponent") OtoCloudComponentImpl componentImpl) {
        super(componentImpl);
    }

    @Override
    public void handle(OtoCloudBusMessage<JsonObject> msg) {
        logger.info("接收到用户 [ERP] 登录请求,开始验证用户登录信息格式.");

        //todo 检验消息格式
        JsonObject body = msg.body();
        JsonObject loginInfo = body.getJsonObject("content");
        JsonObject session = body.getJsonObject(SessionSchema.SESSION);

        loginInfo.put(SessionSchema.SESSION, session);

        Future<JsonObject> loginFuture = Future.future();

        LoginContext loginContext = new LoginContext();
        loginContext.setLoginInfo(loginInfo);
        loginContext.setStrategy(erpLoginStrategy);

        loginContext.login(loginFuture);

        loginFuture.setHandler(ret -> {
            if(ret.succeeded()){
                msg.reply(ret.result());
            }else{
                msg.fail(1, ret.cause().getMessage());
            }
        });
    }

    public static String getActionUrl() {
        return ACTION_URL;
    }

    /**
     * <服务名>.user-management.users.erp.login
     *
     * @return
     */
    @Override
    public String getEventAddress() {
        return UserComponent.MANAGE_USER_ADDRESS + ".erp.login";
    }

    /**
     * post /api/"服务名"/"组件名"/users/actions/erp/login
     *
     * @return /api/otocloud-auth/user-management/users/actions/erp/login
     */
    @Override
    public HandlerDescriptor getHanlderDesc() {
        HandlerDescriptor handlerDescriptor = super.getHanlderDesc();
        ActionURI uri = new ActionURI(ACTION_URL, HttpMethod.POST);
        handlerDescriptor.setRestApiURI(uri);
        return handlerDescriptor;
    }
}

package otocloud.auth.handler;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import io.vertx.core.Future;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import otocloud.auth.common.MessageFinder;
import otocloud.auth.handler.messagechecker.UserCreationChecker;
import otocloud.auth.service.UserService;
import otocloud.common.ActionURI;
import otocloud.framework.core.HandlerDescriptor;
import otocloud.framework.core.OtoCloudBusMessage;
import otocloud.framework.core.OtoCloudComponentImpl;
import otocloud.framework.core.OtoCloudEventHandlerImpl;

/**
 * 当管理员手动新增用户时调用.
 * zhangyef@yonyou.com on 2016-01-21.
 */

public class UserCreationHandler extends OtoCloudEventHandlerImpl<JsonObject> {
    protected Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    private UserCreationChecker checker = new UserCreationChecker();

    @Inject
    private UserService userService;

    @Inject
    public UserCreationHandler(@Named("UserComponent") OtoCloudComponentImpl component) {
        super(component);
    }

    @Override
    public void handle(OtoCloudBusMessage<JsonObject> msg) {
        logger.info("接收到 [新增业务员] 请求, 开始创建 [业务员] 用户.");

        MessageFinder finder = new MessageFinder(msg);

        int userId = finder.getUserId();
        int acctId = finder.getAcctId();

        JsonObject content = finder.getContent();

        if (!checker.check(content)) {
            msg.fail(-1, checker.getErrMessage() + ". 正确格式如下: " + checker.getFormat().toString());
            return;
        }

        String userName = content.getString("user_name");
        String cellNo = content.getString("cell_no");
        String email = content.getString("email");


        Future<JsonObject> createFuture = Future.future();
        userService.createUser(acctId, userId, userName, cellNo, email, createFuture);
        createFuture.setHandler(ret -> {
            if (ret.failed()) {
                logger.warn(ret.cause().getMessage());
                msg.fail(1, "无法新增该用户,请检查 \"用户名称/手机号码/邮箱\" 是否已经注册.");
                return;
            }
            JsonObject reply = content.copy();
            msg.reply(reply);

            sendEmail(ret.result());
        });
    }

    /**
     * 发送邮件通知.
     * 邮件服务的总线地址: user-manager.users.activation.email
     *
     * @param activationInfo 用户激活信息.
     */
    private void sendEmail(JsonObject activationInfo) {
        //TODO 发送邮件事件
    }

    /**
     * 服务名.组件名.users.operators.post
     *
     * @return otocloud-auth.user-management.users.operators.post
     */
    @Override
    public String getEventAddress() {
        return "users.operators.post";
    }

    /**
     * post /api/"服务名"/"组件名"/api/fun
     *
     * @return /api/otocloud-auth/user-management/users/operators
     */
    @Override
    public HandlerDescriptor getHanlderDesc() {
        HandlerDescriptor handlerDescriptor = super.getHanlderDesc();
        ActionURI uri = new ActionURI("users/operators", HttpMethod.POST);
        handlerDescriptor.setRestApiURI(uri);
        return handlerDescriptor;
    }
}
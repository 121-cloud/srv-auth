package otocloud.auth.handler;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import io.vertx.core.Future;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import otocloud.auth.command.LogoutCommand;
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
 * TODO 退出时,向网关发送消息,告知用户将退出121, 请登出ERP系统.
 * Created by zhangye on 2015-10-18.
 */
public class UserLogoutHandler extends OtoCloudEventHandlerImpl<JsonObject> {

    @Inject
    private LogoutCommand logoutCommand;

    @Inject
    public UserLogoutHandler(@Named("UserComponent") OtoCloudComponentImpl componentImpl) {
        super(componentImpl);
    }

    @Override
    public void handle(OtoCloudBusMessage<JsonObject> msg) {
        boolean isLegal = BusMessageChecker.checkLoginInfo(msg.body(), errMsg -> {
            msg.fail(ErrCode.BUS_MSG_FORMAT_ERR.getCode(), errMsg);
        });

        if (!isLegal) {
            return;
        }

        JsonObject loginInfo = msg.body();
        CommandContext context = new CommandContext();
        context.put("data", loginInfo.getJsonObject("content"));
        Future<JsonObject> future = Future.future();
        logoutCommand.executeFuture(context, future);

        future.setHandler(ret -> {
            if (ret.succeeded()) {
                msg.reply(ret.result());
            } else {
                msg.fail(1, ret.cause().getMessage());
            }
        });
    }

    /**
     * 事件总线上注册的最终地址是："服务名".user-management.users.logout
     *
     * @return "user-management.users.logout"
     * @see otocloud.auth.verticle.UserComponent#MANAGE_USER_ADDRESS
     */
    @Override
    public String getEventAddress() {
        return UserComponent.MANAGE_USER_ADDRESS + ".logout";
    }

    /**
     * post /api/"服务名"/"组件名"/users/actions/logout
     *
     * @return "users/actions/login"
     */
    @Override
    public HandlerDescriptor getHanlderDesc() {
        HandlerDescriptor handlerDescriptor = super.getHanlderDesc();
        ActionURI uri = new ActionURI("users/actions/logout", HttpMethod.POST);
        handlerDescriptor.setRestApiURI(uri);
        return handlerDescriptor;
    }
}

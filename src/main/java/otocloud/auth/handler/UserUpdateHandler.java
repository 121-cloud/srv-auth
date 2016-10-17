package otocloud.auth.handler;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import io.vertx.core.Future;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import otocloud.auth.command.UpdateUserCommand;
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
 * Created by zhangye on 2015-10-20.
 */

public class UserUpdateHandler extends OtoCloudEventHandlerImpl<JsonObject> {

    @Inject
    private UpdateUserCommand updateUserCommand;

    @Inject
    private BusMessageChecker busMessageChecker;

    @Inject
    public UserUpdateHandler(@Named("UserComponent") OtoCloudComponentImpl componentImpl) {
        super(componentImpl);
    }

    @Override
    public void handle(OtoCloudBusMessage<JsonObject> msg) {
        boolean isLegal = busMessageChecker.checkUpdateUserInfo(msg.body(), errMsg -> {
            msg.fail(ErrCode.BUS_MSG_FORMAT_ERR.getCode(), errMsg);
        });

        if (!isLegal) {
            return;
        }

        JsonObject body = msg.body();

        JsonObject queryParams = body.getJsonObject("queryParams");
        String openid = queryParams.getString("openId");

        JsonObject content = body.getJsonObject("content");
        content.put("openId", openid);

        CommandContext context = new CommandContext();
        context.put("data", content);

        Future<JsonObject> future = Future.future();
        updateUserCommand.executeFuture(context, future);

        future.setHandler(ret -> {
            if (ret.succeeded()) {
                msg.reply(ret.result());
            } else {
                msg.fail(ErrCode.FAIL.getCode(), ret.cause().getMessage());
            }
        });
    }

    /**
     * 得到对应事件总线的地址。
     *
     * @return "服务名"."组件名"."具体地址"，即 "服务名".user-management.users.put
     */
    @Override
    public String getEventAddress() {
        return UserComponent.MANAGE_USER_ADDRESS + ".put";
    }

    /**
     * 注册REST API。
     *
     * @return put /api/"服务名"/"组件名"/users/:openId
     */
    @Override
    public HandlerDescriptor getHanlderDesc() {
        HandlerDescriptor handlerDescriptor = super.getHanlderDesc();
        ActionURI uri = new ActionURI("users/:openId", HttpMethod.PUT);
        handlerDescriptor.setRestApiURI(uri);
        return handlerDescriptor;
    }
}

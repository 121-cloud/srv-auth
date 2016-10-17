package otocloud.auth.handler;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import io.vertx.core.Future;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import otocloud.auth.command.DeleteUserCommand;
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
 * Created by zhangye on 2015-10-27.
 */
public class UserDeleteHandler extends OtoCloudEventHandlerImpl<JsonObject> {

    @Inject
    private DeleteUserCommand deleteUserCommand;

    @Inject
    public UserDeleteHandler(@Named("UserComponent") OtoCloudComponentImpl componentImpl) {
        super(componentImpl);
    }

    @Override
    public void handle(OtoCloudBusMessage<JsonObject> msg) {
        boolean isLegal = BusMessageChecker.checkDeleteUserInfo(msg.body(), errMsg -> {
            msg.fail(ErrCode.BUS_MSG_FORMAT_ERR.getCode(), errMsg);
        });

        if (!isLegal) {
            return;
        }

        JsonObject body = msg.body();

        JsonObject queryParams = body.getJsonObject("queryParams");
        String openid = queryParams.getString(DeleteUserCommand.USER_ID);

        CommandContext context = new CommandContext();
        context.put("data", new JsonObject().put(DeleteUserCommand.USER_ID, Integer.valueOf(openid)));

        Future<JsonObject> future = Future.future();
        deleteUserCommand.executeFuture(context, future);

        future.setHandler(ret -> {
            if (ret.succeeded()) {
                msg.reply(ret.result());
            } else {
                msg.fail(ErrCode.FAIL.getCode(), ret.cause().getMessage());
            }
        });
    }

    /**
     * 最终的REST API是：put /api/"服务名"/"组件名"/users/:openId
     *
     * @return
     */
    @Override
    public HandlerDescriptor getHanlderDesc() {
        HandlerDescriptor handlerDescriptor = super.getHanlderDesc();
        ActionURI uri = new ActionURI("users/:" + DeleteUserCommand.USER_ID, HttpMethod.DELETE);
        handlerDescriptor.setRestApiURI(uri);
        return handlerDescriptor;
    }

    /**
     * 事件总线上注册的最终地址是："服务名"."组件名".users.delete
     * @return
     */
    @Override
    public String getEventAddress() {
        return UserComponent.MANAGE_USER_ADDRESS + ".delete";
    }
}

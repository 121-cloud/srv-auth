package otocloud.auth.handler;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import io.vertx.core.Future;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import otocloud.auth.command.AddUserCommand;
import otocloud.auth.common.exception.ErrCode;
import otocloud.auth.common.framework.CommandContext;
import otocloud.auth.common.session.SessionSchema;
import otocloud.auth.common.util.BusMessageChecker;
import otocloud.auth.verticle.UserComponent;
import otocloud.common.ActionURI;
import otocloud.framework.core.HandlerDescriptor;
import otocloud.framework.core.OtoCloudBusMessage;
import otocloud.framework.core.OtoCloudComponentImpl;
import otocloud.framework.core.OtoCloudEventHandlerImpl;

/**
 * 向系统中添加一个管理员用户.
 * Created by zhangye on 2015-10-14.
 */
public class UserRegisterHandler extends OtoCloudEventHandlerImpl<JsonObject> {

    @Inject
    private AddUserCommand addUserCommand;

    @Inject
    private BusMessageChecker busMessageChecker;

    @Inject
    public UserRegisterHandler(@Named("UserComponent") OtoCloudComponentImpl componentImpl) {
        super(componentImpl);
    }

    /**
     * 注册REST API。
     *
     * @return post /api/"服务名"/"组件名"/users
     */
    @Override
    public HandlerDescriptor getHanlderDesc() {
        HandlerDescriptor handlerDescriptor = super.getHanlderDesc();
        ActionURI uri = new ActionURI("users", HttpMethod.POST);
        handlerDescriptor.setRestApiURI(uri);
        return handlerDescriptor;
    }

    private JsonObject convertUserInfo(JsonObject userInfo) {
        JsonObject user = new JsonObject();
        user.put("userName", userInfo.getString("name"));
        user.put("password", userInfo.getString("password"));
        user.put("orgAcctId", userInfo.getInteger("org_acct_id")); //处理整数或字符串.
        user.put("cellNo", userInfo.getString("cell_no"));
        user.put("email", userInfo.getString("email"));
        return user;
    }

    /**
     * 得到对应事件总线的地址。
     *
     * @return <服务名>.user-management.users.post
     */
    @Override
    public String getEventAddress() {
        return UserComponent.MANAGE_USER_ADDRESS + ".post";
    }

    @Override
    public void handle(OtoCloudBusMessage<JsonObject> msg) {
        boolean isLegal = busMessageChecker.checkCreateUserInfo(msg.body(), errMsg -> {
            msg.fail(ErrCode.BUS_MSG_FORMAT_ERR.getCode(), errMsg);
        });

        if (!isLegal) {
            return;
        }

        JsonObject body = msg.body();
        JsonObject session = body.getJsonObject(SessionSchema.SESSION, null);

        JsonObject userInfo = convertUserInfo(body.getJsonObject("content"));
        //从Session中取出当前登录的用户ID
        int currentUser = 0; //默认为0，表示没有用户.
        if (session != null) {
            currentUser = session.getInteger(SessionSchema.CURRENT_USER_ID, 0);
        }
        userInfo.put("entryId", currentUser);

        CommandContext context = new CommandContext();
        context.put("data", userInfo);

        Future<JsonObject> future = Future.future();
        addUserCommand.executeFuture(context, future);

        future.setHandler(ret -> {
            if (ret.succeeded()) {
                msg.reply(ret.result());
            } else {
                msg.fail(1, ret.cause().getMessage());
            }
        });
    }
}

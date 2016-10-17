package otocloud.auth.command;

import com.google.inject.Inject;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import otocloud.auth.common.framework.CommandContext;
import otocloud.auth.common.framework.DefaultOtoCommand;
import otocloud.auth.common.framework.IContext;
import otocloud.auth.entity.ReplyMessage;
import otocloud.auth.entity.User;
import otocloud.auth.service.UserService;

/**
 * Created by zhangye on 2015-10-09.
 */
//@LazySingleton
public class DeleteUserCommand extends DefaultOtoCommand<JsonObject> {

    public static final String USER_ID = "userId";

    @Inject
    private UserService userService;

    /**
     * 从上下文中获得userInfo对象，该对象中保存了将要删除的用户的OpenID。
     *
     * @param context
     * @param resultHandler
     */
    @Override
    public void execute(IContext context, Handler resultHandler) {
        JsonObject userInfo = (JsonObject) context.get("userInfo");
        String openId = userInfo.getString(USER_ID);

        User user = userService.deleteByOpenId(openId);
        resultHandler.handle(user);
    }

    @Override
    public void executeFuture(CommandContext context, Future<JsonObject> future) {
        JsonObject userInfo = (JsonObject) context.get("data");
        int openId = userInfo.getInteger(USER_ID);

        Future<User> innerFuture = Future.future();

        userService.deleteById(openId, innerFuture);

        innerFuture.setHandler(ret -> {
            if (ret.succeeded()) {
                future.complete(ReplyMessage.success().getMessage());
            } else {
                future.complete(ReplyMessage.failure().getMessage());
            }
        });


    }
}

package otocloud.auth.command;

import com.google.inject.Inject;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import otocloud.auth.common.framework.CommandContext;
import otocloud.auth.common.framework.DefaultOtoCommand;
import otocloud.auth.common.framework.IContext;
import otocloud.auth.common.util.Mapper;
import otocloud.auth.entity.User;
import otocloud.auth.service.UserService;

/**
 * Created by zhangye on 2015-10-09.
 */
//@LazySingleton
public class UpdateUserCommand extends DefaultOtoCommand<JsonObject> {

    @Inject
    private UserService userService;

    @Override
    public void execute(IContext context, Handler resultHandler) {
        JsonObject userInfo = (JsonObject) context.get("data");
        User user = userService.update(userInfo);

        resultHandler.handle(user);
    }

    @Override
    public void executeFuture(CommandContext context, Future<JsonObject> future) {
        JsonObject userInfo = context.<JsonObject>getValue("data");
        if (userInfo.isEmpty()) {
            future.complete(new JsonObject().put("errCode", 0).put("errMsg", "没有数据被更新."));
            return;
        }
        String openId = userInfo.getString("openId");

        User userWithId;
        Future<User> innerFuture = Future.future();

        try {
            userWithId = userService.findByOpenId(openId);

            User userNeedId = Mapper.mapToEntity(userInfo, User.class);
            userNeedId.setID(userWithId.getID());


            userService.update(userNeedId, innerFuture);
        } catch (Exception e) {
            future.complete(new JsonObject().put("userOpenId", openId));
        }


        innerFuture.setHandler(result -> {
            JsonObject reply = new JsonObject();
            if (result.succeeded()) {
                User user = result.result();

                reply.put("userOpenId", user.getOpenID());
                reply.put("userName", user.getUserName());

                future.complete(reply);
            } else future.fail(result.cause());
        });
    }
}

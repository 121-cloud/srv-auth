package otocloud.auth.command;

import com.google.inject.Inject;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import otocloud.auth.common.framework.CommandContext;
import otocloud.auth.common.framework.DefaultOtoCommand;
import otocloud.auth.common.framework.IContext;
import otocloud.auth.entity.User;
import otocloud.auth.service.UserService;

/**
 * Created by zhangye on 2015-10-09.
 */
//@LazySingleton
public class QueryUserCommand extends DefaultOtoCommand {
    @Inject
    private UserService userService;

    @Override
    public void execute(IContext context, Handler resultHandler) {
        JsonObject userInfo = (JsonObject)context.get("userInfo");
        String openId = userInfo.getString("openId");

        User user = userService.findByOpenId(openId);

        resultHandler.handle(user);
    }

    @Override
    public void executeFuture(CommandContext context, Future future) {

    }
}

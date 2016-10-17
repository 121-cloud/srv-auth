package otocloud.auth.command;

import com.google.inject.Inject;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import otocloud.auth.common.framework.CommandContext;
import otocloud.auth.common.framework.DefaultOtoCommand;
import otocloud.auth.common.framework.IContext;
import otocloud.auth.common.validator.ViolationMessageBuilder;
import otocloud.auth.service.UserService;

import javax.validation.ConstraintViolationException;

/**
 * Created by zhangye on 2015-10-10.
 */
//@LazySingleton
public class LogoutCommand extends DefaultOtoCommand {

    @Inject
    private UserService userService;

    @Override
    public void execute(IContext context, Handler resultHandler) {

    }

    @Override
    public void executeFuture(CommandContext context, Future future) {
        JsonObject data = context.getJson("data");

        String userOpenId = data.getString("userOpenId");

        try {
            userService.logout(userOpenId, future);
        }catch(ConstraintViolationException e){
            String errMsg = ViolationMessageBuilder.build(e);
            future.fail(errMsg);
        }
    }
}

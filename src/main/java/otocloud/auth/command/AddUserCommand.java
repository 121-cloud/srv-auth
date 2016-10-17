package otocloud.auth.command;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import otocloud.auth.common.framework.CommandContext;
import otocloud.auth.common.framework.DefaultOtoCommand;
import otocloud.auth.entity.User;
import otocloud.auth.service.UserService;

import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by zhangye on 2015-10-09.
 */
//@LazySingleton
public class AddUserCommand extends DefaultOtoCommand<JsonObject> {

    protected Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    @Inject
    private UserService userService;

    @Override
    public void executeFuture(CommandContext context, Future<JsonObject> future) {

        JsonObject userInfo = (JsonObject) context.get("data");
        final User[] user = {null};
        JsonObject result = new JsonObject();

        try {
            Future<User> innfuture = Future.future();
            userService.create(userInfo, innfuture);
            innfuture.setHandler(ret -> {
                if (ret.succeeded()) {
                    user[0] = ret.result();

                    result.put("errCode", 0);
                    result.put("errMsg", "");
                    result.put("data", new JsonObject().put("userId", user[0].getID()));
                    future.complete(result);
                } else {
//                    result.put("errCode", 1);
//                    result.put("errMsg", ret.cause().getMessage());
                    future.fail(ret.cause().getMessage());
                }
            });
        } catch (ConstraintViolationException e) {
            JsonArray violationMsg = new JsonArray();
            Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
            if (logger.isInfoEnabled()) {
                Iterator<ConstraintViolation<?>> itr = violations.iterator();
                while (itr.hasNext()) {
                    ConstraintViolation<?> violation = itr.next();
                    violationMsg.add(violation.getMessage());
                    logger.info(violation.getMessage());
                }
            }
            result.put("errCode", 1);
            result.put("errMsg", violationMsg.toString());
            future.fail(result.toString());
        }

    }


}

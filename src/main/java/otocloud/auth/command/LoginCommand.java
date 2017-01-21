package otocloud.auth.command;

import com.google.inject.Inject;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import org.apache.commons.lang3.StringUtils;
import otocloud.auth.common.framework.CommandContext;
import otocloud.auth.common.framework.DefaultOtoCommand;
import otocloud.auth.common.validator.ViolationMessageBuilder;
import otocloud.auth.service.UserService;

import javax.validation.ConstraintViolationException;

/**
 * Created by zhangye on 2015-10-09.
 */
//@LazySingleton
public class LoginCommand extends DefaultOtoCommand {
    protected static final Logger logger = LoggerFactory.getLogger(LoginCommand.class);

    @Inject
    private UserService userService;


    @Override
    public void executeFuture(CommandContext context, Future future) {
        JsonObject data = context.getJson("data");
        String userName = data.getString("userName");
        String cellNo = data.getString("cellNo");
        String password = data.getString("password");

        String sessionId = context.get("sessionId").toString();

        logger.info("AuthService - 登录时的SessionID是: " + sessionId);       
        

        try {
            if (StringUtils.isBlank(userName)) {
                logger.info("使用 [手机号,密码] 方式登录.");
                userService.loginWithCellNo(cellNo, password, sessionId, future);
            } else {
                logger.info("使用 [用户名,密码] 方式登录.");
                userService.login(userName, password, false, sessionId, future);
            }

        } catch (ConstraintViolationException e) {
            String errMsg = ViolationMessageBuilder.build(e);
            future.fail(errMsg);
        } catch (Exception ex) {
        	ex.printStackTrace();
            String errMsg = ex.getMessage();
            future.fail(errMsg);
        }
    }
}

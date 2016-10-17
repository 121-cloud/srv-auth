package otocloud.auth.handler;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import otocloud.auth.service.UserService;
import otocloud.framework.core.OtoCloudBusMessage;
import otocloud.framework.core.OtoCloudComponentImpl;
import otocloud.framework.core.OtoCloudEventHandlerImpl;

/**
 * 查询用户的授权相关信息.
 * 只提供事件总线API,不提供RestAPI.
 * 例如,根据token查询SessionId.
 *
 * zhangyef@yonyou.com on 2015-11-24.
 */
public class TokenQueryHandler extends OtoCloudEventHandlerImpl<JsonObject> {
    protected static final Logger logger = LoggerFactory.getLogger(TokenQueryHandler.class);

    @Inject
    private UserService userService;

    @Inject
    public TokenQueryHandler(@Named("UserComponent") OtoCloudComponentImpl componentImpl) {
        super(componentImpl);
    }

    @Override
    public void handle(OtoCloudBusMessage<JsonObject> msg) {
        logger.info("AuthService - 收到信息查询请求.");

        JsonObject body = msg.body();

        JsonObject condition = body.getJsonObject("condition");
        String token = condition.getString("token");

        logger.info("AuthService - 根据token查询SessionID, token:\"" + token + "\"");
        Future<String> sessionFuture = Future.future();

        userService.findSessionId(token, sessionFuture);

        sessionFuture.setHandler( ret -> {
            if(ret.failed()){
                msg.reply(new JsonObject().put("errCode", 1).put("errMsg", "无法找到SessionID."));
                return;
            }

            String sessionId = ret.result();

            logger.info("AuthService - 查询到的SessionID是: " + sessionId);

            JsonObject reply = new JsonObject();
            reply.put("data", new JsonObject().put("session_id", sessionId));

            msg.reply(reply);
        });


    }

    /**
     * "服务名".user-management.query
     * @return
     */
    @Override
    public String getEventAddress() {
        return "query";
    }
}

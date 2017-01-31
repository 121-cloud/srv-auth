package otocloud.auth.user;

import io.vertx.core.json.JsonObject;
import otocloud.auth.common.UserOnlineSchema;
import otocloud.auth.AuthService;
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
	
	private String USERS_ONLINE = "UsersOnline";
	
    public TokenQueryHandler(OtoCloudComponentImpl componentImpl) {
        super(componentImpl);
    }

    @Override
    public void handle(OtoCloudBusMessage<JsonObject> msg) {

        JsonObject body = msg.body();

        JsonObject condition = body.getJsonObject("condition");
        String token = condition.getString("token");

        this.componentImpl.getLogger().info("AuthService - 根据token查询SessionID, token:\"" + token + "\"");

        JsonObject query = new JsonObject();
        query.put(UserOnlineSchema.TOKEN, token);
        
        AuthService authService = (AuthService)this.componentImpl.getService();
        authService.getAuthSrvMongoDataSource().getMongoClient().findOne(USERS_ONLINE, query, new JsonObject(), ret -> {
            if (ret.succeeded()) {
                JsonObject found = ret.result();

                String sessionId = found.getString(UserOnlineSchema.SESSION_ID);
                this.componentImpl.getLogger().info("查找到的SessionID: " + sessionId);

                JsonObject reply = new JsonObject();
                reply.put("data", new JsonObject().put("session_id", sessionId));

                msg.reply(reply);
            } else {
				Throwable errThrowable = ret.cause();
				String errMsgString = errThrowable.getMessage();
				this.componentImpl.getLogger().error("无法根据Token找到SessionID." + errMsgString, errThrowable);
			
            	msg.fail(400, "无法根据Token找到SessionID. 可能的原因是: 用户已经退出.");
            }
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

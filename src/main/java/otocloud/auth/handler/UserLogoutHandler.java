package otocloud.auth.handler;

import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import otocloud.auth.verticle.AuthService;
import otocloud.auth.verticle.UserComponent;
import otocloud.common.ActionURI;
import otocloud.framework.core.HandlerDescriptor;
import otocloud.framework.core.OtoCloudBusMessage;
import otocloud.framework.core.OtoCloudComponentImpl;
import otocloud.framework.core.OtoCloudEventHandlerImpl;

/**
 * TODO 退出时,向网关发送消息,告知用户将退出121, 请登出ERP系统.
 * Created by zhangye on 2015-10-18.
 */
public class UserLogoutHandler extends OtoCloudEventHandlerImpl<JsonObject> {

    
    public UserLogoutHandler(OtoCloudComponentImpl componentImpl) {
        super(componentImpl);
    }

    @Override
    public void handle(OtoCloudBusMessage<JsonObject> msg) {
    	
    	JsonObject loginInfo = msg.body();         
        JsonObject session = loginInfo.getJsonObject("session");
		Long userId = session.getLong("user_id");
       
		JsonObject query = new JsonObject().put("user_id", userId);
        
        AuthService authService = (AuthService)this.componentImpl.getService();
        authService.getAuthSrvMongoDataSource().getMongoClient().removeDocument("UsersOnline", query, resultHandler->{
            if(resultHandler.failed()){                                    
				Throwable errThrowable = resultHandler.cause();
				String errMsgString = errThrowable.getMessage();
				this.componentImpl.getLogger().error(errMsgString, errThrowable);
				msg.fail(100, errMsgString);
            }else{
            	msg.reply(resultHandler.result().toJson());
            }
        });

    }
    

    /**

    /**
     * 事件总线上注册的最终地址是："服务名".user-management.users.logout
     *
     * @return "user-management.users.logout"
     * @see otocloud.auth.verticle.UserComponent#MANAGE_USER_ADDRESS
     */
    @Override
    public String getEventAddress() {
        return UserComponent.MANAGE_USER_ADDRESS + ".logout";
    }

    /**
     * post /api/"服务名"/"组件名"/users/actions/logout
     *
     * @return "users/actions/login"
     */
    @Override
    public HandlerDescriptor getHanlderDesc() {
        HandlerDescriptor handlerDescriptor = super.getHanlderDesc();
        ActionURI uri = new ActionURI("users/actions/logout", HttpMethod.POST);
        handlerDescriptor.setRestApiURI(uri);
        return handlerDescriptor;
    }
}

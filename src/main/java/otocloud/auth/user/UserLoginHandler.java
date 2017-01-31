package otocloud.auth.user;

import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import otocloud.auth.AuthService;
import otocloud.auth.user.UserComponent;
import otocloud.common.ActionURI;
import otocloud.framework.core.HandlerDescriptor;
import otocloud.framework.core.OtoCloudBusMessage;
import otocloud.framework.core.OtoCloudComponentImpl;
import otocloud.framework.core.OtoCloudEventHandlerImpl;

/**
 * Created by zhangye on 2015-10-15.
 */
public class UserLoginHandler extends OtoCloudEventHandlerImpl<JsonObject> {

    private static final String ACTION_URL = "users/actions/login";
    
    public UserLoginHandler(OtoCloudComponentImpl componentImpl) {
        super(componentImpl);
    }


    public static String getActionUrl(){
        return ACTION_URL;
    }
    /**
     * post /api/"服务名"/"组件名"/users/actions/login
     *
     * @return "users/actions/login"
     */
    @Override
    public HandlerDescriptor getHanlderDesc() {
        HandlerDescriptor handlerDescriptor = super.getHanlderDesc();
        ActionURI uri = new ActionURI(ACTION_URL, HttpMethod.POST);
        handlerDescriptor.setRestApiURI(uri);
        return handlerDescriptor;
    }

    /**
     * "服务名".user-management.users.login
     *
     *  serviceName.componentName.getEventAddress()
     *
     * @return "user-management.users.login"
     * @see otocloud.auth.verticle.UserComponent#MANAGE_USER_ADDRESS
     */
    @Override
    public String getEventAddress() {
        return UserComponent.MANAGE_USER_ADDRESS + ".login";
    }
    
    
    @Override
    public void handle(OtoCloudBusMessage<JsonObject> msg) {
     	
    	JsonObject loginInfo = msg.body();    	
        JsonObject data = loginInfo.getJsonObject("content");        
        JsonObject session = loginInfo.getJsonObject("session");
		Long userId = session.getLong("user_id");
		Long acctId = data.getLong("acct_id");       
       
		JsonObject query = new JsonObject().put("user_id", userId);
		JsonObject update = new JsonObject().put("$set", new JsonObject().put("acct_id", acctId));
        
        AuthService authService = (AuthService)this.componentImpl.getService();
        authService.getAuthSrvMongoDataSource().getMongoClient().updateCollection("UsersOnline", query, update, resultHandler->{
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
    

    
    
}

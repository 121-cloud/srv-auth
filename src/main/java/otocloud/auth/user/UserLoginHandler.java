package otocloud.auth.user;

import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import otocloud.common.ActionURI;
import otocloud.framework.common.IgnoreAuthVerify;
import otocloud.framework.core.HandlerDescriptor;
import otocloud.framework.core.OtoCloudBusMessage;
import otocloud.framework.core.OtoCloudComponentImpl;
import otocloud.framework.core.OtoCloudEventHandlerImpl;
import otocloud.framework.core.session.Session;
import otocloud.framework.core.session.SessionStore;

/**
 * Created by zhangye on 2015-10-15.
 */
@IgnoreAuthVerify
public class UserLoginHandler extends OtoCloudEventHandlerImpl<JsonObject> {

    private static final String ADDRESS = "login";
    
    public UserLoginHandler(OtoCloudComponentImpl componentImpl) {
        super(componentImpl);
    }


    public static String getActionUrl(){
        return ADDRESS;
    }

    @Override
    public HandlerDescriptor getHanlderDesc() {
        HandlerDescriptor handlerDescriptor = super.getHanlderDesc();
        ActionURI uri = new ActionURI(ADDRESS, HttpMethod.POST);
        handlerDescriptor.setRestApiURI(uri);
        return handlerDescriptor;
    }


    @Override
    public String getEventAddress() {
        return ADDRESS;
    }    
    
    /* 
     * { 
     * 	  acct_id: 
     * }
     * 
     */    
    @Override
    public void handle(OtoCloudBusMessage<JsonObject> msg) {
    	
		String token = msg.headers().get("token");	
     	
    	JsonObject loginInfo = msg.body();    	
        JsonObject data = loginInfo.getJsonObject("content"); 
		Long acctId = data.getLong("acct_id");   
		
        JsonObject sessionData = msg.getSession();
        sessionData.put("acct_id", acctId.toString());	
		
		//在session中补充acct_id
		SessionStore sessionStore = this.componentImpl.getService().getSessionStore();
		if(sessionStore != null){
			sessionStore.get(token, sessionRet->{						
				Session session = sessionRet.result();		
				
				session.setItem("acct_id", acctId.toString() ,retRet->{
					msg.reply(retRet.result());									
					session.close(closeHandler->{							
					});						
				});

			});				
		}else{
			String errMsgString = "无session服务.";
			componentImpl.getLogger().error(errMsgString);
			msg.fail(100, errMsgString);
		}	


 
    }      
    

    
    
}

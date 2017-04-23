package otocloud.auth.admin;

import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import otocloud.common.ActionURI;
import otocloud.framework.common.IgnoreAuthVerify;
import otocloud.framework.core.CommandMessage;
import otocloud.framework.core.HandlerDescriptor;
import otocloud.framework.core.OtoCloudComponentImpl;
import otocloud.framework.core.OtoCloudEventHandlerImpl;
import otocloud.framework.core.session.Session;
import otocloud.framework.core.session.SessionStore;

/**
 * TODO 退出时,向网关发送消息,告知用户将退出121, 请登出ERP系统.
 * Created by zhangye on 2015-10-18.
 */
@IgnoreAuthVerify
public class UserLogoutHandler extends OtoCloudEventHandlerImpl<JsonObject> {

    private static final String ADDRESS = "logout";
    
    public UserLogoutHandler(OtoCloudComponentImpl componentImpl) {
        super(componentImpl);
    }

    /* 
     * { 
     * 	  
     * }
     * 
     */  
    @Override
    public void handle(CommandMessage<JsonObject> msg) {
		String token = msg.headers().get("token");
		
		//登录成功，构建用户session
		SessionStore sessionStore = this.componentImpl.getService().getSessionStore();
		if(sessionStore != null){
			sessionStore.get(token, sessionRet->{						
				Session session = sessionRet.result();		
				
				session.removeAll(retRet->{
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
    


    @Override
    public String getEventAddress() {
        return ADDRESS;
    }


    @Override
    public HandlerDescriptor getHanlderDesc() {
        HandlerDescriptor handlerDescriptor = super.getHanlderDesc();
        ActionURI uri = new ActionURI(ADDRESS, HttpMethod.POST);
        handlerDescriptor.setRestApiURI(uri);
        return handlerDescriptor;
    }
}

package otocloud.auth.user;

import io.vertx.core.Future;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.sql.UpdateResult;
import otocloud.auth.dao.UserDAO;
import otocloud.common.ActionURI;
import otocloud.framework.core.HandlerDescriptor;
import otocloud.framework.core.OtoCloudBusMessage;
import otocloud.framework.core.OtoCloudComponentImpl;
import otocloud.framework.core.OtoCloudEventHandlerImpl;

/**
 * Created by zhangye on 2015-10-20.
 */

public class UserUpdateHandler extends OtoCloudEventHandlerImpl<JsonObject> {
	
	private static final String ADDRESS = "update";

    public UserUpdateHandler(OtoCloudComponentImpl componentImpl) {
        super(componentImpl);
    }
    
    
    /* 
     * { 
     * 		  id: 
	 *	      name: 用户名
	 *	      cell_no: 电话
	 *	      email: 邮箱	  
     * }
     * 
     */
    @Override
    public void handle(OtoCloudBusMessage<JsonObject> msg) {
/*        boolean isLegal = busMessageChecker.checkUpdateUserInfo(msg.body(), errMsg -> {
            msg.fail(ErrCode.BUS_MSG_FORMAT_ERR.getCode(), errMsg);
        });

        if (!isLegal) {
            return;
        }*/
    	
    	JsonObject body = msg.body();
    	
		JsonObject params = body.getJsonObject("queryParams");		
		
		Long userId = Long.parseLong(params.getString("id"));
    	
        JsonObject content = msg.body().getJsonObject("content");
    	
    	//Long userId = content.getLong("id", 0L);
    	String name = content.getString("name", null);
    	String cell_no = content.getString("cell_no", null);
    	String email = content.getString("email", null);    	

        Future<UpdateResult> future = Future.future();
        
        UserDAO userDAO = new UserDAO(this.componentImpl.getSysDatasource());
        userDAO.update(userId, name, cell_no, email, future);

        future.setHandler(ret -> {
            if (ret.succeeded()) {
                msg.reply(ret.result().toJson());
            } else {
            	Throwable errThrowable = ret.cause();
    			String errMsgString = errThrowable.getMessage();
    			this.componentImpl.getLogger().error(errMsgString, errThrowable);
    			msg.fail(100, errMsgString);
            }
        });
    }


    @Override
    public String getEventAddress() {
        return ADDRESS;
    }

    @Override
    public HandlerDescriptor getHanlderDesc() {
        HandlerDescriptor handlerDescriptor = super.getHanlderDesc();
        ActionURI uri = new ActionURI(ADDRESS + "/:id", HttpMethod.PUT);
        handlerDescriptor.setRestApiURI(uri);
        return handlerDescriptor;
    }
}

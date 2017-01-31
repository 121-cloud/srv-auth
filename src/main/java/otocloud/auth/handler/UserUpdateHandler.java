package otocloud.auth.handler;

import io.vertx.core.Future;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.sql.UpdateResult;
import otocloud.auth.dao.UserDAO;
import otocloud.auth.verticle.UserComponent;
import otocloud.common.ActionURI;
import otocloud.framework.core.HandlerDescriptor;
import otocloud.framework.core.OtoCloudBusMessage;
import otocloud.framework.core.OtoCloudComponentImpl;
import otocloud.framework.core.OtoCloudEventHandlerImpl;

/**
 * Created by zhangye on 2015-10-20.
 */

public class UserUpdateHandler extends OtoCloudEventHandlerImpl<JsonObject> {

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
    	
        JsonObject content = msg.body().getJsonObject("content");
    	
    	Long userId = content.getLong("id", 0L);
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

    /**
     * 得到对应事件总线的地址。
     *
     * @return "服务名"."组件名"."具体地址"，即 "服务名".user-management.users.put
     */
    @Override
    public String getEventAddress() {
        return UserComponent.MANAGE_USER_ADDRESS + ".put";
    }

    /**
     * 注册REST API。
     *
     * @return put /api/"服务名"/"组件名"/users/:openId
     */
    @Override
    public HandlerDescriptor getHanlderDesc() {
        HandlerDescriptor handlerDescriptor = super.getHanlderDesc();
        ActionURI uri = new ActionURI("users/:openId", HttpMethod.PUT);
        handlerDescriptor.setRestApiURI(uri);
        return handlerDescriptor;
    }
}

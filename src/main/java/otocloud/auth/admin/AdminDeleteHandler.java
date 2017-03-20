package otocloud.auth.admin;

import io.vertx.core.Future;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import otocloud.auth.dao.UserDAO;
import otocloud.common.ActionURI;
import otocloud.framework.core.HandlerDescriptor;
import otocloud.framework.core.OtoCloudBusMessage;
import otocloud.framework.core.OtoCloudComponentImpl;
import otocloud.framework.core.OtoCloudEventHandlerImpl;

/**
 * Created by zhangye on 2015-10-27.
 */
public class AdminDeleteHandler extends OtoCloudEventHandlerImpl<JsonObject> {
	
	public static final String ADDRESS = "delete";

    public AdminDeleteHandler(OtoCloudComponentImpl componentImpl) {
        super(componentImpl);
    }

    /* 
     * { 
     * 	  id: 
     * }
     * 
     */
    @Override
    public void handle(OtoCloudBusMessage<JsonObject> msg) {
/*        boolean isLegal = BusMessageChecker.checkDeleteUserInfo(msg.body(), errMsg -> {
            msg.fail(ErrCode.BUS_MSG_FORMAT_ERR.getCode(), errMsg);
        });

        if (!isLegal) {
            return;
        }*/
    	
    	JsonObject body = msg.body();
    	
		JsonObject params = body.getJsonObject("queryParams");		
		
		Long userId = Long.parseLong(params.getString("id"));

        //JsonObject content = msg.body().getJsonObject("content");
    	
    	//Long userId = content.getLong("id", 0L);
 
        Future<Boolean> future = Future.future();
        
        UserDAO userDAO = new UserDAO(this.componentImpl.getSysDatasource());
        userDAO.deleteById(userId, future);

        future.setHandler(ret -> {
            if (ret.succeeded()) {
                msg.reply(ret.result());
            } else {
            	Throwable errThrowable = ret.cause();
    			String errMsgString = errThrowable.getMessage();
    			this.componentImpl.getLogger().error(errMsgString, errThrowable);
    			msg.fail(100, errMsgString);
            }
        });
    }

    /**
     * 最终的REST API是：put /api/"服务名"/"组件名"/users/:openId
     *
     * @return
     */
    @Override
    public HandlerDescriptor getHanlderDesc() {
        HandlerDescriptor handlerDescriptor = super.getHanlderDesc();
        ActionURI uri = new ActionURI(ADDRESS + "/:id", HttpMethod.DELETE);
        handlerDescriptor.setRestApiURI(uri);
        return handlerDescriptor;
    }

    /**
     * 事件总线上注册的最终地址是："服务名"."组件名".users.delete
     * @return
     */
    @Override
    public String getEventAddress() {
        return ADDRESS;
    }
}

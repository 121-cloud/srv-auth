package otocloud.auth.admin;

import io.vertx.core.Future;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.sql.ResultSet;
import otocloud.auth.dao.UserDAO;
import otocloud.common.ActionURI;
import otocloud.framework.core.CommandMessage;
import otocloud.framework.core.HandlerDescriptor;
import otocloud.framework.core.OtoCloudComponentImpl;
import otocloud.framework.core.OtoCloudEventHandlerImpl;

/**
 * 用户列表的分页查询
 * 
 */
public class UserPostQueryHandler extends OtoCloudEventHandlerImpl<JsonObject> {

	private static final String ADDRESS = "query-post";
	
    public UserPostQueryHandler(OtoCloudComponentImpl componentImpl) {
        super(componentImpl);
    }
    
    /* 
     * {
     * 	  acct_id:
     * 	  user_id:
     * }
     * 
     */
    @Override
    public void handle(CommandMessage<JsonObject> msg) {

        JsonObject body = msg.body();
        JsonObject content = body.getJsonObject("content");

        Long user_id = content.getLong("user_id");      
        Long acctId = content.getLong("acct_id");      

        Future<ResultSet> getFuture = Future.future();

        UserDAO userDAO = new UserDAO(this.componentImpl.getSysDatasource());
        userDAO.getUserPosts(acctId, user_id, getFuture);

        getFuture.setHandler(ret -> {
            if (ret.succeeded()) {
                msg.reply(ret.result().getRows());
            } else {
            	Throwable errThrowable = ret.cause();
    			String errMsgString = errThrowable.getMessage();
    			this.componentImpl.getLogger().error(errMsgString, errThrowable);
    			msg.fail(100, errMsgString);
            }
        });
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
}

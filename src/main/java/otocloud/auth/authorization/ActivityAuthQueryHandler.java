package otocloud.auth.authorization;

import io.vertx.core.Future;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.sql.ResultSet;
import otocloud.auth.dao.AuthDAO;
import otocloud.common.ActionURI;
import otocloud.framework.core.HandlerDescriptor;
import otocloud.framework.core.OtoCloudBusMessage;
import otocloud.framework.core.OtoCloudComponentImpl;
import otocloud.framework.core.OtoCloudEventHandlerImpl;



public class ActivityAuthQueryHandler extends OtoCloudEventHandlerImpl<JsonObject> {

    public ActivityAuthQueryHandler(OtoCloudComponentImpl componentImpl) {
        super(componentImpl);
    }

    /* 
     * 	{
		acct_biz_unit_post_id
		}
     */
    @Override
    public void handle(OtoCloudBusMessage<JsonObject> msg) {
        
        JsonObject body = msg.body();
        //JsonObject session = body.getJsonObject(SessionSchema.SESSION);

        JsonObject post = body.getJsonObject("content");
        
        Long acct_biz_unit_post_id = post.getLong("acct_biz_unit_post_id");

        Future<ResultSet> getFuture = Future.future();
        
        AuthDAO authDAO = new AuthDAO(componentImpl.getSysDatasource());		
        authDAO.getList(acct_biz_unit_post_id, getFuture);
        
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

    /**
     * "服务名".user-management.department.query
     *
     * @return
     */
    @Override
    public String getEventAddress() {
        return "activity_query";
    }

    /**
     * 注册REST API。
     *
     * @return get /api/"服务名"/"组件名"
     */
    @Override
    public HandlerDescriptor getHanlderDesc() {
        HandlerDescriptor handlerDescriptor = super.getHanlderDesc();
        ActionURI uri = new ActionURI("activity_query", HttpMethod.GET);
        handlerDescriptor.setRestApiURI(uri);
        return handlerDescriptor;
    }
}

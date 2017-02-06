package otocloud.auth.user;

import java.util.List;

import io.vertx.core.Future;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.sql.ResultSet;
import otocloud.auth.dao.UserDAO;
import otocloud.common.ActionURI;
import otocloud.framework.core.HandlerDescriptor;
import otocloud.framework.core.OtoCloudBusMessage;
import otocloud.framework.core.OtoCloudComponentImpl;
import otocloud.framework.core.OtoCloudEventHandlerImpl;


/**
 * 用户列表的分页查询
 * zhangyef@yonyou.com on 2015-12-18.
 */
public class AcctOwnerGetHandler extends OtoCloudEventHandlerImpl<JsonObject> {

	private static final String ADDRESS = "owner-query";
	
    public AcctOwnerGetHandler(OtoCloudComponentImpl componentImpl) {
        super(componentImpl);
    }
    
    /* 
     * {
     * 	  acct_id: 
     * }
     * 
     */
    @Override
    public void handle(OtoCloudBusMessage<JsonObject> msg) {

        JsonObject body = msg.body();
       
        JsonObject content = body.getJsonObject("content");
        Long acctId = content.getLong("acct_id");

        Future<ResultSet> retFuture = Future.future();

        UserDAO userDAO = new UserDAO(this.componentImpl.getSysDatasource());
        userDAO.getOwnerUserByAcctId(acctId, retFuture);

        retFuture.setHandler(ret -> {
            if (ret.failed()) {
				Throwable err = ret.cause();
				String errMsg = err.getMessage();
				componentImpl.getLogger().error(errMsg, err);	
				msg.fail(400, errMsg);
                return;
            }else{
            	List<JsonObject> retDatas = ret.result().getRows();
            	if(retDatas != null && retDatas.size() > 0){
            		msg.reply(retDatas.get(0));
            	}else{
            		msg.fail(100, "无数据");
            	}
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

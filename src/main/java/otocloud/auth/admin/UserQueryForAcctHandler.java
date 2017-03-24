package otocloud.auth.admin;

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

import java.util.List;

/**
 * 用户列表的分页查询
 * zhangyef@yonyou.com on 2015-12-18.
 */
public class UserQueryForAcctHandler extends OtoCloudEventHandlerImpl<JsonObject> {

	private static final String ADDRESS = "query-for-acct";
	
    public UserQueryForAcctHandler(OtoCloudComponentImpl componentImpl) {
        super(componentImpl);
    }
    
    /* 
     * {
     * 	  acct_id: 
     * 	  paging: {
	 *	      sort_field: 排序字段，只支持单个字段,
	 *	      sort_direction: 1：升序，-1：降序,
	 *	      page_number: 页码,
	 *	      page_size: 每页大小,
	 *	      total: 总数，下次需要回传
	 *	      total_page: 总页数，下次需要回传
	 *	   }
     * }
     * 
     */
    @Override
    public void handle(OtoCloudBusMessage<JsonObject> msg) {

        JsonObject body = msg.body();
        JsonObject content = body.getJsonObject("content");

        //Long acctId = content.getLong("acct_id");
        Long acct_id = content.getLong("acct_id");
        JsonObject pagingOptions = content.getJsonObject("paging");
        Integer pageSize = pagingOptions.getInteger("page_size");


        Future<ResultSet> pageFuture = Future.future();

        UserDAO userDAO = new UserDAO(this.componentImpl.getSysDatasource());
        userDAO.getUserListForAcctByPage(acct_id, pagingOptions, pageFuture);

        pageFuture.setHandler(ret -> {
            if (ret.failed()) {
				Throwable err = ret.cause();
				String errMsg = err.getMessage();
				componentImpl.getLogger().error(errMsg, err);	
				msg.fail(400, errMsg);
                return;
            }
            
            List<JsonObject> retDataArrays = ret.result().getRows();
            
            Future<Integer> countFuture = Future.future();
            userDAO.countUserForAcct(acct_id, countFuture);
            countFuture.setHandler(countUserRet -> {
                if (countUserRet.failed()) {                   
    				Throwable errThrowable = countUserRet.cause();
    				String errMsgString = errThrowable.getMessage();
    				this.componentImpl.getLogger().error(errMsgString, errThrowable);
    				
                    JsonObject reply = new JsonObject();
                    reply.put("total_page", 1);                    
                    reply.put("total", retDataArrays.size());
                    reply.put("datas", retDataArrays);
                    
                    msg.reply(reply);
                    
                }else{
                	Integer items_total_num = countUserRet.result();
                    boolean oneMorePage = items_total_num % pageSize != 0;

                    JsonObject reply = new JsonObject();
                    reply.put("total_page", items_total_num / pageSize + (oneMorePage ? 1 : 0));                    
                    reply.put("total", items_total_num);
                    reply.put("datas", retDataArrays);
                    
                    msg.reply(reply);

                }
            });


        });
    }

    /**
     * @return post /api/"服务名"/"组件名"/users/page
     */
    @Override
    public HandlerDescriptor getHanlderDesc() {
        HandlerDescriptor handlerDescriptor = super.getHanlderDesc();
        ActionURI uri = new ActionURI(ADDRESS, HttpMethod.POST);
        handlerDescriptor.setRestApiURI(uri);
        return handlerDescriptor;
    }

    /**
     * @return 服务名"."组件名"."具体地址"，即 "服务名".user-management.users.get.page
     */
    @Override
    public String getEventAddress() {
        return ADDRESS;
    }
}

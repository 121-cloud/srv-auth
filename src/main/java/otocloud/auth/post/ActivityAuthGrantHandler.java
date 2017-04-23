package otocloud.auth.post;

import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import otocloud.auth.dao.AuthDAO;
import otocloud.common.ActionURI;
import otocloud.common.SessionSchema;
import otocloud.framework.core.CommandMessage;
import otocloud.framework.core.HandlerDescriptor;
import otocloud.framework.core.OtoCloudComponentImpl;
import otocloud.framework.core.OtoCloudEventHandlerImpl;



public class ActivityAuthGrantHandler extends OtoCloudEventHandlerImpl<JsonObject> {
	
	public static final String DEP_CREATE = "activity_grant";


    public ActivityAuthGrantHandler(OtoCloudComponentImpl componentImpl) {
        super(componentImpl);
    }

	/**
	{
		acct_biz_unit_post_id
		acct_app_activity_id
		acct_id,
		app_id,
		app_activity_id
	}
	*/
	@Override
	public void handle(CommandMessage<JsonObject> msg) {
		JsonObject body = msg.body();
		
		componentImpl.getLogger().info(body.toString());
		
		JsonObject content = body.getJsonObject("content");
		JsonObject sessionInfo = msg.getSession();
		
		Long acct_biz_unit_post_id = content.getLong("acct_biz_unit_post_id");
		Long acct_app_activity_id = content.getLong("acct_app_activity_id");
		Long acct_id = content.getLong("acct_id");
		Long app_id = content.getLong("app_id");
		Long app_activity_id = content.getLong("app_activity_id");
		
		Long entry_id = 0L; //默认为0，表示没有用户.
        if (sessionInfo != null) {
        	entry_id = Long.parseLong(sessionInfo.getString(SessionSchema.CURRENT_USER_ID));
        }
			
		AuthDAO authDAO = new AuthDAO(componentImpl.getSysDatasource());
		//departmentDAO.setDataSource(componentImpl.getSysDatasource());		
		
		authDAO.create(acct_biz_unit_post_id, 
	    		acct_app_activity_id, 
	    		acct_id, 
	    		app_id,
	    		app_activity_id,
	    		entry_id, daoRet -> {

			if (daoRet.failed()) {
				Throwable err = daoRet.cause();
				String errMsg = err.getMessage();
				componentImpl.getLogger().error(errMsg, err);	
				msg.fail(400, errMsg);
			} else {
				Long id = daoRet.result();
				msg.reply(new JsonObject().put("id", id));				
			}

		});

	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public HandlerDescriptor getHanlderDesc() {		
		
		HandlerDescriptor handlerDescriptor = super.getHanlderDesc();
		
		//参数
/*		List<ApiParameterDescriptor> paramsDesc = new ArrayList<ApiParameterDescriptor>();
		paramsDesc.add(new ApiParameterDescriptor("targetacc",""));		
		paramsDesc.add(new ApiParameterDescriptor("soid",""));		
		handlerDescriptor.setParamsDesc(paramsDesc);	*/
		
		ActionURI uri = new ActionURI(DEP_CREATE, HttpMethod.POST);
		handlerDescriptor.setRestApiURI(uri);
		
		return handlerDescriptor;		

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getEventAddress() {
		return DEP_CREATE;
	}
	
}

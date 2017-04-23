/*
 * Copyright (C) 2015 121Cloud Project Group  All rights reserved.
 */
package otocloud.auth.post;

import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.sql.UpdateResult;
import otocloud.auth.dao.AuthDAO;
import otocloud.common.ActionURI;
import otocloud.framework.core.CommandMessage;
import otocloud.framework.core.HandlerDescriptor;
import otocloud.framework.core.OtoCloudComponentImpl;
import otocloud.framework.core.OtoCloudEventHandlerImpl;


public class ActivityAuthDeleteHandler extends OtoCloudEventHandlerImpl<JsonObject> {

	public static final String DEP_DELETE = "activity_delete";

	/**
	 * Constructor.
	 *
	 * @param componentImpl
	 */
	public ActivityAuthDeleteHandler(OtoCloudComponentImpl componentImpl) {
		super(componentImpl);
	}

	/**
	{
		"id":
	}
	 */
	@Override
	public void handle(CommandMessage<JsonObject> msg) {
		JsonObject body = msg.body();
		
		componentImpl.getLogger().info(body.toString());
		
		JsonObject params = body.getJsonObject("queryParams");
		//JsonObject sessionInfo = body.getJsonObject("session",null);		
		
		Long id = Long.parseLong(params.getString("id"));
			
		AuthDAO authDAO = new AuthDAO(componentImpl.getSysDatasource());		
		
		authDAO.deleteById(id, daoRet -> {

			if (daoRet.failed()) {
				Throwable err = daoRet.cause();
				String errMsg = err.getMessage();
				componentImpl.getLogger().error(errMsg, err);	
				msg.fail(400, errMsg);
			} else {
				UpdateResult result = daoRet.result();		
				if (result.getUpdated() <= 0) {						
					String errMsg = "更新影响行数为0";
					componentImpl.getLogger().error(errMsg);									
					msg.fail(400, errMsg);
				} else {
					msg.reply("ok");
				}
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
		
		ActionURI uri = new ActionURI(":id", HttpMethod.DELETE);
		handlerDescriptor.setRestApiURI(uri);
		
		return handlerDescriptor;		

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getEventAddress() {
		return DEP_DELETE;
	}

}

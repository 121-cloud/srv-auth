package otocloud.auth.admin;

import io.vertx.core.Future;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import otocloud.auth.dao.UserDAO;
import otocloud.common.ActionURI;
import otocloud.framework.common.IgnoreAuthVerify;
import otocloud.framework.core.HandlerDescriptor;
import otocloud.framework.core.OtoCloudBusMessage;
import otocloud.framework.core.OtoCloudComponentImpl;
import otocloud.framework.core.OtoCloudEventHandlerImpl;

/**
 * 注册用户时,查询即将注册的手机号是否已经被注册过,即手机号是否在数据库中存在.
 * <p>
 * zhangyef@yonyou.com on 2015-11-09.
 */
@IgnoreAuthVerify
public class CellNoQueryHandler extends OtoCloudEventHandlerImpl<JsonObject> {
	
	public static final String ADDRESS = "verify_cellno";

    public CellNoQueryHandler(OtoCloudComponentImpl componentImpl) {
        super(componentImpl);
    }

    /* 
     * { 
     * 	  cell_no: 手机号
     * }
     * 
     */
    @Override
    public void handle(OtoCloudBusMessage<JsonObject> msg) {
/*        boolean isLegal = BusMessageChecker.checkQueryCellNo(msg.body(), errMsg -> {
            msg.fail(ErrCode.DUPLICATED_CELL_NO.getCode(), errMsg);
        });

        if (!isLegal) {
            return;
        }*/
    	
        JsonObject content = msg.body().getJsonObject("content");
    	String cell_no = content.getString("cell_no", null);
    	
    	
        Future<Long> verifyFuture = Future.future();
        //检查手机号字段是否重复.(重复表示已经注册过)
        UserDAO userDAO = new UserDAO(this.componentImpl.getSysDatasource());
        userDAO.isRegisteredCellNo(cell_no, verifyFuture);
        
        verifyFuture.setHandler(ret -> {
            if (ret.failed()) {
				Throwable err = ret.cause();
				String errMsg = err.getMessage();
				componentImpl.getLogger().error(errMsg, err);	
				msg.fail(400, errMsg);
                return;
            }

            boolean exists = (ret.result() > 0L) ? true : false;
            JsonObject reply = new JsonObject();
            reply.put("exists", exists);

            msg.reply(reply);
 
        });
    }

    /**
     * 事件总线地址.
     *
     * @return "服务名"."组件名".users.verify.cellNo
     */
    @Override
    public String getEventAddress() {
        return ADDRESS;
    }

    /**
     * REST API.
     *
     * @return get /api/"服务名"/"组件名"/users/verify/:cellNo
     */
    @Override
    public HandlerDescriptor getHanlderDesc() {
        HandlerDescriptor handlerDescriptor = super.getHanlderDesc();
        ActionURI uri = new ActionURI(ADDRESS, HttpMethod.GET);
        handlerDescriptor.setRestApiURI(uri);
        return handlerDescriptor;
    }
}

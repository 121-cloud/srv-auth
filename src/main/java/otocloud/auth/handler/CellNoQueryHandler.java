package otocloud.auth.handler;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import io.vertx.core.Future;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import otocloud.auth.common.exception.ErrCode;
import otocloud.auth.common.util.BusMessageChecker;
import otocloud.auth.entity.ReplyMessage;
import otocloud.auth.service.UserService;
import otocloud.auth.verticle.UserComponent;
import otocloud.common.ActionURI;
import otocloud.common.webserver.MessageBodyConvention;
import otocloud.framework.core.HandlerDescriptor;
import otocloud.framework.core.OtoCloudBusMessage;
import otocloud.framework.core.OtoCloudComponentImpl;
import otocloud.framework.core.OtoCloudEventHandlerImpl;

/**
 * 注册用户时,查询即将注册的手机号是否已经被注册过,即手机号是否在数据库中存在.
 * <p>
 * zhangyef@yonyou.com on 2015-11-09.
 */
public class CellNoQueryHandler extends OtoCloudEventHandlerImpl<JsonObject> {

    public static final String CELL_NO = "cellNo";

    @Inject
    private UserService userService;

    @Inject
    public CellNoQueryHandler(@Named("UserComponent") OtoCloudComponentImpl componentImpl) {
        super(componentImpl);
    }

    @Override
    public void handle(OtoCloudBusMessage<JsonObject> msg) {
        boolean isLegal = BusMessageChecker.checkQueryCellNo(msg.body(), errMsg -> {
            msg.fail(ErrCode.DUPLICATED_CELL_NO.getCode(), errMsg);
        });

        if (!isLegal) {
            return;
        }
        JsonObject body = msg.body();
        JsonObject queryParams = body.getJsonObject(MessageBodyConvention.HTTP_QUERY);
        String cellNo = queryParams.getString(CELL_NO);

        Future<Boolean> future = Future.future();
        userService.verifyCellNo(cellNo, future);
        future.setHandler(ret -> {
            if(ret.succeeded()) {
                boolean exists = ret.result();
                JsonObject reply = new JsonObject();
                reply.put("exists", exists);

                msg.reply(reply);
            }else{
                msg.reply(ReplyMessage.failure().toString());
            }
        });
    }

    /**
     * 事件总线地址.
     *
     * @return "服务名"."组件名".users.verify.cellNo
     */
    @Override
    public String getEventAddress() {
        return UserComponent.MANAGE_USER_ADDRESS + ".verify.cellNo";
    }

    /**
     * REST API.
     *
     * @return get /api/"服务名"/"组件名"/users/verify/:cellNo
     */
    @Override
    public HandlerDescriptor getHanlderDesc() {
        HandlerDescriptor handlerDescriptor = super.getHanlderDesc();
        ActionURI uri = new ActionURI("users/verify", HttpMethod.GET);
        handlerDescriptor.setRestApiURI(uri);
        return handlerDescriptor;
    }
}

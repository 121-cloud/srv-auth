package otocloud.auth.handler;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import io.vertx.core.Future;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import otocloud.auth.common.exception.ErrCode;
import otocloud.auth.common.session.SessionSchema;
import otocloud.auth.common.util.BusMessageChecker;
import otocloud.auth.common.util.Mapper;
import otocloud.auth.mybatis.entity.AuthUser;
import otocloud.auth.service.UserService;
import otocloud.auth.verticle.UserComponent;
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
public class UserQueryHandler extends OtoCloudEventHandlerImpl<JsonObject> {
    protected static final Logger logger = LoggerFactory.getLogger(UserQueryHandler.class);

    @Inject
    private UserService userService;

    @Inject
    private BusMessageChecker busMessageChecker;

    @Inject
    public UserQueryHandler(@Named("UserComponent") OtoCloudComponentImpl componentImpl) {
        super(componentImpl);
    }

    @Override
    public void handle(OtoCloudBusMessage<JsonObject> msg) {
        logger.info("AuthService - 接收到了分页查询用户列表的请求.");

        boolean isLegal = busMessageChecker.checkQueryUsersByPage(msg.body(), errMsg -> {
            msg.fail(ErrCode.BUS_MSG_FORMAT_ERR.getCode(), errMsg);
        });

        if (!isLegal) {
            return;
        }

        JsonObject body = msg.body();
        JsonObject content = body.getJsonObject("content");

        int page_index = content.getInteger("page_index");
        int page_size = content.getInteger("page_size");

        int department_id = content.getInteger("department_id");

        JsonObject session = body.getJsonObject(SessionSchema.SESSION);
        int acctId = session.getInteger(SessionSchema.ORG_ACCT_ID);

        Future<List<AuthUser>> pageFuture = Future.future();
        userService.getUserListByPage(acctId, department_id, page_index, page_size, pageFuture);

        pageFuture.setHandler(ret -> {
            if (ret.failed()) {
                msg.fail(4004, "查询的用户数据不存在,或无法查询用户的数据.");

                return;
            }

            int items_total_num = userService.countUser(acctId, department_id);

            boolean oneMorePage = items_total_num % page_size != 0;

            JsonObject reply = new JsonObject();
            reply.put("page_num", items_total_num / page_size + (oneMorePage ? 1 : 0));
            reply.put("page_index", page_index);
            reply.put("items_total_num", items_total_num);


            List<AuthUser> users = ret.result();
            try {
                JsonArray items_in_page = Mapper.toJsonArray(users);
                reply.put("items_in_page", items_in_page);

                msg.reply(reply);
            } catch (Exception ignore) {
                msg.fail(1, "内部数据格式转换错误。");
            }
        });
    }

    /**
     * @return post /api/"服务名"/"组件名"/users/page
     */
    @Override
    public HandlerDescriptor getHanlderDesc() {
        HandlerDescriptor handlerDescriptor = super.getHanlderDesc();
        ActionURI uri = new ActionURI("users/page", HttpMethod.POST);
        handlerDescriptor.setRestApiURI(uri);
        return handlerDescriptor;
    }

    /**
     * @return 服务名"."组件名"."具体地址"，即 "服务名".user-management.users.get.page
     */
    @Override
    public String getEventAddress() {
        return UserComponent.MANAGE_USER_ADDRESS + ".get.page";
    }
}

package otocloud.auth.handler.query;

import com.google.inject.Injector;
import io.vertx.core.Future;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import otocloud.auth.common.MessageFinder;
import otocloud.auth.service.AccountService;
import otocloud.common.ActionURI;
import otocloud.framework.core.HandlerDescriptor;
import otocloud.framework.core.OtoCloudBusMessage;
import otocloud.framework.core.OtoCloudComponentImpl;
import otocloud.framework.core.OtoCloudEventHandlerImpl;

/**
 * 查询指定企业下的用户统计信息.
 * zhangyef@yonyou.com on 2016-01-22.
 */

public class UserStaticsHandler extends OtoCloudEventHandlerImpl<JsonObject> {
    protected static final Logger logger = LoggerFactory.getLogger(UserStaticsHandler.class);

    private Injector injector;

    private AccountService accountService;

    public UserStaticsHandler(OtoCloudComponentImpl component , Injector injector) {
        super(component);
        this.injector = injector;
    }

    @Override
    public void handle(OtoCloudBusMessage<JsonObject> msg) {
        logger.info("收到用户统计信息查询请求.");
        MessageFinder finder = new MessageFinder(msg);
        int userId = finder.getUserId();
        int acctId = finder.getAcctId();

        logger.info("即将查询 [" + acctId + "] 账户下的用户统计信息.");
        AccountService accountService = getAccountService();

        Future<JsonObject> getFuture = Future.future();
        accountService.getUserStatistics(acctId, getFuture);
        getFuture.setHandler(ret -> {
            if(ret.failed()){
                logger.warn("无法查询用户统计数据. 管理员ID: " + userId);
                msg.fail(1, "无法查询用户统计数据,可能的原因是: 没有查询权限.");
                return;
            }

            msg.reply(ret.result());
        });
    }

    /**
     * 用于延迟加载.
     * @return
     */
    private AccountService getAccountService() {
        if (accountService == null) {
            accountService = injector.getInstance(AccountService.class);
        }

        return accountService;
    }

    /**
     * 服务名.组件名.users.statistics.get
     *
     * @return <服务名>.query-management.users.statistics.get
     */
    @Override
    public String getEventAddress() {
        return "users.statistics.get";
    }

    /**
     * get /api/"服务名"/"组件名"/users/statistics
     *
     * @return /api/otocloud-auth/query-management/users/statistics
     */
    @Override
    public HandlerDescriptor getHanlderDesc() {
        HandlerDescriptor handlerDescriptor = super.getHanlderDesc();
        ActionURI uri = new ActionURI("users/statistics", HttpMethod.GET);
        handlerDescriptor.setRestApiURI(uri);
        return handlerDescriptor;
    }
}
package otocloud.auth.handler.erpconnection;

import com.google.inject.Injector;
import io.vertx.core.Future;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import otocloud.auth.common.MessageFinder;
import otocloud.auth.service.AccountService;
import otocloud.common.ActionURI;
import otocloud.framework.core.HandlerDescriptor;
import otocloud.framework.core.OtoCloudBusMessage;
import otocloud.framework.core.OtoCloudComponentImpl;
import otocloud.framework.core.OtoCloudEventHandlerImpl;

/**
 * 查询用户的绑定状态和绑定账户.
 * zhangyef@yonyou.com on 2016-01-20.
 */
public class ErpAccountBindInfoHandler extends OtoCloudEventHandlerImpl<JsonObject> {

    private Injector injector;
    private AccountService accountService;

    public ErpAccountBindInfoHandler(OtoCloudComponentImpl component, Injector injector) {
        super(component);
        this.injector = injector;
    }

    private AccountService getAccountService() {
        if (accountService == null) {
            accountService = injector.getInstance(AccountService.class);
        }

        return accountService;
    }

    @Override
    public void handle(OtoCloudBusMessage<JsonObject> msg) {
        MessageFinder finder = new MessageFinder(msg);

//        int acctId = finder.getAcctId();
        int userId = finder.getUserId();

        Future<JsonObject> getFuture = Future.future();
        getAccountService().getBindInfo(userId, getFuture);

        getFuture.setHandler(ret-> {
            if(ret.failed()){
                msg.fail(-1, ret.cause().getMessage());

                return;
            }

            msg.reply(ret.result());
        });
    }

    /**
     * <服务名>.<组件名>.users.bind.get
     *
     * @return otocloud-auth.erp-connection.users.bind.get
     */
    @Override
    public String getEventAddress() {
        return "users.bind.get";
    }

    /**
     * get /api/"服务名"/"组件名"/users/bind
     *
     * @return /api/otocloud-auth/erp-connection/users/bind
     */
    @Override
    public HandlerDescriptor getHanlderDesc() {
        HandlerDescriptor handlerDescriptor = super.getHanlderDesc();
        ActionURI uri = new ActionURI("users/bind", HttpMethod.GET);
        handlerDescriptor.setRestApiURI(uri);
        return handlerDescriptor;
    }
}

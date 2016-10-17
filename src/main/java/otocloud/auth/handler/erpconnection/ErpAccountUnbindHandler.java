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
 * 解除ERP账户绑定.
 * zhangyef@yonyou.com on 2016-01-21.
 */

public class ErpAccountUnbindHandler extends OtoCloudEventHandlerImpl<JsonObject> {
    private Injector injector;
    private AccountService accountService;

    public ErpAccountUnbindHandler(OtoCloudComponentImpl component, Injector injector) {
        super(component);
        this.injector = injector;
    }

    @Override
    public void handle(OtoCloudBusMessage<JsonObject> msg) {
        MessageFinder finder = new MessageFinder(msg);
        int userId = finder.getUserId();

        Future<JsonObject> unbindFuture = Future.future();

        getAccountService().unbindWithErp(userId, unbindFuture);

        unbindFuture.setHandler(ret -> {
            if(ret.failed()){
                msg.fail(-1, ret.cause().getMessage());
                return;
            }

            msg.reply(ret.result());
        });

    }

    private AccountService getAccountService() {
        if (accountService == null) {
            accountService = injector.getInstance(AccountService.class);
        }

        return accountService;
    }

    /**
     * <服务名>.<组件名>.users.bind.delete
     *
     * @return otocloud-auth.erp-connection.users.bind.delete
     */
    @Override
    public String getEventAddress() {
        return "users.bind.delete";
    }

    /**
     * delete /api/"服务名"/"组件名"/users/bind
     *
     * @return /api/otocloud-auth/erp-connection/users/bind
     */
    @Override
    public HandlerDescriptor getHanlderDesc() {
        HandlerDescriptor handlerDescriptor = super.getHanlderDesc();
        ActionURI uri = new ActionURI("users/bind", HttpMethod.DELETE);
        handlerDescriptor.setRestApiURI(uri);
        return handlerDescriptor;
    }
}
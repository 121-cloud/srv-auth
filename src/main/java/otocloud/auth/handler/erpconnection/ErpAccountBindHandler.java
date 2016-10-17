package otocloud.auth.handler.erpconnection;

import com.google.inject.Injector;
import io.vertx.core.Future;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import otocloud.auth.common.MessageFinder;
import otocloud.auth.handler.messagechecker.ErpAccountBindChecker;
import otocloud.auth.service.AccountService;
import otocloud.common.ActionURI;
import otocloud.framework.core.HandlerDescriptor;
import otocloud.framework.core.OtoCloudBusMessage;
import otocloud.framework.core.OtoCloudComponentImpl;
import otocloud.framework.core.OtoCloudEventHandlerImpl;

/**
 * 绑定ERP账户.
 * 当121用户与ERP账户绑定后,用户便可以登录ERP.
 * <p>
 * zhangyef@yonyou.com on 2016-01-20.
 */
public class ErpAccountBindHandler extends OtoCloudEventHandlerImpl<JsonObject> {
    private Injector injector;
    private AccountService accountService;
    private ErpAccountBindChecker checker = new ErpAccountBindChecker();

    public ErpAccountBindHandler(OtoCloudComponentImpl component, Injector injector) {
        super(component);
        this.injector = injector;
    }

    @Override
    public void handle(OtoCloudBusMessage<JsonObject> msg) {
        MessageFinder finder = new MessageFinder(msg);

        JsonObject content = finder.getContent();

        if (!checker.check(content)) {
            msg.fail(-1, "请求的数据格式不正确. 格式如下: " + checker.getFormat().toString());
            return;
        }

        int acctId = finder.getAcctId();
        int userId = finder.getUserId();

        String erpUserCode = content.getString("erp_usercode");
        String erpPassword = content.getString("erp_password");

        Future<Boolean> bindFuture = Future.future();

        AccountService service = getAccountService();
        service.bindWithErp(acctId, userId, erpUserCode, erpPassword, bindFuture);

        bindFuture.setHandler(ret -> {
            if (ret.failed()) {
                msg.fail(-1, "绑定ERP账户失败, 请检查ERP用户名和ERP密码是否正确.");
                return;
            }
            if (ret.result()) {
                msg.reply(new JsonObject().put("isBound", true));
                return;
            }

            msg.fail(-1, "绑定ERP账户失败.");
        });
    }

    private AccountService getAccountService() {
        if (accountService == null) {
            accountService = injector.getInstance(AccountService.class);
        }

        return accountService;
    }

    /**
     * <服务名>.<组件名>.users.bind
     *
     * @return
     */
    @Override
    public String getEventAddress() {
        return "users.bind";
    }

    /**
     * post /api/"服务名"/"组件名"/users/bind
     *
     * @return /api/otocloud-auth/erp-connection/users/bind
     */
    @Override
    public HandlerDescriptor getHanlderDesc() {
        HandlerDescriptor handlerDescriptor = super.getHanlderDesc();
        ActionURI uri = new ActionURI("users/bind", HttpMethod.POST);
        handlerDescriptor.setRestApiURI(uri);
        return handlerDescriptor;
    }
}

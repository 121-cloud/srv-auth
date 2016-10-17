package otocloud.auth.handler;

import io.vertx.core.json.JsonObject;
import otocloud.auth.verticle.UserComponent;
import otocloud.framework.core.OtoCloudBusMessage;
import otocloud.framework.core.OtoCloudComponentImpl;
import otocloud.framework.core.OtoCloudEventHandlerImpl;

/**
 * 不提供REST API,只提供事件总线的接口
 * zhangyef@yonyou.com on 2015-11-13.
 */
public class RestFilterHandler extends OtoCloudEventHandlerImpl<JsonObject> {

    public RestFilterHandler(OtoCloudComponentImpl componentImpl) {
        super(componentImpl);
    }

    @Override
    public void handle(OtoCloudBusMessage<JsonObject> event) {

    }

    /**
     * 事件总线地址.
     *
     * @return "服务名"."组件名".users.url.filter
     */
    @Override
    public String getEventAddress() {
        return UserComponent.MANAGE_USER_ADDRESS + ".uri.filter";
    }
}

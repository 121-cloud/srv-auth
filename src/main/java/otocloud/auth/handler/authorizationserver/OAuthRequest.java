package otocloud.auth.handler.authorizationserver;

import io.vertx.core.json.JsonObject;
import otocloud.common.webserver.MessageBodyConvention;
import otocloud.framework.core.OtoCloudBusMessage;

/**
 * zhangyef@yonyou.com on 2015-11-13.
 */
public class OAuthRequest {
    static OAuthRequestWrapper create(OtoCloudBusMessage<JsonObject> message){
        OAuthRequestWrapper request = new OAuthRequestWrapper();
        request.setHeaders(message.headers());
        JsonObject params  = message.body().getJsonObject(MessageBodyConvention.HTTP_QUERY);
        request.setParams(params);

        return request;
    }
}

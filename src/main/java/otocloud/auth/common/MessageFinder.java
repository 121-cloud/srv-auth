package otocloud.auth.common;

import io.vertx.core.json.JsonObject;
import otocloud.common.webserver.MessageBodyConvention;
import otocloud.framework.core.OtoCloudBusMessage;

/**
 * 从事件总线的消息中提取需要的信息.
 * <p>
 * zhangyef@yonyou.com on 2016-01-21.
 */
public class MessageFinder {

    private OtoCloudBusMessage<JsonObject> msg;
    private JsonObject body;
    private JsonObject session;

    public MessageFinder(OtoCloudBusMessage<JsonObject> msg){
        this.msg = msg;
        this.body = msg.body();
        this.session = getSession();
    }

    public int getAcctId(){
        return session.getInteger(MessageBodyConvention.SESSION_ACCT_ID);
    }

    public int getUserId(){
        return session.getInteger(MessageBodyConvention.SESSION_USER_ID);
    }

    public JsonObject getSession(){
        return body.getJsonObject(MessageBodyConvention.SESSION);
    }

    /**
     *
     * @return HTTP请求体
     */
    public JsonObject getContent(){
        return body.getJsonObject(MessageBodyConvention.HTTP_BODY);
    }

}

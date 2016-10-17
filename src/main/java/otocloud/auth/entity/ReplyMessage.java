package otocloud.auth.entity;

import io.vertx.core.json.JsonObject;

/**
 * Created by zhangye on 15/10/8.
 */
public class ReplyMessage {
    private static final String ERR_CODE = "errCode";
    private static final String ERR_MSG = "errMsg";
    private static final String REPLY_DATA = "data";
    private JsonObject message = new JsonObject();

    public static ReplyMessage success() {
        ReplyMessage replyMessage = new ReplyMessage();

        replyMessage.setErrCode(0);
        replyMessage.setErrMsg("OK");

        return replyMessage;
    }

    public static ReplyMessage failure(){
        ReplyMessage replyMessage = new ReplyMessage();

        replyMessage.setErrCode(1);
        replyMessage.setErrMsg("Fail");

        return replyMessage;
    }

    public ReplyMessage setErrCode(int errCode) {
        message.put(ERR_CODE, errCode);
        return this;
    }

    public ReplyMessage setErrMsg(String errMsg) {
        message.put(ERR_MSG, errMsg);
        return this;
    }

    public ReplyMessage setReplyData(JsonObject data) {
        message.put(REPLY_DATA, data);
        return this;
    }

    /**
     * 转换为JSON字符串.
     *
     * @return JSON字符串.
     */
    @Override
    public String toString() {
        return message.toString();
    }

    public JsonObject getMessage() {
        return message;
    }
}

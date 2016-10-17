package otocloud.auth.entity;

/**
 * Created by better/zhangye on 15/9/29.
 */
public class ReplyCodeMessage extends JsonableAdapter {

    private String errCode;
    private String errMsg;

    public static ReplyCodeMessage success(){
        ReplyCodeMessage msg = new ReplyCodeMessage();
        msg.setErrCode(0);
        msg.setErrMsg("ok");

        return msg;
    }

    public static ReplyCodeMessage fail(){
        ReplyCodeMessage msg = new ReplyCodeMessage();
        msg.setErrCode(-1);
        msg.setErrMsg("fail");

        return msg;
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public void setErrCode(int errCode) {
        this.errCode = Integer.toString(errCode);
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }


}

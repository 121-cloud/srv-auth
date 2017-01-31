package otocloud.auth.common;

/**
 * Created by zhangye on 2015-10-16.
 */
public enum ErrCode {
    SUCCESS(0, "Ok"),
    FAIL(1, "Fail"),
    BUS_MSG_FORMAT_ERR(1, "Fail"),
    INVALID_USERNAME_OR_PASSWORD(2, "无效的用户名或密码"),
    DUPLICATED_CELL_NO(3, "重复的手机号"),
    DEFAULT(-1, "Unknown Error");

    private int code;
    private String message;

    ErrCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }
}

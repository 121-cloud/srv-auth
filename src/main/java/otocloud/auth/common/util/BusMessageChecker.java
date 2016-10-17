package otocloud.auth.common.util;

import com.google.inject.Singleton;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import otocloud.auth.common.validator.Required;
import otocloud.auth.common.validator.ViolationMessageBuilder;

import javax.validation.ConstraintViolationException;
import javax.validation.executable.ValidateOnExecution;

/**
 * Created by zhangye on 2015-10-16.
 */
@Singleton
public class BusMessageChecker {

    private static void triggerCheckLoginInfo(
            @Required(value = {"password"},
                    choices = {"userName", "cellNo"}) JsonObject msg) {
    }

    public void triggerCheckQueryUsersByPage(
            @Required({"page_index", "page_size", "department_id"})JsonObject msg){}

    /**
     * 定义消息的content字段，触发验证操作.
     *
     * @param msg
     */
    public void triggerCheckContent(@Required({"content"}) JsonObject msg) {
        //do nothing.
    }

    /**
     * 定义消息格式的约束，触发验证操作.
     *
     * @param msg
     */
    private static void triggerCheckCreateUserInfo(
            @Required({"name", "password", "org_acct_id", "cell_no", "email"}) JsonObject msg) {
        //do nothing.
    }

    /**
     * 检查消息总线是否符合"创建用户"的格式要求。
     *
     * @param msg        事件总线消息.
     * @param errHandler 错误处理.
     * @return true, 符合格式要求; false, 违反格式要求.
     */
    public  boolean checkCreateUserInfo(JsonObject msg, Handler<String> errHandler) {

        try {
            triggerCheckContent(msg);
            triggerCheckCreateUserInfo(msg.getJsonObject("content"));
            return true;
        } catch (ConstraintViolationException e) {
            if (errHandler != null) {
                errHandler.handle(ViolationMessageBuilder.build(e));
            }

            return false;
        }
    }

    /**
     * 当删除用户时,检查请求的数据格式.
     *
     * @param msg
     * @param errHandler
     * @return
     */
    public static boolean checkDeleteUserInfo(JsonObject msg, Handler<String> errHandler) {
        try {
            triggerCheckQueryParams(msg);
            triggerUpdateUserInfo(msg.getJsonObject("queryParams")); //与更新用户数据的必含字段相同.
            return true;
        } catch (ConstraintViolationException e) {
            if (errHandler != null) {
                errHandler.handle(ViolationMessageBuilder.build(e));
            }

            return false;
        }
    }

    public static boolean checkQueryCellNo(JsonObject msg, Handler<String> errMsgHandler) {
        try {
            triggerCheckQueryParams(msg);
            triggerQueryCellNo(msg.getJsonObject("queryParams"));
            return true;
        } catch (ConstraintViolationException e) {
            if (errMsgHandler != null) {
                errMsgHandler.handle(ViolationMessageBuilder.build(e));
            }

            return false;
        }
    }

    public  boolean checkUpdateUserInfo(JsonObject msg, Handler<String> errHandler) {
        try {
            triggerCheckContent(msg);
            triggerCheckQueryParams(msg);
            triggerUpdateUserInfo(msg.getJsonObject("queryParams"));
            return true;
        } catch (ConstraintViolationException e) {
            if (errHandler != null) {
                errHandler.handle(ViolationMessageBuilder.build(e));
            }

            return false;
        }
    }

    public boolean checkQueryUsersByPage(JsonObject msg, Handler<String> errHandler){
        try{
            triggerCheckContent(msg);
            triggerCheckQueryUsersByPage(msg.getJsonObject("content"));
            return true;
        }catch (ConstraintViolationException e) {
            if (errHandler != null) {
                errHandler.handle(ViolationMessageBuilder.build(e));
            }

            return false;
        }
    }

    /**
     * 验证修改用户信息时必选的URL参数。
     *
     * @param queryParams
     */
    private static void triggerUpdateUserInfo(@Required({"openId"}) JsonObject queryParams) {

    }

    /**
     * 验证查询手机号是否已经被注册时,必须包含的手机号参数.
     *
     * @param queryParams HTTP请求的查询参数.
     */
    private static void triggerQueryCellNo(@Required({"cellNo"}) JsonObject queryParams) {
    }

    /**
     * 消息格式中需要具有queryParams字段。
     *
     * @param msg
     */
    private static void triggerCheckQueryParams(@Required({"queryParams"}) JsonObject msg) {

    }

    /**
     * 检查消息总线是否符合"用户登录"的格式要求。
     *
     * @param msg
     * @param errHandler
     * @return
     */
    public static boolean checkLoginInfo(JsonObject msg, Handler<String> errHandler) {

        try {
            triggerCheckLoginInfo(msg);
            return true;
        } catch (ConstraintViolationException e) {
            if (errHandler != null) {
                errHandler.handle(ViolationMessageBuilder.build(e));
            }

            return false;
        }
    }
}

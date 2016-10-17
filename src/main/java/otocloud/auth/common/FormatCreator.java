package otocloud.auth.common;

import io.vertx.core.json.JsonObject;

/**
 * 构建各个接口的消息格式.
 * zhangyef@yonyou.com on 2016-01-20.
 */
public class FormatCreator {
    public static JsonObject erpAccountBindFormat(){
        JsonObject format = new JsonObject();
        format.put("erp_usercode", "");
        format.put("erp_password", "");

        return format;
    }

    public static JsonObject userCreationFormat(){
        JsonObject format = new JsonObject();
        format.put("user_name", "");
        format.put("cell_no", "");
        format.put("email", "");
        return format;
    }
}

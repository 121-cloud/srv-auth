package otocloud.auth.common;

import io.vertx.core.json.JsonObject;

import java.util.Set;

/**
 * zhangyef@yonyou.com on 2016-01-20.
 */
public class FormatCheckerDefaultImpl implements FormatChecker {
    private String errMessage;

    /**
     * 如果格式为空,则不验证格式.
     *
     * @param content 被验证的内容.
     * @return
     */
    @Override
    public boolean check(JsonObject content) {

        boolean passed = true;
        JsonObject format = getFormat();
        if (format == null) {
            return true;
        }

        StringBuilder stringBuilder = new StringBuilder();

        Set<String> fields = format.fieldNames();
        for(String field: fields){
            if(!content.containsKey(field)){
                stringBuilder.append("缺少字段: " + field + System.lineSeparator());
                passed = false;
            }
        }

        errMessage = stringBuilder.toString();

        return passed;
    }

    @Override
    public JsonObject getFormat() {
        return null;
    }

    @Override
    public String getErrMessage() {
        return errMessage;
    }
}

package otocloud.auth.common;

import io.vertx.core.json.JsonObject;

/**
 * 检查 JSON 的消息格式.
 * zhangyef@yonyou.com on 2016-01-20.
 */
public interface FormatChecker {
    /**
     * 验证消息格式.
     *
     * @param content 被验证的内容.
     * @return true, 验证成功. false, 验证失败.
     */
    boolean check(JsonObject content);

    /**
     * 获得被验证的消息格式.
     *
     * @return
     */
    JsonObject getFormat();

    /**
     * 检验错误时的消息.
     *
     * @return
     */
    String getErrMessage();
}

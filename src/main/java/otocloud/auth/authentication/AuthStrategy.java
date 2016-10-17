package otocloud.auth.authentication;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

/**
 * 不同登录策略,
 * 使用121账户登录;
 * 使用ERP账户登录.
 * zhangyef@yonyou.com on 2015-12-24.
 */
public interface AuthStrategy {
    /**
     * 异步登录接口.
     * @param loginInfo
     * @param loginFuture
     */
    void login(JsonObject loginInfo, Future<JsonObject> loginFuture);

    void logout(JsonObject logoutInfo, Future<JsonObject> logoutFuture);
}

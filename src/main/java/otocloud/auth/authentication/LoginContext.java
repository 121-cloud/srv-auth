package otocloud.auth.authentication;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

/**
 * zhangyef@yonyou.com on 2015-12-24.
 */
public class LoginContext {

    //登录策略
    AuthStrategy strategy;

    //登录详细信息
    JsonObject loginInfo;

    //登出时需要的信息
    JsonObject logoutInfo;

    public JsonObject getLoginInfo() {
        return loginInfo;
    }

    public void setLoginInfo(JsonObject loginInfo) {
        this.loginInfo = loginInfo;
    }

    public JsonObject getLogoutInfo() {
        return logoutInfo;
    }

    public void setLogoutInfo(JsonObject logoutInfo) {
        this.logoutInfo = logoutInfo;
    }

    public AuthStrategy getStrategy() {
        return strategy;
    }

    public void setStrategy(AuthStrategy strategy) {
        this.strategy = strategy;
    }

    /**
     * 执行异步登录.
     *
     * @param loginFuture
     */
    public void login(Future<JsonObject> loginFuture) {
        this.strategy.login(loginInfo, loginFuture);
    }

    public void logout(Future<JsonObject> logoutFuture){
        this.strategy.logout(logoutInfo, logoutFuture);
    }
}

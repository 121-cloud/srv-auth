package otocloud.auth.common.route;

import io.vertx.core.Handler;

/**
 * 符合某个路由条件的具体路由.
 * Created by zhangye on 2015-10-13.
 *
 * @param <Condition> 路由条件
 */
public interface OtoRoute<Condition> {
    OtoRouter router();
    OtoRoute<Condition> order(int order);
    boolean matches(Condition condition, boolean failure);
    OtoRoute<Condition> handler(Handler<OtoRoutingContext> handler);
    OtoRoute<Condition> failureHandler(Handler<OtoRoutingContext> handler);
}

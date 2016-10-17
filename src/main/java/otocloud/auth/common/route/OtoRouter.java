package otocloud.auth.common.route;

import io.vertx.core.Handler;

/**
 * Created by zhangye on 2015-10-13.
 */
public interface OtoRouter<Condition, Content> {
    OtoRoute<Condition> route(Condition condition);
    OtoRoute<Condition> route(String uri, String method);
    Handler<Throwable> exceptionHandler();
    void accept(Content content);

    OtoRouter exceptionHandler(Handler<Throwable> exceptionHandler);
    void handleContext(OtoRoutingContext context);
    void handleFailure(OtoRoutingContext context);
}

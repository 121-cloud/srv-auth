package otocloud.auth.common.route;

import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

import java.util.Set;

/**
 * Created by zhangye on 2015-10-13.
 */
public class OtoUriRoute implements OtoRoute<JsonObject> {
    protected Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    private OtoUriRouter router;
    private int order;
    private boolean added;
    private JsonObject condition;

    private Handler<OtoRoutingContext> contextHandler;
    private Handler<OtoRoutingContext> failureHandler;

    public OtoUriRoute(OtoUriRouter router, int order, JsonObject condition) {
        this.router = router;
        this.order = order;
        this.condition = condition;
    }

    public int order() {
        return this.order;
    }

    @Override
    public OtoRouter router() {
        return this.router;
    }

    @Override
    public synchronized OtoRoute<JsonObject> order(int order) {
        if (added) {
            throw new IllegalStateException("Can't change order after route is active");
        }
        this.order = order;
        return this;
    }

    @Override
    public synchronized boolean matches(JsonObject s, boolean failure) {
        if (failure && failureHandler == null || !failure && contextHandler == null) {
            return false;
        }

        Set<String> fields = this.condition.fieldNames();
        for (String name:fields){
            if(condition.getValue(name).equals(s.getValue(name)) == false){
                return false;
            }
        }
        return true;
    }

    @Override
    public synchronized OtoRoute<JsonObject> handler(Handler<OtoRoutingContext> contextHandler) {
        if (this.contextHandler != null) {
            logger.warn("Setting handler for a route more than once!");
        }
        this.contextHandler = contextHandler;
        checkAdd();
        return this;
    }

    private void checkAdd() {
        if (!added) {
            router.add(this);
            added = true;
        }
    }

    @Override
    public synchronized OtoRoute<JsonObject> failureHandler(Handler<OtoRoutingContext> exceptionHandler) {
        if (this.failureHandler != null) {
            logger.warn("Setting failureHandler for a route more than once!");
        }
        this.failureHandler = exceptionHandler;
        checkAdd();
        return this;
    }

    synchronized void handleContext(OtoRoutingContext context) {
        if (contextHandler != null) {
            contextHandler.handle(context);
        }
    }

    synchronized void handleFailure(OtoRoutingContext context) {
        if (failureHandler != null) {
            failureHandler.handle(context);
        }
    }


}

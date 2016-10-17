package otocloud.auth.common.route;

import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by zhangye on 2015-10-13.
 */
public class OtoUriRoutingContext implements OtoRoutingContext<JsonObject> {
    private final OtoUriRouter router;
    protected Logger logger = LoggerFactory.getLogger(this.getClass().getName());
    protected OtoRoute currentRoute;
    protected JsonObject condition;
    private Map<String, Object> data;
    private Throwable failure;
    private int statusCode = -1;
    private Iterator<OtoUriRoute> iter;
    private Message<JsonObject> message;

    public OtoUriRoutingContext(OtoUriRouter router, JsonObject condition, Message<JsonObject> message,
                                Iterator<OtoUriRoute> iter) {
        this.router = router;
        this.condition = condition;
        this.iter = iter;
        this.message = message;
    }

    @Override
    public void next() {
        if (!iterateNext()) {
            logger.warn("没有更过匹配的路由了.");
        }
    }

    @Override
    public boolean failed() {
        return failure != null || statusCode != -1;
    }

    @Override
    public Message<JsonObject> message() {
        return message;
    }

    @Override
    public Throwable failure() {
        return failure;
    }

    @Override
    public void fail(int statusCode) {
        this.statusCode = statusCode;
        doFail();
    }

    @Override
    public void fail(Throwable t) {
        this.failure = t;
        doFail();
    }

    @Override
    public OtoRoutingContext<JsonObject> put(String key, Object obj) {
        getData().put(key, obj);
        return this;
    }

    private void doFail() {
        this.iter = router.iterator();
        next();
    }

    private Map<String, Object> getData() {
        if (data == null) {
            data = new HashMap<>();
        }
        return data;
    }

    @Override
    public <T> T get(String key) {
        Object obj = getData().get(key);
        return (T) obj;
    }

    @Override
    public Map<String, Object> data() {
        return getData();
    }

    protected boolean iterateNext() {
        boolean failed = failed();
        while (iter.hasNext()) {
            OtoUriRoute route = iter.next();
            if (route.matches(condition, failed)) {
                try {
                    currentRoute = route;
                    if (failed) {
                        route.handleFailure(this);
                    } else {
                        route.handleContext(this);
                    }

                } catch (Throwable t) {
                    if (!failed) {
                        if (logger.isTraceEnabled()) logger.trace("Failing the routing");
                        fail(t);
                    } else {
                        // Failure in handling failure!
                        if (logger.isTraceEnabled()) logger.trace("Failure in handling failure");
                        unhandledFailure(-1, t, route.router());
                    }
                } finally {
                    currentRoute = null;
                }
                return true;
            }
        }

        return failed;
    }

    @Override
    public <Condition> OtoRoute<Condition> currentRoute() {
        return currentRoute;
    }

    protected void unhandledFailure(int statusCode, Throwable failure, OtoRouter router) {
        int code = statusCode != -1 ? statusCode : 500;
        if (failure != null) {
            if (router.exceptionHandler() != null) {
                router.exceptionHandler().handle(failure);
            } else {
                logger.error("Unexpected exception in route", failure);
            }
        } else {
            message().fail(code, "未处理的错误");
        }
    }

}

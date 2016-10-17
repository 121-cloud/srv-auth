package otocloud.auth.common.route;

import io.vertx.core.Handler;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by zhangye on 2015-10-13.
 */
public class OtoUriRouter implements OtoRouter<JsonObject, Message<JsonObject>> {
    private final AtomicInteger orderSequence = new AtomicInteger();
    private final Set<OtoUriRoute> routes =
            new ConcurrentSkipListSet<>((OtoUriRoute o1, OtoUriRoute o2) -> Integer.compare(o1.order(), o2.order()));
    protected Logger logger = LoggerFactory.getLogger(this.getClass().getName());
    private Handler<Throwable> exceptionHandler;

    @Override
    public OtoRoute<JsonObject> route(JsonObject condition) {
        return new OtoUriRoute(this, orderSequence.getAndIncrement(), condition);
    }

    public OtoRoute<JsonObject> route(String uri, String method){
        JsonObject condition = new JsonObject();
        condition.put("uri", uri).put("method", method);

        return route(condition);
    }


    @Override
    public Handler<Throwable> exceptionHandler() {
        return exceptionHandler;
    }

    @Override
    public void accept(Message<JsonObject> message) {
        JsonObject body = message.body();
        new OtoUriRoutingContext(this, body.getJsonObject("api"), message, routes.iterator()).next();
    }

    @Override
    public synchronized OtoRouter exceptionHandler(Handler<Throwable> exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
        return this;
    }

    @Override
    public void handleContext(OtoRoutingContext context) {
        throw new NotImplementedException();
    }

    @Override
    public void handleFailure(OtoRoutingContext context) {
        throw new NotImplementedException();
    }

    void add(OtoUriRoute route) {
        routes.add(route);
    }

    Iterator<OtoUriRoute> iterator() {
        return routes.iterator();
    }
}

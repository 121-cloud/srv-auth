package otocloud.auth.common.route;

import io.vertx.core.eventbus.Message;

import java.util.Map;

/**
 * Created by zhangye on 2015-10-13.
 */
public interface OtoRoutingContext<MessageBody> {
    void next();

    boolean failed();

    Message<MessageBody> message();

    Throwable failure();

    /**
     * Fail the context with the specified status code.
     * <p/>
     * This will cause the router to route the context to any matching failure handlers for the request. If no failure handlers
     * match a default failure response will be sent.
     *
     * @param statusCode the HTTP status code
     */
    void fail(int statusCode);

    /**
     * Fail the context with the specified throwable.
     * <p/>
     * This will cause the router to route the context to any matching failure handlers for the request. If no failure handlers
     * match a default failure response with status code 500 will be sent.
     *
     * @param throwable a throwable representing the failure
     */
    void fail(Throwable throwable);

    /**
     * Put some arbitrary data in the context. This will be available in any handlers that receive the context.
     *
     * @param key the key for the data
     * @param obj the data
     * @return a reference to this, so the API can be used fluently
     */
    OtoRoutingContext<MessageBody> put(String key, Object obj);

    /**
     * Get some data from the context. The data is available in any handlers that receive the context.
     *
     * @param key the key for the data
     * @param <T> the type of the data
     * @return the data
     * @throws java.lang.ClassCastException if the data is not of the expected type
     */
    <T> T get(String key);

    /**
     * @return all the context data as a map
     */
    Map<String, Object> data();

    <Condition> OtoRoute<Condition> currentRoute();
}

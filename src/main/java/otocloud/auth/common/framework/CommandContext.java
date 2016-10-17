package otocloud.auth.common.framework;

import io.vertx.core.json.JsonObject;

/**
 * Created by zhangye on 2015-10-08.
 */
public class CommandContext implements IContext<String, Object> {
    private JsonObject context;

    public CommandContext() {
        context = new JsonObject();
    }

    @Override
    public void put(String key, Object value) {
        context.put(key, value);
    }

    @Override
    public Object get(String key) {
        return context.getValue(key);
    }

    @Override
    public JsonObject getJson(String key) {
        return context.getJsonObject(key);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getValue(String key) {
        return (T) get(key);
    }

    @Override
    public boolean isEmpty() {
        return context.isEmpty();
    }
}

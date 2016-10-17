package otocloud.auth.common.framework;

import io.vertx.core.json.JsonObject;

/**
 * Created by zhangye on 2015-10-08.
 */
public interface IContext<K, V> {
    void put(K key, V value);
    V get(K key);
    JsonObject getJson(K key);
    <T> T getValue(K key);

    boolean isEmpty();
}

package otocloud.auth.common.util;

import java.util.HashMap;
import java.util.Map;

/**
 * zhangyef@yonyou.com on 2015-12-02.
 */
public enum GlobalDataPool {
    INSTANCE;

    private Map<String, Object> contextData = new HashMap<>();

    public void put(String key, Object value) {
        contextData.put(key, value);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String key) {
        return (T)contextData.get(key);
    }
}

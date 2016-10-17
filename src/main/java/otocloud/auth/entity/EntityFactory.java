package otocloud.auth.entity;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.vertx.core.json.JsonObject;

import java.io.IOException;

/**
 * Created by better/zhangye on 15/9/28.
 */
public class EntityFactory {

    private static ObjectMapper mapper = new ObjectMapper();

    /**
     * 从Json生成Java对象。
     * @param json
     * @param <T> 被生成的Java对象的类型。
     * @return
     * @throws IOException
     */
    public static <T> T fromJson(String json, Class<T> tClass) throws IOException {
        T objectAdapter = mapper.readValue(json, tClass);
        return objectAdapter;
    }

    public static <T> T fromJsonObject(JsonObject object, Class<T> tClass) throws IOException {
        return fromJson(object.toString(), tClass);
    }
}

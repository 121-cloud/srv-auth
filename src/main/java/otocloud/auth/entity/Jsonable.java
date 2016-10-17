package otocloud.auth.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.vertx.core.json.JsonObject;

/**
 * 可以转换为Json对象的接口。
 * 所有需要提供向Json对象转换功能的实体类都可以实现该接口。
 * <p/>
 * Created by better/zhangye on 15/9/28.
 */
public interface Jsonable {

    String toJson() throws JsonProcessingException;

    JsonObject toJsonObject() throws JsonProcessingException;
}

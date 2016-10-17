package otocloud.auth.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

/**
 * Created by better/zhangye on 15/9/28.
 */
public class JsonableAdapter implements Jsonable {
    protected Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    private static ObjectMapper mapper = new ObjectMapper();
    static{
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL); //空字段不显示
    }


    @Override
    public String toJson() throws JsonProcessingException {
        return mapper.writeValueAsString(this);
    }

    @Override
    public JsonObject toJsonObject() throws JsonProcessingException {
        return new JsonObject(toJson());
    }

    @Override
    public String toString() {
        String body = null;
        try{
            body = toJson();
            return body;
        }catch (JsonProcessingException e){
            logger.info("无法转换为JSON字符串，使用默认toString()方法");
        }
        return super.toString();
    }
}

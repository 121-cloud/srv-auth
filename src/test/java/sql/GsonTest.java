package sql;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.vertx.core.json.JsonObject;
import org.junit.Ignore;
import org.junit.Test;
import otocloud.auth.mybatis.entity.AuthUser;

import java.util.UUID;

/**
 * zhangyef@yonyou.com on 2015-11-06.
 */
@Ignore("测试Gson用法.")
public class GsonTest {
    @Test
    public void print(){
        String uuid = UUID.randomUUID().toString();
        System.out.println(uuid);
    }

    @Test
    public void testFieldNamingPolicy(){
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
        AuthUser user = new AuthUser();
        user.setCellNo("123");
        user.setEmail("2@yonyou.com");
        user.setId(2);

        String str = gson.toJson(user);
        JsonObject jsonObject = new JsonObject(str);
        System.out.println(jsonObject.toString());

        jsonObject.put("id", 3);
        jsonObject.put("cell_no", "3@yonyou.com");

        AuthUser fromDB = gson.fromJson(jsonObject.toString(), AuthUser.class);
        System.out.println(fromDB.getCellNo());
    }
}

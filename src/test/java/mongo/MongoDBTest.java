package mongo;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

/**
 * zhangyef@yonyou.com on 2015-10-28.
 */
@RunWith(VertxUnitRunner.class)
public class MongoDBTest {
    String collection = "UsersOnline";
    private Vertx vertx;
    private MongoClient client;

    @Before
    public void setUp(TestContext context) {
        final Async async = context.async();

        vertx = Vertx.vertx();
        JsonObject config = new JsonObject();
        config.put("host", "10.10.23.112");
//        config.put("host", "localhost");
        config.put("port", 27017);
        config.put("db_name", "otocloud-auth");
        client = MongoClient.createShared(vertx, config);

        async.complete();
    }

    @After
    public void tearDown(TestContext context) {
        vertx.close(context.asyncAssertSuccess());
    }

    @Test
    public void it_should_get_current_user_info(TestContext context) {
        final Async async = context.async();

        JsonObject userInfo = new JsonObject();
        userInfo.put("current_user_id", 1);

        client.findOne(collection, userInfo, new JsonObject(), ret -> {
            if(ret.succeeded()){
                JsonObject found = ret.result();

                context.assertEquals(1, found.getInteger("current_user_id"));

                async.complete();
            }
        });
    }

    @Test
    public void it_should_add_a_user_info(TestContext context) {
        final Async async = context.async();

        JsonObject document = new JsonObject();
        document.put("current_user_id", 1);

        JsonObject info = new JsonObject();
        info.put("infoId", 2);
        info.put("time", Instant.now());
        document.put("info", info);

        JsonObject user1 = new JsonObject();
        user1.put("openId", UUID.randomUUID().toString());
        user1.put("userId", 2);
        document.put("queryUsers", new JsonArray().add(user1));

        client.insert(collection, document, ret -> {
            if (ret.succeeded()) {
                String result = ret.result();
                System.out.println(result);

                async.complete();
            }
        });
    }

    @Test
    public void it_should_remove_a_user_online(TestContext context){
        final Async async = context.async();
        JsonObject condition = new JsonObject();
        condition.put("user_open_id", "3badecf3-3eed-442d-97c6-4b6246539f13");

        client.remove(collection, condition, ret -> {
            if(ret.succeeded()){
                async.complete();
            }else{
                context.fail();
            }
        });
    }

    @Test
    public void it_should_print_now(){

        LocalDateTime ldt = LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault());
        System.out.println(ldt.toString());
    }

}

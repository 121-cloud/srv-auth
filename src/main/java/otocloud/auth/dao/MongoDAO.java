package otocloud.auth.dao;

import com.google.inject.Singleton;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.mongo.MongoClientDeleteResult;
import otocloud.auth.common.util.GlobalDataPool;

import javax.inject.Inject;

/**
 * zhangyef@yonyou.com on 2015-10-28.
 */
@Singleton
public class MongoDAO {
    private final String HOST = "10.10.23.112";
    private final int PORT = 27017;
    private final String DB_NAME = "otocloud-auth";
    private final MongoClient client;
    private String USERS_ONLINE = "UsersOnline";
    /**
     * 存储需要激活的用户ID
     */
    public static final String USERS_ACTIVATION = "UsersActivation";

    public String USERS_OPEN_ID = "UsersOpenIDMap";


    @Inject
    public MongoDAO(Vertx vertx) {
        JsonObject mongo_client = GlobalDataPool.INSTANCE.<JsonObject>get("mongo_client_at_auth_service");
        JsonObject config;

        if (mongo_client != null) {
            config = mongo_client;
        } else {
            config = new JsonObject();
            config.put("host", HOST);
            config.put("port", PORT);
        }

        config.put("db_name", DB_NAME);

        client = MongoClient.createNonShared(vertx, config);
    }

    public void insert(JsonObject data, Handler<AsyncResult<String>> resultHandler) {
        client.insert(USERS_ONLINE, data, resultHandler);
    }

    public void insert(String collection, JsonObject data, Handler<AsyncResult<String>> resultHandler){
        client.insert(collection, data, resultHandler);
    }


    public void findOne(JsonObject query, Handler<AsyncResult<JsonObject>> resultHandler) {
        client.findOne(USERS_ONLINE, query, new JsonObject(), resultHandler);
    }

    public void findOne(String collection, JsonObject query, Handler<AsyncResult<JsonObject>> resultHandler){
        client.findOne(collection, query, new JsonObject(), resultHandler);
    }

    public void delete(String collection, JsonObject condition, Handler<AsyncResult<MongoClientDeleteResult>> resultHandler){
        client.removeDocument(collection, condition, resultHandler);
    }

    /**
     * 移除符合条件的所有用户数据.
     *
     * @param condition
     * @param resultHandler
     */
    public void delete(JsonObject condition, Handler<AsyncResult<MongoClientDeleteResult>> resultHandler) {
        client.removeDocument(USERS_ONLINE, condition, resultHandler);
    }
}

package otocloud.auth.authprovider;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.User;
import io.vertx.ext.jdbc.JDBCClient;

/**
 * Created by better/zhangye on 15/9/30.
 */
public class OtocloudAuthImpl implements OtocloudAuth {
    JDBCClient jdbcClient;

    public OtocloudAuthImpl(JDBCClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public void authenticate(JsonObject authInfo, Handler<AsyncResult<User>> resultHandler) {

    }
}

package otocloud.auth.authprovider;

import io.vertx.ext.auth.AuthProvider;
import io.vertx.ext.jdbc.JDBCClient;

/**
 * Created by better/zhangye on 15/9/30.
 */
public interface OtocloudAuth extends AuthProvider {
    static OtocloudAuth create(JDBCClient jdbcClient){
        return new OtocloudAuthImpl(jdbcClient);
    }
}

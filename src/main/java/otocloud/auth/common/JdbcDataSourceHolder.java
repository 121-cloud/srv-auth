package otocloud.auth.common;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import otocloud.persistence.dao.JdbcDataSource;

/**
 * zhangyef@yonyou.com on 2015-11-23.
 */
public class JdbcDataSourceHolder {
    private JdbcDataSource jdbcDataSource;

    public JdbcDataSource getJdbcDataSource() {
        return jdbcDataSource;
    }

    public void setJdbcDataSource(JdbcDataSource jdbcDataSource) {
        this.jdbcDataSource = jdbcDataSource;
    }

    public static JdbcDataSource createDataSource(Vertx vertx, JsonObject datasourceCfg) {
        return JdbcDataSource.createDataSource(vertx, datasourceCfg);
    }

    public void init(Vertx vertx, JsonObject datasourceCfg) {
        getJdbcDataSource().init(vertx, datasourceCfg);
    }

    public void close() {
        getJdbcDataSource().close();
    }

    public JDBCClient getSqlClient() {
        return getJdbcDataSource().getSqlClient();
    }
}

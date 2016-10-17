package otocloud.auth.dao;

import com.google.inject.Inject;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.ResultSet;
import io.vertx.ext.sql.SQLConnection;
import io.vertx.ext.sql.UpdateResult;
import org.apache.commons.lang3.StringUtils;
import otocloud.auth.common.JdbcDataSourceHolder;

import javax.persistence.Column;
import java.lang.reflect.Field;
import java.util.Set;


/**
 * Created by zhangye on 2015-10-15.
 */
public class OtoBaseDAO {
    protected Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    @Inject
    private JdbcDataSourceHolder dataSource;

    protected final void deleteWithParams(String sql, JsonArray params, Future<UpdateResult> done) {
        updateWithParams(sql, params, done);
    }


    /**
     * TODO 添加测试
     * <p>
     * 构造 UPDATE 的 SET 子句.
     *
     * @param values 将要更新的数据.
     * @return "key1=?, key2=?"
     */
    private String makeUpdateSetClause(JsonObject values) {
        StringBuilder builder = new StringBuilder();

        values.forEach(entry -> {
            String key = entry.getKey();
//            String value = entry.getValue().toString();
            builder.append(", ").append(key).append("=").append("?"); //", key=?"
        });

        return builder.toString();
    }

    /**
     * 生成 UPDATE 中 SET 子句中的值.
     *
     * @param set
     * @return
     */
    private JsonArray makeUpdateSetValues(JsonObject set) {
        return makeValuesFromJson(set);
    }

    private JsonArray makeValuesFromJson(JsonObject values) {
        JsonArray params = new JsonArray();
        values.forEach(entry -> {
            params.add(entry.getValue());
        });

        return params;
    }


    /**
     * 在指定数据库表中查找满足条件的记录.
     *
     * @param tableName      数据库表名.
     * @param columns        如果为null或者空数组, 表示查询所有列.
     * @param where          以(columnName, columnValue)方式存储的查询约束条件.
     * @param completeFuture
     */
    protected final void queryBy(String tableName, final String[] columns, final JsonObject where,
                                 Future<ResultSet> completeFuture) {
        String querySQL = "SELECT " + makeSelectColumnClause(columns)
                + " FROM " + tableName
                + " WHERE " + makeWhereConditionClause(where);
        JsonArray params = makeWhereValues(where);
        queryWithParams(querySQL, params, completeFuture);
    }

    /**
     * 取出where子句的参数值。注意，值的排列顺序需要与参数的顺序保持一致。
     *
     * @param where
     * @return
     * @see OtoBaseDAO#makeWhereConditionClause(io.vertx.core.json.JsonObject)
     */
    private JsonArray makeWhereValues(JsonObject where) {

        return makeValuesFromJson(where);
    }

    /**
     * 如果参数为null或者大小为空，则查询所有的列。
     * 返回的结果前后都没有空格.
     *
     * @param columns
     * @return
     */
    protected final String makeSelectColumnClause(String[] columns) {
        StringBuilder builder = new StringBuilder();
        if (columns == null || columns.length == 0) {
            builder.append("*");
        } else {
            int columnNum = columns.length;
            for (int i = 0; i < columnNum; i++) {
                builder.append(columns[i]).append(i + 1 != columnNum ? ", " : "");
            }
        }
        return builder.toString().trim();
    }

    /**
     * @param where
     * @return 输出的格式为“ para1=? AND para2=?”
     */
    protected final String makeWhereConditionClause(JsonObject where) {
        StringBuilder builder = new StringBuilder();
        Set<String> fields = where.fieldNames();
        String[] fieldNames = fields.toArray(new String[fields.size()]);
        int fieldNum = fieldNames.length;
        for (int i = 0; i < fieldNum; i++) {
            builder.append(fieldNames[i] + "=?").append(i + 1 != fieldNum ? " AND " : "");
        }

        return builder.toString();
    }

    protected final void queryWithParams(String sql, JsonArray params, Future<ResultSet> completeFuture) {
        createDBConnect(conn -> {

            conn.queryWithParams(sql, params, ret -> {
                if (ret.succeeded()) {
                    completeFuture.complete(ret.result());
                    closeDBConnect(conn);
                } else {
                    completeFuture.fail(new RuntimeException(ret.cause()));
                }
            });
        });

    }

    /**
     * 更新指定表的数据。
     *
     * @param tableName  数据库表的名称.
     * @param setValues  需要更新的值.
     * @param where      更新子句的约束条件.
     * @param doneFuture 更新结束后调用.
     */
    protected final void updateBy(String tableName, JsonObject setValues, JsonObject where, Future<UpdateResult>
            doneFuture) {
        String updateSQL = "UPDATE " + tableName
                + " SET id=id " + makeUpdateSetClause(setValues)
                + " WHERE " + makeWhereConditionClause(where);

        JsonArray setParams = makeUpdateSetValues(setValues);
        JsonArray whereParams = makeWhereValues(where);

        JsonArray params = new JsonArray().addAll(setParams).addAll(whereParams);

        updateWithParams(updateSQL, params, doneFuture);

    }

    /**
     * 如果sql语句中没有问号表示的参数，则params可以为null。
     *
     * @param sql
     * @param params
     * @param done
     */
    protected final void updateWithParams(String sql, JsonArray params, Future<UpdateResult> done) {
        createDBConnect(conn -> conn.setAutoCommit(false, res -> {
                    if (res.failed()) {
                        done.fail(res.cause());
                        return;
                    }

                    conn.updateWithParams(sql, params, ret -> {
                        if (ret.succeeded()) {
                            // 提交事务
                            conn.commit(cmtRet -> {
                                if (cmtRet.succeeded()) {
                                    try {
                                        done.complete(ret.result());
                                    } finally {
                                        //关闭连接
                                        closeDBConnect(conn);
                                    }
                                } else {
                                    done.fail(cmtRet.cause());
                                }
                            });

                            return;
                        } else {
                            conn.rollback(rbRet -> {
                                done.fail(ret.cause());
                            });
                        }


                    });

                }),
                e -> logger.error("连接数据库错误.", e));
    }

    protected final void createDBConnect(Handler<SQLConnection> connectionHandler) {
        createDBConnect(connectionHandler, e -> {
        });
    }

    protected final void createDBConnect(Handler<SQLConnection> connectedHandler, Handler<Throwable> errHandler) {
        JDBCClient jdbcClient = dataSource.getSqlClient();
        jdbcClient.getConnection(conRes -> {
            if (conRes.succeeded()) {
                final SQLConnection connection = conRes.result();
                connectedHandler.handle(connection);
            } else {
                Throwable err = conRes.cause();
                String errMsg = err.getMessage();
                logger.error(errMsg, err);
                if (errHandler != null) {
                    errHandler.handle(err);
                }
            }
        });
    }

    final protected void closeDBConnect(SQLConnection conn) {
        conn.setAutoCommit(true, ret -> {
            conn.close(handler -> {
                if (handler.failed()) {
                    Throwable conErr = handler.cause();
                    logger.error(conErr.getMessage(), conErr);
                } else {
                    if (logger.isInfoEnabled()) {
                        logger.info("连接关闭成功.");
                    }
                }
            });
        });
    }

    /**
     * 根据实体对象，生成UPDATE语句中的SET子句字符串。
     * <p>
     * 如果merged为true，则实体对象的null属性<strong>会</strong>放到SET子句中。
     * 如果merged为false，则实体对象的null属性<strong>不会</strong>放到SET子句中。
     *
     * @param entity 被处理的实体对象.
     * @param merged 标记对象中的null字段是否放置在SET子句中. true, 与数据库合并空字段; false, 覆盖数据库字段.
     * @param <T>    对象类型.
     * @return SET 子句(不包含SET关键字).
     */
    public final <T> String mapToSetClause(T entity, boolean merged) {
        Class clazz = entity.getClass();

        Field[] fields = clazz.getDeclaredFields();

        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            Column column = field.getAnnotation(Column.class);

            //没有被注解，则不处理.
            if (column == null) {
                continue;
            }
            String setKey = column.name();

            try {
                if (StringUtils.isBlank(setKey)) {
                    setKey = field.getName(); //默认使用字段名
                }
                field.setAccessible(true);
                Object newValue = field.get(entity);
                field.setAccessible(false);

                //值为null，则跳过。
                if (merged && newValue == null) {
                    continue;
                }

                if (i != 0) {
                    //不是第一个，添加分隔符
                    builder.append(", ");
                }

                builder.append(setKey).append("=");
                builder.append(makeSetValue(newValue));

            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return builder.toString();
    }

    /**
     * TODO 处理不同数据类型.
     *
     * @param value
     * @return
     */
    private String makeSetValue(Object value) {
        StringBuilder builder = new StringBuilder();
        if (value == null) {
            return "''";
        }

        if (value instanceof String) {
            builder.append("'").append(value).append("'");
        } else {
            builder.append(value);
        }

        return builder.toString();
    }

}

package otocloud.auth.dao;

import com.google.inject.Singleton;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.sql.ResultSet;
import io.vertx.ext.sql.UpdateResult;
import otocloud.auth.common.util.Mapper;
import otocloud.auth.entity.User;

import java.util.List;

/**
 * 操作用户表的持久层（auth_user表）。
 * <p>
 * Created by better/zhangye on 15/9/29.
 */
@Singleton
public class UserDAO extends OtoBaseDAO {

    public boolean create(User user, Future<User> future) {

        final String insertUserSQL = "INSERT INTO auth_user(" +
                "org_acct_id, name, password, cell_no, email, status, entry_id, entry_datetime) " +
                "VALUES(?, ?, ?, ?, ?, ?, ?, now())";
        JsonArray params = new JsonArray();
        params.add(user.getOrgAcctId());
        params.add(user.getUserName());
        params.add(user.getPassword());
        params.add(user.getCellNo() == null ? "" : user.getCellNo());
        params.add(user.getEmail() == null ? "" : user.getEmail());
        params.add("A"); //status
        params.add(user.getEntryId()); //设置该记录的创建人

        Future<UpdateResult> innerFuture = Future.future();

        updateWithParams(insertUserSQL, params, innerFuture);

        innerFuture.setHandler(result -> {
            if (result.succeeded()) {
                UpdateResult updateResult = result.result();
                int num = updateResult.getUpdated();

                Integer userId = updateResult.getKeys().getInteger(0);
                User storedUser = user;
                storedUser.setID(userId);

                future.complete(storedUser);

                logger.info("更新了" + num + "条记录.");
                logger.info(updateResult.toJson());
            } else {
                future.fail(result.cause());
                logger.warn("AuthService 无法增加新用户." + result.cause().getMessage());
            }
        });


        return true;
    }

    /**
     * 将参数中的值与数据库中的值合并，如果参数中user的字段为空，则不覆盖数据库中的值。
     * 如果不为空，则覆盖数据库中的值。
     *
     * @param user   带有指定的id。
     * @param future
     */
    public void merge(User user, Future<User> future) {
        update(user, future, true);
    }

    public void update(User user, Future<User> future) {
        update(user, future, false);
    }

    /**
     * 使用传递的参数进行数据更新。
     * <p>
     * 只存储被@Column注解标记的属性。
     *
     * @param user   新的用户数据
     * @param future 返回更新后的用户数据
     * @param merge  true, 表示将新的数据合并到原有记录中；false, 表示用新的数据替换原有记录。
     * @see OtoBaseDAO#mapToSetClause(java.lang.Object, boolean)
     */
    public void update(User user, Future<User> future, boolean merge) {
        JsonArray params = new JsonArray();

        String setClause = mapToSetClause(user, merge);

        final String updateSQL = "UPDATE auth_user"
                + " SET " + setClause
                + " WHERE id=?";

        params.add(user.getID());

        Future<UpdateResult> innerFuture = Future.future();

        updateWithParams(updateSQL, params, innerFuture);

        innerFuture.setHandler(result -> {
            if (result.succeeded()) {
                UpdateResult updateResult = result.result();
                System.out.println(updateResult.toJson());
                future.complete(user);
            } else {
                future.fail(result.cause());
                logger.warn("用户信息无法更改。");
            }
        });

    }

    /**
     * 根据参数中的主键，更新有变化的属性字段。如果参数中的属性为空或值不变，则不更新响应字段。
     *
     * @param userId 用户ID，数据库表的主键.
     */
    public void setLoginDateTime(String userId, Future<UpdateResult> future) {
        String updateSQL = "UPDATE auth_user"
                + " SET last_login_datetime=now()"
                + " WHERE id=" + userId;
        updateWithParams(updateSQL, null, future);
    }

    /**
     * 不执行真正的删除操作。
     *
     * @param where  查询条件.
     * @param future 删除成功后回调.
     */
    public void deleteBy(JsonObject where, Future<UpdateResult> future) {
        String deleteSQL = "UPDATE auth_user"
                + " SET delete_id=0, delete_datetime=now()"
                + " WHERE 1=1";
        StringBuilder builder = new StringBuilder();
        builder.append(deleteSQL);

        JsonArray params = new JsonArray();

        where.forEach(member -> {
            String name = member.getKey();
            Object value = member.getValue();

            builder.append(" AND ").append(name);
            builder.append("=");
            builder.append("?");

            params.add(value instanceof Integer ? Integer.parseInt(value.toString()) : value);
        });

        Future<UpdateResult> innerFuture = Future.future();

        deleteWithParams(builder.toString(), params, innerFuture);

        innerFuture.setHandler(ret -> {
            if (ret.succeeded()) {
                UpdateResult result = ret.result();
                int num = result.getUpdated();

                future.complete(result);

                logger.info("删除了" + num + "条记录.");
            } else {
                future.fail(ret.cause());
                logger.warn("记录无法删除: " + ret.cause().getMessage());
            }

        });
    }

    /**
     * 不执行数据删除，仅标记记录为删除状态。
     *
     * @param userId 用户ID.
     * @param future 删除成功后回调.
     */
    public void deleteById(int userId, Future<Boolean> future) {
        Future<UpdateResult> updateResultFuture = Future.future();

        deleteBy(new JsonObject().put("id", userId), updateResultFuture);

        updateResultFuture.setHandler(ret -> {
            if (ret.succeeded()) {
                future.complete(true);
            } else {
                future.fail(ret.cause());
                logger.warn(String.format("ID 为%d的用户表记录无法删除: %s", userId, ret.cause().getMessage()));
            }
        });

    }

    /**
     * 异步根据用户ID查找单条记录。
     *
     * @param id     用户ID.
     * @param future 返回User对象.
     */
    public void findById(int id, Future<User> future) {
        Future<ResultSet> innerFuture = Future.future();

        queryBy("auth_user", new String[]{"id", "org_acct_id", "org_dept_id", "name", "cell_no", "email"}, new
                JsonObject().put("id", id), innerFuture);
        addResultHandler(future, innerFuture);

    }

    private void addResultHandler(Future<User> future, Future<ResultSet> innerFuture) {
        innerFuture.setHandler(result -> {
            if (result.succeeded()) {
                ResultSet resultSet = result.result();
                //如果没有找到用户，说明用户名和密码错误，或用户不存在。
                if (resultSet.getNumRows() == 0) {
                    future.complete();
                    return;
                }

                List<User> users = Mapper.mapToEntity(resultSet, User.class);

                if (users == null) {
                    future.complete();
                } else {
                    future.complete(users.get(0));
                }

            } else {
                future.fail(result.cause());
            }
        });
    }

    /**
     * 根据用户名和密码判断用户是否存在。
     *
     * @param userName 用户名
     * @param password 密码
     * @return User(id, org_acct_id, name)
     */
    public void findBy(String userName, String password, Future<User> future) {
        Future<ResultSet> innerFuture = Future.future();

        queryBy("auth_user", new String[]{"id", "org_acct_id", "name"},
                new JsonObject().put("name", userName).put("password", password),
                innerFuture);

        addResultHandler(future, innerFuture);
    }

    /**
     * 根据手机号和密码查找用户信息.
     *
     * @param cellNo   手机号.
     * @param password 密码
     * @param future   如果无法找到用户,返回null; 如果找到用户, 则返回User实体对象.
     */
    public void findByCellNo(String cellNo, String password, Future<User> future) {
        Future<ResultSet> innerFuture = Future.future();

        queryBy("auth_user", new String[]{"id", "org_acct_id", "name"},
                new JsonObject().put("cell_no", cellNo).put("password", password),
                innerFuture);

        addResultHandler(future, innerFuture);
    }
}

package otocloud.auth.service;

import com.google.inject.ImplementedBy;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import otocloud.auth.common.validator.HasLoginInfo;
import otocloud.auth.entity.User;
import otocloud.auth.mybatis.entity.AuthUser;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by better/zhangye on 15/9/28.
 */
@ImplementedBy(UserServiceImpl.class)
public interface UserService {

    void create(@HasLoginInfo JsonObject userInfo, Future<User> future);

    void addManagerRole(User manager, Future<User> addFuture);

    User update(JsonObject userInfo);

    /**
     * 异步更新接口。
     *
     * @param user
     * @param future
     */
    void update(User user, Future<User> future);

    User findByOpenId(String openId);

    List<User> findAll();

    User delete(JsonObject userInfo);

    User deleteByOpenId(String openId);

    /**
     * 不执行数据删除操作，仅将数据标记为删除。
     *
     * @param userId     用户ID.
     * @param doneFuture 删除成功后回调.
     */
    void deleteById(int userId, Future<User> doneFuture);

    /**
     * 异步登录的接口。
     *
     * @param userName 用户名.
     * @param password 密码.
     * @param future   返回函数结果.
     * @return json结构.
     * <pre>
     *         {
     *              "user_openIid":"OPEN_ID",
     *              "user_name":"USER_NAME",
     *              "access_token":"TOKEN",
     *              "expires_in":"N SECONDS"
     *          }
     * </pre>
     */
    void login(@NotNull(message = "用户名不能为空") String userName, @NotNull(message = "密码不能为空") String password,
               String sessionId, Future<JsonObject> future);

//    void protectSessionIdWithToken(String token, String sessionId);

    /**
     * 根据token查询SessionID.
     *
     * @param token token字符串. 如果字符串的前后包含空格,将被移除.
     */
//    String findSessionId(String token);

    void findSessionId(String token, Future<String> findFuture);

    /**
     * 如果Session已超时被移除,则将SessionID从数据库中移除.
     *
     * @param token
     * @param deleteFuture
     */
    void deleteSessionId(String token, Future<Void> deleteFuture);

    void login(String userName, String password, boolean useCellNo,
               String sessionId, Future<JsonObject> future);

    JsonObject makeUserOnlineShape(AuthUser user, String token, String sessionId);

    void stayOnlineInfo(JsonObject online, Future<Void> future);

    void loginWithCellNo(@NotNull(message = "手机号不能为空") String cellNo, @NotNull(message = "密码不能为空") String password,
                         String sessionId, Future<JsonObject> future);

    void importErpUsers(List<AuthUser> userList, Future<Integer> future);

    /**
     * 异步执行退出。
     *
     * @param userOpenId
     * @param future
     * @return true, 已成功退出; false, 退出失败.
     */
    void logout(String userOpenId, Future<JsonObject> future);

    /**
     * 验证手机号是否已经被注册.
     * 如果参数为null,则抛出异常.
     *
     * @param cellNo 手机号.
     * @param future true, 手机号已经被注册; false, 手机号未被注册.
     */
    void verifyCellNo(@NotNull(message = "手机号不能为空.") String cellNo, Future<Boolean> future);

    /**
     * 分页查询部门下的用户列表.
     * <p>
     * 业务约束:只有企业管理员角色才可以调用该接口.
     *
     * @param acctId     企业账户ID.
     * @param deptId     部门ID.
     * @param pageIndex  页数索引(开始为0).
     * @param pageSize   每页内的数据条数.
     * @param pageFuture 查询结果.
     */
    void getUserListByPage(int acctId, int deptId, int pageIndex, int pageSize, Future<List<AuthUser>> pageFuture);

    /**
     * 同步查询代码.
     * 根据企业账户信息和部门ID,查询全部记录数.
     * <p>
     * 数据约束:数据的delete_datetime是null.
     *
     * @param acctId 企业账户ID.
     * @param deptId 部门ID.
     * @return 用户的数目.
     */
    int countUser(int acctId, int deptId);

    /**
     * 判断用户是否已经和121账户绑定.
     *
     * @param acct_id         企业账户
     * @param user_name       用户名
     * @param connectedFuture 是否连接上
     */
    void beBoundWithErp(int acct_id, String user_name, Future<AuthUser> connectedFuture);

    /**
     * 创建指定账户下的普通用户(业务员)
     *
     * @param acctId       企业账户ID
     * @param managerId    管理员ID
     * @param userName     用户名
     * @param cellNo       手机号
     * @param email        邮箱
     * @param createFuture 返回创建成功的用户.
     */
    void createUser(int acctId, int managerId, String userName, String cellNo, String email,
                    Future<JsonObject> createFuture);
}

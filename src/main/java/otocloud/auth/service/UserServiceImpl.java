package otocloud.auth.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.mongo.MongoClientDeleteResult;
import io.vertx.ext.web.Session;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.jasypt.util.password.StrongPasswordEncryptor;

import otocloud.auth.common.RSAUtil;
import otocloud.auth.common.UserStatus;
import otocloud.auth.common.VertxAsyncExecutor;
import otocloud.auth.common.exception.ErrCode;
import otocloud.auth.common.exception.NullStringParameterException;
import otocloud.auth.common.session.SessionSchema;
import otocloud.auth.dao.MongoDAO;
import otocloud.auth.dao.UserDAO;
import otocloud.auth.entity.EntityFactory;
import otocloud.auth.entity.User;
import otocloud.auth.entity.UserOnlineSchema;
import otocloud.auth.mybatis.entity.*;
import otocloud.auth.mybatis.mapper.AuthAcctRoleMapper;
import otocloud.auth.mybatis.mapper.AuthRoleMapper;
import otocloud.auth.mybatis.mapper.AuthUserMapper;
import otocloud.auth.mybatis.mapper.AuthUserRoleMapper;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.validation.constraints.NotNull;

import java.io.IOException;
import java.lang.reflect.Field;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

/**
 * Created by better/zhangye on 15/9/28.
 */
@Singleton
public class UserServiceImpl implements UserService {

    protected static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    /**
     * 执行异步代码.
     */
    private final VertxAsyncExecutor asyncExecutor;
    /**
     * 线程安全的加密类.
     */
    private final StrongPasswordEncryptor passwordEncryptor;
    /**
     * 已登录的用户。
     * (openId, user)
     */
    public HashMap<String, User> usersLoggedIn;
    @Inject
    private UserDAO userDAO;
    @Inject
    private MongoDAO mongoDAO;
    @Inject
    private AuthUserMapper authUserMapper;
    /**
     * 管理角色
     */
    @Inject
    private AuthRoleMapper authRoleMapper;
    /**
     * 管理企业角色
     */
    @Inject
    private AuthAcctRoleMapper authAcctRoleMapper;
    /**
     * 管理用户拥有的企业角色
     */
    @Inject
    private AuthUserRoleMapper authUserRoleMapper;
    @Inject
    private SessionService sessionService;
    /**
     * 登录时产生对照关系。
     * 对照关系：(token, userOpenID)。
     */    
    private BidiMap<String, String> tokenWithUser;

    public UserServiceImpl() {
        tokenWithUser = new DualHashBidiMap<>();
        usersLoggedIn = new HashMap<>();
        asyncExecutor = VertxAsyncExecutor.create();
        passwordEncryptor = new StrongPasswordEncryptor();
    }

    /**
     * 对密码明文加密.
     *
     * @param userPassword
     * @return
     */
    private String encryptPassword(String userPassword) {
        //使用SHA-256加密算法
        return passwordEncryptor.encryptPassword(userPassword);
    }

    @Override
    public void create(JsonObject userInfo, Future<User> doneFuture) {

        try {
            final User user = EntityFactory.fromJsonObject(userInfo, User.class);

            //检查手机号字段是否为空.
            if (StringUtils.isBlank(user.getCellNo())) {
                doneFuture.fail(new NullStringParameterException());
            }

            Future<Boolean> verifyFuture = Future.future();
            //检查手机号字段是否重复.(重复表示已经注册过)
            verifyCellNo(user.getCellNo(), verifyFuture);

            //解密来自客户端的密码
            String plainText = RSAUtil.decrypt(user.getPassword(), "ufsoft*123");

            //加密用户密码并存储
            user.setPassword(encryptPassword(plainText));

            verifyFuture.setHandler(ret -> {
                if (ret.failed()) {
                    doneFuture.fail(ret.cause());
                    return;
                }

                boolean exists = ret.result();
                //手机号已经存在.
                if (exists) {
                    doneFuture.fail("手机号已经存在, 不能重复注册.");
                    return;
                }

                Future<User> future = Future.future();
                userDAO.create(user, future);
                future.setHandler(result -> {
                    if (result.succeeded()) {
                        User u = result.result();

                        // 生成用户角色
                        syncAddManagerRole(user.getOrgAcctId(), user.getID());

                        //todo delete this line
                        //todo delete 自动绑定c1用户
                        bindWithErp(user.getID(), "c1");

                        //生成OpenID
                        String openId = UUID.randomUUID().toString();
                        u.setOpenID(openId);

                        usersLoggedIn.put(openId, u);

                        logger.warn("用户创建成功, OpenID：" + u.getOpenID());
                        doneFuture.complete(u);
                    } else {
                        doneFuture.fail(result.cause());
                    }
                });
            });


        } catch (IOException e) {
            logger.warn("用户创建失败：" + e.getMessage());
        }

    }

    @Override
    public void addManagerRole(User manager, Future<User> addFuture) {
        asyncExecutor.execute(() -> {
            syncAddManagerRole(manager.getOrgAcctId(), manager.getID());
            return manager;
        }, addFuture);
    }

    /**
     * 修改数据库记录, 添加ERP绑定.
     *
     * @param userId      用户ID, 数据库主键
     * @param erpUserCode 用户在ERP系统中的编码
     */
    private void bindWithErp(final int userId, final String erpUserCode) {
        AuthUser authUser = new AuthUser();
        authUser.setId(userId);
        authUser.setConnectedWithErp("Y");
        authUser.setErpUserCode(erpUserCode);
        int updated = authUserMapper.updateByPrimaryKeySelective(authUser);
    }

    /**
     * 查找管理员角色
     *
     * @return
     */
    private AuthRole findAcctManagerRole() {
        String roleTypeCode = "ACCT_MANAGER";
        return findOrCreateAuthRole(roleTypeCode);
    }

    /**
     * 查找业务角色
     *
     * @return
     */
    private AuthRole findBizOperator() {
        String roleTypeCode = "BIZ_OPERATOR";
        return findOrCreateAuthRole(roleTypeCode);
    }

    /**
     * 如果没有找到角色, 则自动创建.
     *
     * @param roleTypeCode
     * @return
     */
    private AuthRole findOrCreateAuthRole(String roleTypeCode) {
        AuthRoleExample example = new AuthRoleExample();
        example.createCriteria().andDeleteDatetimeIsNull().andRoleTypeCodeEqualTo(roleTypeCode);

        List<AuthRole> roles = authRoleMapper.selectByExample(example);

        //如果没有找到,则创建角色.
        if (roles.isEmpty()) {
            String roleName = "默认名称.";
            return createAuthRole(roleName, roleTypeCode);
        }

        return roles.get(0);
    }

    /**
     * 创建授权角色.
     *
     * @param roleName     角色名称
     * @param roleTypeCode 角色类型
     * @return
     */
    private AuthRole createAuthRole(String roleName, String roleTypeCode) {
        AuthRole role = new AuthRole();
        role.setRoleName(roleName);
        role.setRoleTypeCode(roleTypeCode);
        role.setEntryId(0);
        role.setEntryDatetime(new Date());

        authRoleMapper.insert(role);

        return role;
    }

    /**
     * 添加企业账户角色
     *
     * @param acctId
     * @param roleId
     * @return
     */
    private AuthAcctRole addAcctRole(int acctId, int roleId) {
        AuthAcctRole authAcctRole = new AuthAcctRole();
        authAcctRole.setOrgAcctId(acctId);
        authAcctRole.setAuthRoleId(roleId);
        authAcctRole.setAuthTypeCode(UserStatus.ACTIVE());
        authAcctRole.setEntryId(0);
        authAcctRole.setEntryDatetime(new Date());

        authAcctRoleMapper.insert(authAcctRole);

        return authAcctRole;
    }

    /**
     * 添加用户角色
     */
    private AuthUserRole addUserRole(int userId, int acctRoleId) {
        AuthUserRole authUserRole = new AuthUserRole();
        authUserRole.setAuthUserId(userId);
        authUserRole.setAuthAcctRoleId(acctRoleId);
        authUserRole.setEntryId(0);
        authUserRole.setAuthTypeCode(UserStatus.ACTIVE());
        authUserRole.setEntryDatetime(new Date());

        authUserRoleMapper.insert(authUserRole);

        return authUserRole;
    }

    /**
     * 查找本企业下, 业务员企业角色
     *
     * @param acctId
     * @return
     */
    private AuthAcctRole findBizOperatorAcctRole(int acctId) {
        AuthRole operatorRole = findBizOperator();

        AuthAcctRoleExample example = new AuthAcctRoleExample();
        example.createCriteria()
                .andDeleteDatetimeIsNull()
                .andOrgAcctIdEqualTo(acctId)
                .andAuthRoleIdEqualTo(operatorRole.getId());

        List<AuthAcctRole> acctRoles = authAcctRoleMapper.selectByExample(example);

        if (acctRoles.isEmpty()) {
            return addAcctRole(acctId, operatorRole.getId());
        }

        return acctRoles.get(0);
    }

    /**
     * 给业务员添加[业务员角色]
     *
     * @param acctId
     * @param userId
     */
    private void syncAddBizOperatorRole(int acctId, int userId) {
        AuthAcctRole bizOperatorAcctRole = findBizOperatorAcctRole(acctId);
        addUserRole(userId, bizOperatorAcctRole.getId());
    }

    /**
     * 添加企业角色和管理员角色.
     *
     * @param acctId
     * @param userId
     */
    private void syncAddManagerRole(final int acctId, final int userId) {

//        String roleName = "企业管理员";
//        String roleTypeCode = "ACCT_MANAGER";

        //插入角色
        AuthRole managerRole = findAcctManagerRole();
        AuthRole operatorRole = findBizOperator();

        int roleId = managerRole.getId();
        int operatorRoleId = operatorRole.getId();

        //插入企业角色
        AuthAcctRole authAcctRole = addAcctRole(acctId, roleId);
        addAcctRole(acctId, operatorRoleId);

        int acctRoleId = authAcctRole.getId();
        //给管理员指定管理角色
        addUserRole(userId, acctRoleId);
    }

    @Override
    public User update(JsonObject userInfo) {
        String openId = userInfo.getString("openId");
        User user = findByOpenId(openId);

        Field[] fields = user.getClass().getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Field aField = fields[i];

            String fieldName = aField.getName();
            if (userInfo.containsKey(fieldName)) {
                String value = userInfo.getString(fieldName);
                try {
                    aField.set(user, value);
                } catch (IllegalAccessException e) {

                }
            }
        }

        update(user);

        return user;
    }

    /**
     * 将指定的字段更新到数据库中.
     *
     * @param user   带有指定id的User实例.
     * @param future 返回更新后的User实例.
     */
    @Override
    public void update(User user, Future<User> future) {
        User oldUser = usersLoggedIn.get(user.getOpenID());
        User mergedUser = user;
        mergedUser.setOrgAcctId(oldUser.getOrgAcctId());

        userDAO.merge(mergedUser, future);
    }

    public User findByOpenId(String openId) {
        return usersLoggedIn.get(openId);
    }

    /**
     * 根据用户名称查找用户的OpenID
     *
     * @param userName
     * @return 如果找到对应用户，返回用户的实体；如果没有找到，返回null。
     */
    public User findByName(String userName) {
        Collection<User> userInfos = usersLoggedIn.values();

        Iterator<User> itr = userInfos.iterator();
        while (itr.hasNext()) {
            User curr = itr.next();
            if (curr.getUserName().equals(userName)) {
                return curr;
            }
        }

        return null;
    }

    public List<User> findAll() {
        List<User> allUsers = new LinkedList<>();
        allUsers.addAll(usersLoggedIn.values());
        return allUsers;
    }

    @Override
    public User delete(JsonObject userInfo) {
        throw new NotImplementedException();
    }

    public void update(User user) {
        connectOpenIdWithUser(user);
    }

    @Override
    public void login(@NotNull(message = "用户名不能为空") String userName,
                      @NotNull(message = "密码不能为空") String password,
                      String sessionId, Future<JsonObject> future) {
        login(userName, password, false, sessionId, future);
    }

    @Override
    public void findSessionId(String token, Future<String> findFuture) {
        JsonObject query = new JsonObject();
        query.put(UserOnlineSchema.TOKEN, token);

        mongoDAO.findOne(query, ret -> {
            if (ret.succeeded() && ret.result() != null) {
                JsonObject found = ret.result();

                String sessionId = found.getString(UserOnlineSchema.SESSION_ID);
                logger.info("查找到的SessionID: " + sessionId);

                findFuture.complete(sessionId);
            } else {
                findFuture.fail("无法根据Token找到SessionID. 可能的原因是: 用户已经退出.");
            }
        });
    }

    @Override
    public void deleteSessionId(String token, Future<Void> deleteFuture) {
        JsonObject query = new JsonObject();
        query.put(UserOnlineSchema.TOKEN, token);
        mongoDAO.delete(query, ret -> {
            if (ret.succeeded()) {
                deleteFuture.complete();
            } else {
                deleteFuture.fail("无法移除SessionID.");
            }
        });
    }

    /**
     * 使用用户名或手机号登录.
     *
     * @param userName 用户名或手机号.
     * @param password 密码.
     * @param future
     */
    @Override
    public void login(String userName, String password, boolean useCellNo,
                      String sessionId, Future<JsonObject> future) {
        //通过用户名查找可能匹配的用户列表
        AuthUserExample example = new AuthUserExample();
        AuthUserExample.Criteria c = example.createCriteria().andDeleteDatetimeIsNull();
        c.andStatusEqualTo(UserStatus.ACTIVE()); //只有 A 状态的用户才可以登录.

        if (useCellNo) {
            logger.info("使用 [手机号] 登录, 查询数据库.");
            c.andCellNoEqualTo(userName);
        } else {
            logger.info("使用 [用户名和密码] 登录, 查询数据库.");
            c.andNameEqualTo(userName);
        }


        List<AuthUser> users = authUserMapper.selectByExample(example);

        AuthUser loginUser = null;

        ListIterator<AuthUser> itr = users.listIterator();

        //对密码解密,获取明文
        String plainTxt = getPlainText(password);

        //分别验证各个用户
        while (itr.hasNext()) {
            AuthUser user = itr.next();
            String dbPwd = user.getPassword();
            if(plainTxt.equals(dbPwd)){
            	loginUser = user;
                break;
            }
            //使用SHA-256哈希摘要算法将明文进行哈希计算，并与数据存储的哈希密文进行比较
            boolean matched = passwordEncryptor.checkPassword(plainTxt, dbPwd);

            if (matched) {
                loginUser = user;
                break;
            }
        }

        if (loginUser == null) {
            //没有用户,直接返回登录失败.
            logger.info("无法找到用户信息.");
            future.fail(new RuntimeException(ErrCode.INVALID_USERNAME_OR_PASSWORD.getMessage()));
            return;
        }

        //保存登录信息
        loginUser.setLastLoginDatetime(new Date());
        authUserMapper.updateByPrimaryKey(loginUser);

        //生成UserOpenId和token.
        String token = UUID.randomUUID().toString();
        String userOpenId = UUID.randomUUID().toString();

        //查找用户角色
        List<AuthRole> roles = authRoleMapper.getRoles(loginUser.getId(), loginUser.getOrgAcctId());

        JsonObject reply = createReply(loginUser, userOpenId, token, roles);

        //记录用户在线状态
        //在数据库中记录登录信息.
        Future<Void> onlineFuture = Future.future();
        stayOnline(loginUser, sessionId, userOpenId, token, onlineFuture);
        onlineFuture.setHandler(onlineRet -> {
            if (onlineRet.succeeded()) {
            	
            	//生成session
/*            	Session session = sessionService.createSession();
            	session.put(key, obj)*/
            	
                future.complete(reply);
            } else {
                future.fail(onlineRet.cause());
            }
        });

    }


    private String getPlainText(String encryptedPassword){
        try {
            return RSAUtil.decrypt(encryptedPassword, "ufsoft*123");
        } catch (Exception e) {
            logger.warn("无法解密密码.");
        }

        return encryptedPassword;
    }

    private JsonObject createReply(AuthUser user, String userOpenId, String token, List<AuthRole> roles) {
        JsonObject reply = new JsonObject();
        reply.put("user_openid", userOpenId);
        reply.put("user_name", user.getName());
        reply.put("access_token", token.toString());// 获取到的凭证
        reply.put("expires_in", 7200); //凭证有效时间，单位：秒

        int acctId = user.getOrgAcctId();
        reply.put("acct_id", acctId); //将企业账户返回到页面, 客户端向后台发送总线消息时会用到企业账户.

        //添加用户角色
        JsonArray replyRoles = new JsonArray();
        if (!roles.isEmpty()) {
            for (AuthRole role : roles) {
                JsonObject replyRole = new JsonObject();
                replyRole.put("role_name", role.getRoleName());
                replyRole.put("role_type", role.getRoleTypeCode());
                replyRoles.add(replyRole);
            }
        }
        //无论是否分配角色,都返回roles字段,以保持前端编码的统一.
        reply.put("roles", replyRoles);

        reply.put(SessionSchema.ORG_ACCT_ID, acctId); //设置企业账号
        reply.put(SessionSchema.CURRENT_USER_ID, user.getId()); //设置用户ID（数据库主键）
        return reply;
    }


    private void stayOnline(AuthUser user, String sessionId, String userOpenId, String token, Future<Void> future) {
        if (null == sessionId) {
            if (logger.isInfoEnabled()) {
                logger.info("没有SessionID, 无法关联token.");
            }
        } else if (logger.isInfoEnabled()) {
            logger.info("正在关联token与SessionID." +
                    "[token:" + token + ", " +
                    "sessionId:" + sessionId + "]");
        }

        JsonObject online = new JsonObject();
        online.put(UserOnlineSchema.USER_ID, user.getId());
        online.put(UserOnlineSchema.USER_OPEN_ID, userOpenId);
        online.put(UserOnlineSchema.ACCT_ID, user.getOrgAcctId());
        online.put(UserOnlineSchema.TOKEN, token);
        online.put(UserOnlineSchema.SESSION_ID, sessionId);
        Instant timestamp = Instant.now();
        online.put(UserOnlineSchema.LOGIN_DATE_TIME, timestamp);

        LocalDateTime ldt = LocalDateTime.ofInstant(timestamp, ZoneId.systemDefault());

        online.put(UserOnlineSchema.LOCAL_LOGIN_DATE_TIME, ldt.toString());


        stayOnlineInfo(online, future);

    }

    /**
     * 不记录生成OpenId.
     *
     * @param user
     * @param token
     * @param sessionId
     * @return
     */
    @Override
    public JsonObject makeUserOnlineShape(AuthUser user, String token, String sessionId) {
        JsonObject online = new JsonObject();
        online.put(UserOnlineSchema.USER_ID, user.getId());
        online.put(UserOnlineSchema.ACCT_ID, user.getOrgAcctId());
        online.put(UserOnlineSchema.TOKEN, token);
        online.put(UserOnlineSchema.SESSION_ID, sessionId);
        Instant timestamp = Instant.now();
        online.put(UserOnlineSchema.LOGIN_DATE_TIME, timestamp);

        LocalDateTime ldt = LocalDateTime.ofInstant(timestamp, ZoneId.systemDefault());

        online.put(UserOnlineSchema.LOCAL_LOGIN_DATE_TIME, ldt.toString());

        return online;
    }

    /**
     * 将用户的登录信息存储在数据库中.
     *
     * @param future
     */
    @Override
    public void stayOnlineInfo(JsonObject online, Future<Void> future) {
        mongoDAO.insert(online, onlineRet -> {
            if (onlineRet.succeeded()) {
                future.complete();
            } else {
                future.fail(onlineRet.cause());
            }
        });
    }

    /**
     * 使用手机号登录系统。
     *
     * @param cellNo   手机号.
     * @param password 密码.
     * @param future   返回登录结果.
     */
    @Override
    public void loginWithCellNo(@NotNull(message = "手机号不能为空") String cellNo,
                                @NotNull(message = "密码不能为空") String password,
                                String sessionId,
                                Future<JsonObject> future) {
        login(cellNo, password, true, sessionId, future);
    }

    @Override
    public void importErpUsers(List<AuthUser> userList, Future<Integer> importFuture) {
        asyncExecutor.execute(() -> authUserMapper.insertBatch(userList), importFuture);
    }

    private void connectOpenIdWithUser(User userInfo) {
        //记录openID与user的对应关系
        usersLoggedIn.put(userInfo.getOpenID(), userInfo);
    }

    /**
     * 1. 创建token，并根据token创建session。
     * 2. 建立token与user的对应关系。
     *
     * @param userInfo
     * @return
     * @see UserServiceImpl#tokenWithUser
     */
    private UUID getToken(User userInfo) {
        UUID token = UUID.randomUUID();

        logger.info("AuthService - 生成了token: " + token.toString());

        //记录token与UserOpenId的对应关系
        tokenWithUser.put(token.toString(), userInfo.getOpenID());
        return token;
    }


    /**
     * 用户退出登录.
     * 从数据库中删除登录信息.
     *
     * @param userOpenId
     */
    public void goOffline(String userOpenId, Handler<AsyncResult<MongoClientDeleteResult>> resultHandler) {
        mongoDAO.delete(new JsonObject().put(UserOnlineSchema.USER_OPEN_ID, userOpenId), resultHandler);
    }

    /**
     * 根据userOpenId查询token，根据token，移除会话。
     *
     * @param userOpenId
     * @param future
     */
    @Override
    public void logout(String userOpenId, Future<JsonObject> future) {
        String token = tokenWithUser.getKey(userOpenId);
        //sessionService.deleteById(token);
        tokenWithUser.remove(token);

        JsonObject reply = new JsonObject();
        reply.put("errCode", ErrCode.SUCCESS.getCode());
        reply.put("errMsg", ErrCode.SUCCESS.getMessage());

        goOffline(userOpenId, ret -> {
            if (ret.succeeded()) {
                future.complete(reply);
            } else {
                future.fail("无法移除用户的在线信息.");
            }
        });


    }

    @Override
    public void verifyCellNo(@NotNull(message = "手机号不能为空.") String cellNo, Future<Boolean> future) {
        Objects.requireNonNull(cellNo);

        asyncExecutor.execute(() -> authUserMapper.isRegisteredCellNo(cellNo), future);
    }

    @Override
    public void getUserListByPage(int acctId, int deptId, int pageIndex, int pageSize,
                                  Future<List<AuthUser>> pageFuture) {
        List<AuthUser> users = new LinkedList<>();

        if (pageIndex < 0 || pageSize <= 0) {
            pageFuture.complete(users);

            return;
        }

        asyncExecutor.execute(() -> {
            AuthUserExample authUserExample = new AuthUserExample();
            authUserExample.createCriteria()
                    .andDeleteDatetimeIsNull()
                    .andOrgAcctIdEqualTo(acctId)
                    .andOrgDeptIdEqualTo(deptId);

            int page_index = pageIndex;
            int page_size = pageSize;
            RowBounds bounds = new RowBounds(page_index * page_size, page_size);
            return authUserMapper.selectByExampleWithRowbounds(authUserExample, bounds);
        }, pageFuture);
    }

    @Override
    public int countUser(int acctId, int deptId) {
        AuthUserExample authUserExample = new AuthUserExample();
        authUserExample.createCriteria()
                .andDeleteDatetimeIsNull()
                .andOrgAcctIdEqualTo(acctId)
                .andOrgDeptIdEqualTo(deptId);

        return authUserMapper.countByExample(authUserExample);
    }

    /**
     * 根据数据库中的 ERP_USER_CODE 字段和 CONNECTED_WITH_ERP 字段, 判断是否绑定了ERP账户.
     *
     * @param acct_id         企业账户
     * @param user_name       用户名
     * @param connectedFuture 是否连接上
     */
    @Override
    public void beBoundWithErp(int acct_id, String user_name, Future<AuthUser> connectedFuture) {
        AuthUserExample example = new AuthUserExample();
        example.createCriteria()
                .andOrgAcctIdEqualTo(acct_id)
                .andErpUserCodeEqualTo(user_name)
                .andDeleteDatetimeIsNull()
                .andConnectedWithErpEqualTo("Y");
        List<AuthUser> userList = authUserMapper.selectByExample(example);

        if (userList.isEmpty()) {
            connectedFuture.fail("ERP账户未绑定.");
            return;
        }

        AuthUser user = userList.get(0);
        connectedFuture.complete(user);
    }

    @Override
    public void createUser(int acctId, int managerId, String userName, String cellNo, String email,
                           Future<JsonObject> createFuture) {
        Future<AuthUser> insertFuture = Future.future();

        asyncExecutor.execute(() -> {
            AuthUser user = new AuthUser();
            user.setOrgAcctId(acctId);
            user.setOrgDeptId(-1);
            user.setName(userName);
            user.setCellNo(cellNo);
            user.setEmail(email);
            user.setStatus(UserStatus.INACTIVE());
            user.setEntryDatetime(new Date());
            user.setEntryId(managerId);

            authUserMapper.insert(user);
            return user;
        }, insertFuture);

       insertFuture.setHandler(ret -> {
           if (ret.failed()){
               logger.warn("用户数据无法保存到数据库.");
               createFuture.fail("新建用户失败");
               return;
           }

           AuthUser user = ret.result();

           //生成激活码
           String activateCode = generateActivationCode(user);
           JsonObject activateInfo = new JsonObject();
           activateInfo.put("acct_id", acctId);
           activateInfo.put("manager_id", managerId); //企业管理员ID
           activateInfo.put("user_id", user.getId());
           activateInfo.put("user_name", user.getName());
           activateInfo.put("cell_no", user.getCellNo());
           activateInfo.put("email", user.getEmail());
           activateInfo.put("activation_code", activateCode);

           //将一次性激活码存入数据库
           mongoDAO.insert(MongoDAO.USERS_ACTIVATION, activateInfo, insertRet -> {
               if(insertRet.failed()){
                   logger.warn("用户激活码保存到 Mongo 数据库失败.");
                   createFuture.fail("无法将用户激活码保存到 Mongo 数据库中.");
                   return;
               }
               createFuture.complete(activateInfo);
           });
       });

    }

    /**
     * 生成用户的一次性激活码
     *
     * @param user
     * @return
     */
    public String generateActivationCode(AuthUser user) {
        //将激活码与userId,临时放入mongo中.
        //接到激活请求时,将mongo中的激活码删除.更新User的状态为A
        String name = user.getName();
        String cellNo = user.getCellNo();
        String email = user.getEmail();

        String md1 = DigestUtils.md5Hex(name);
        String md2 = DigestUtils.md5Hex(md1 + cellNo);
        String md3 = DigestUtils.md5Hex(md2 + email);

        return md3;
    }

    public User deleteByOpenId(String userOpenId) {
        User u = usersLoggedIn.remove(userOpenId);
        int id = u.getID();
        Future<Boolean> future = Future.future();
        userDAO.deleteById(id, future);
        return u;
    }

    @Override
    public void deleteById(int userId, Future<User> doneFuture) {
        Future<User> findFuture = Future.future();

        userDAO.findById(userId, findFuture);

        findFuture.setHandler(ret -> {
            if (ret.succeeded()) {
                Future<Boolean> deleteFuture = Future.future();
                userDAO.deleteById(userId, deleteFuture);

                deleteFuture.setHandler(delRet -> {
                    if (delRet.succeeded()) {
                        doneFuture.complete(ret.result());
                    } else {
                        doneFuture.fail(new RuntimeException(delRet.cause()));
                    }
                });
            } else {
                doneFuture.fail(new RuntimeException(ret.cause()));
            }
        });
    }
}

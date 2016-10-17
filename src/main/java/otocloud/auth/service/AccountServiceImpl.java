package otocloud.auth.service;

import com.google.inject.Inject;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import otocloud.auth.common.VertxAsyncExecutor;
import otocloud.auth.mybatis.entity.AuthUser;
import otocloud.auth.mybatis.entity.AuthUserExample;
import otocloud.auth.mybatis.entity.OrgDept;
import otocloud.auth.mybatis.mapper.AuthUserMapper;
import otocloud.auth.mybatis.mapper.OrgDeptMapper;

import java.util.List;

/**
 * zhangyef@yonyou.com on 2015-12-17.
 */
public class AccountServiceImpl implements AccountService {
    /**
     * 执行异步代码.
     */
    private final VertxAsyncExecutor asyncExecutor;

    @Inject
    private OrgDeptMapper orgDeptMapper;

    @Inject
    private AuthUserMapper authUserMapper;

    @Inject
    public AccountServiceImpl(Vertx vertx) {
        asyncExecutor = VertxAsyncExecutor.create(vertx);
    }

    @Override
    public void getDepartmentsList(int acctId, Future<List<OrgDept>> getFuture) {
        asyncExecutor.execute(() -> orgDeptMapper.selectByAcct(acctId), getFuture);
    }

    @Override
    public void bindWithErp(int acctId, int userId, String userName, String password, Future<Boolean> bindFuture) {
        AuthUser user = new AuthUser();
        user.setId(userId);
        user.setErpUserCode(userName);
        user.setConnectedWithErp("Y");

        Future<Integer> updateFuture = Future.future();

        asyncExecutor.execute(() -> {
            int updated = authUserMapper.updateByPrimaryKeySelective(user);
            return updated;
        }, updateFuture);

        updateFuture.setHandler(ret -> {
            if (ret.succeeded() && ret.result() != 0) {
                bindFuture.complete(true);
                return;
            }

            bindFuture.fail("操作数据库时出现错误.");
        });
    }

    @Override
    public void getBindInfo(int userId, Future<JsonObject> getFuture) {
        Future<AuthUser> selectFuture = Future.future();

        asyncExecutor.execute(() -> {
            AuthUser user = authUserMapper.selectByPrimaryKey(userId);
            return user;
        }, selectFuture);

        selectFuture.setHandler(ret -> {
            if (ret.failed()) {
                getFuture.fail("[数据库查询错误] 查找用户的绑定ERP账户是出错.");
                return;
            }

            AuthUser user = ret.result();

            String connected = user.getConnectedWithErp();

            boolean bound = (connected != null && connected.equals("Y"));

            JsonObject reply = new JsonObject();
            reply.put("isBound", bound);
            if (bound) {
                reply.put("erp_usercode", user.getErpUserCode());
            }

            getFuture.complete(reply);
        });

    }

    @Override
    public void unbindWithErp(int userId, Future<JsonObject> unbindFuture) {
        Future<AuthUser> selectFuture = Future.future();

        asyncExecutor.execute(() -> {
            AuthUser user = authUserMapper.selectByPrimaryKey(userId);
            return user;
        }, selectFuture);

        selectFuture.setHandler(ret -> {
            if (ret.failed()) {
                unbindFuture.fail("无法查询数据库的绑定信息.");
                return;
            }
            AuthUser user = ret.result();
            String erpUserCode = user.getErpUserCode();

            user.setConnectedWithErp(null);
            user.setErpUserCode(null);

            int updated = authUserMapper.updateByPrimaryKey(user);

            JsonObject reply = new JsonObject();
            reply.put("isBound", false);
            reply.put("erp_usercode", erpUserCode);

            unbindFuture.complete(reply);
        });


    }


    private int getUserNumber(int acctId, String userStatus) {
        AuthUserExample example = new AuthUserExample();
        AuthUserExample.Criteria c = example.createCriteria().andDeleteDatetimeIsNull()
                .andOrgAcctIdEqualTo(acctId);

        switch (userStatus.toUpperCase()) {
            case "A":
            case "I":
            case "F":
                c.andStatusEqualTo(userStatus);
                break;
            case "NULL":
                c.andStatusIsNull();
                break;
            default:
                break;
        }

        return authUserMapper.countByExample(example);
    }


    @Override
    public void getUserStatistics(int acctId, Future<JsonObject> getFuture) {
        asyncExecutor.execute(() -> {

            int total_user_number = getUserNumber(acctId, "*");
            int active_user_number = getUserNumber(acctId, "A");
            int inactive_user_number = getUserNumber(acctId, "I");
            int forbidden_user_number = getUserNumber(acctId, "F");
            int unknown_user_number = getUserNumber(acctId, "NULL");

            JsonObject numbers = new JsonObject();
            numbers.put("total_user_number", total_user_number);
            numbers.put("active_user_number", active_user_number);
            numbers.put("inactive_user_number", inactive_user_number);
            numbers.put("forbidden_user_number", forbidden_user_number);
            numbers.put("unknown_user_number", unknown_user_number);
            return numbers;

        }, getFuture);

    }
}

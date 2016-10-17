package otocloud.auth.service;

import com.google.inject.ImplementedBy;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import otocloud.auth.mybatis.entity.OrgDept;

import java.util.List;

/**
 * 查询账户的有关信息。
 * zhangyef@yonyou.com on 2015-12-17.
 */
@ImplementedBy(AccountServiceImpl.class)
public interface AccountService {
    void getDepartmentsList(int acctId, Future<List<OrgDept>> getFuture);

    /**
     * 用户将NCCloud账户与ERP账户绑定.
     *
     * @param acctId      NCCloud企业账号
     * @param userId
     * @param erpUserName ERP用户名
     * @param erpPassword ERP密码
     * @param bindFuture
     */
    void bindWithErp(int acctId, int userId, String erpUserName, String erpPassword, Future<Boolean> bindFuture);

    /**
     * 根据用户ID, 查询是否绑定了ERP账户.
     *
     * @param userId
     * @param getFuture
     */
    void getBindInfo(int userId, Future<JsonObject> getFuture);

    /**
     * 解除绑定ERP账户.
     *
     * @param userId
     * @param unbindFuture
     */
    void unbindWithErp(int userId, Future<JsonObject> unbindFuture);

    /**
     * 查询指定企业下的用户统计信息.
     *
     * @param acctId    企业ID
     * @param getFuture 返回统计结果.
     */
    void getUserStatistics(int acctId, Future<JsonObject> getFuture);
}

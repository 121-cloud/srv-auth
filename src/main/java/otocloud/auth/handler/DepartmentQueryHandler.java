package otocloud.auth.handler;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import io.vertx.core.Future;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import otocloud.auth.common.session.SessionSchema;
import otocloud.auth.common.util.Mapper;
import otocloud.auth.mybatis.entity.OrgDept;
import otocloud.auth.service.AccountService;
import otocloud.common.ActionURI;
import otocloud.framework.core.HandlerDescriptor;
import otocloud.framework.core.OtoCloudBusMessage;
import otocloud.framework.core.OtoCloudComponentImpl;
import otocloud.framework.core.OtoCloudEventHandlerImpl;

import java.util.List;

/**
 * 查询指定企业账户下的部门信息列表.
 * zhangyef@yonyou.com on 2015-12-16.
 */
public class DepartmentQueryHandler extends OtoCloudEventHandlerImpl<JsonObject> {
    protected static final Logger logger = LoggerFactory.getLogger(DepartmentQueryHandler.class);


    @Inject
    private AccountService accountService;

    @Inject
    public DepartmentQueryHandler(@Named("UserComponent") OtoCloudComponentImpl componentImpl) {
        super(componentImpl);
    }

    @Override
    public void handle(OtoCloudBusMessage<JsonObject> msg) {
        logger.info("AuthService - 收到了查询部门信息的请求.");
        JsonObject body = msg.body();
        JsonObject session = body.getJsonObject(SessionSchema.SESSION);

        int acctId = session.getInteger(SessionSchema.ORG_ACCT_ID);

        Future<List<OrgDept>> getFuture = Future.future();

        accountService.getDepartmentsList(acctId, getFuture);

        getFuture.setHandler(ret -> {
            if(ret.failed()){
                msg.fail(1, "无法获取部门列表。");
                return;
            }

            List<OrgDept> depts = ret.result();
            //添加未分类部门
            OrgDept dept = new OrgDept();
            dept.setOrgAcctId(acctId);
            dept.setDeptName("财务部");
            dept.setId(-1);

            depts.add(dept);

            try {
                JsonArray replyArray = Mapper.toJsonArray(depts);
                msg.reply(replyArray);
            }catch (Exception ignore){
                msg.fail(1, "内部数据格式转换错误。");
            }

        });

    }

    /**
     * "服务名".user-management.department.query
     *
     * @return
     */
    @Override
    public String getEventAddress() {
        return "department.query";
    }

    /**
     * 注册REST API。
     *
     * @return get /api/"服务名"/"组件名"/departments
     */
    @Override
    public HandlerDescriptor getHanlderDesc() {
        HandlerDescriptor handlerDescriptor = super.getHanlderDesc();
        ActionURI uri = new ActionURI("departments", HttpMethod.GET);
        handlerDescriptor.setRestApiURI(uri);
        return handlerDescriptor;
    }
}

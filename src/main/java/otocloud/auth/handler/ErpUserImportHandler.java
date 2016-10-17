package otocloud.auth.handler;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import otocloud.auth.common.ProcessReporter;
import otocloud.auth.common.session.SessionSchema;
import otocloud.auth.mybatis.entity.AuthUser;
import otocloud.auth.service.UserService;
import otocloud.auth.verticle.AuthService;
import otocloud.auth.verticle.UserComponent;
import otocloud.common.ActionURI;
import otocloud.common.Command;
import otocloud.common.CommandDeliveryOptions;
import otocloud.common.webserver.MessageBodyConvention;
import otocloud.framework.core.HandlerDescriptor;
import otocloud.framework.core.OtoCloudBusMessage;
import otocloud.framework.core.OtoCloudComponentImpl;
import otocloud.framework.core.OtoCloudEventHandlerImpl;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * 从ERP系统导入企业内用户.
 * 导入用户前,需要管理员先登录ERP系统.
 * <p>
 * zhangyef@yonyou.com on 2015-12-24.
 */
public class ErpUserImportHandler extends OtoCloudEventHandlerImpl<JsonObject> {
    private final String ADAPTER_ADDRESS = "otocloud-app-common-uid-ad-user.get";
    protected Logger logger = LoggerFactory.getLogger(ErpUserImportHandler.class);
    private String IMPORT_PROCESS_MONITOR_ADDRESS;
    @Inject
    private Vertx vertx;

    @Inject
    private UserService userService;

    private String authServiceName;

    private OtoCloudComponentImpl component;

    private AuthService authService;

    @Inject
    public ErpUserImportHandler(@Named("UserComponent") OtoCloudComponentImpl component, AuthService authService) {
        super(component);
        this.authService = authService;

        init();
    }

    @PostConstruct
    private void init() {
        this.authServiceName = authService.getServiceName();

        IMPORT_PROCESS_MONITOR_ADDRESS = this.authServiceName + "."
                + this.componentImpl.getName()
                + ".users.erp.import.process.monitor";
    }

    @Override
    public void handle(OtoCloudBusMessage<JsonObject> msg) {
        logger.info("接收到用户 [ERP] 导入请求,开始验证请求信息格式.");

        JsonObject body = msg.body();
        JsonObject params = body.getJsonObject(MessageBodyConvention.HTTP_QUERY);
        if (params.containsKey("monitor_address")) {
            //返回进度监控地址
            JsonObject reply = new JsonObject();
            reply.put("progress_monitor_address", IMPORT_PROCESS_MONITOR_ADDRESS);
            msg.reply(reply);
            return;
        }

        JsonObject session = body.getJsonObject(SessionSchema.SESSION);

        //接受消息后,马上返回浏览器,表明已经开始任务.
        JsonObject reply = new JsonObject();
        reply.put("result", "ok");
        reply.put("progress_monitor_address", IMPORT_PROCESS_MONITOR_ADDRESS);
        msg.reply(reply);

        doImportOnGateway(session);
    }

    private void doImportOnGateway(JsonObject session) {
        if (session == null) {
            return;
        }
        int acctId = session.getInteger(SessionSchema.ORG_ACCT_ID);
        int userId = session.getInteger(SessionSchema.CURRENT_USER_ID);

        Command command = new Command(acctId, ADAPTER_ADDRESS);
        command.setSessionID(session.getString(SessionSchema.SESSION_ID));
        command.setSessions(session);

        ProcessReporter reporter = new ProcessReporter(this.vertx, IMPORT_PROCESS_MONITOR_ADDRESS);

        //设置多次调用
        CommandDeliveryOptions options = new CommandDeliveryOptions();
        options.setSendTimeout(10 * 1000); //10s

        //在网关中执行, 调用适配器接口.
        command.executeOnGateway(this.vertx, options, true, cmdRet -> {
            if (cmdRet.failed()) {
                logger.info("无法从ERP获取数据.");
                return;
            }

            logger.info("已经从ERP成功获取数据. 正在将数据保存到121系统中.");

//            int userNum = cmdRet.getDataCount();
            JsonArray users = cmdRet.getDatas();

            List<AuthUser> userList = new LinkedList<AuthUser>();
            for (Object user : users) {
                logger.info("转换用户信息格式.");
                if (user instanceof JsonObject) {

                    logger.debug(user);

                    JsonObject userInfo = (JsonObject) user;
                    AuthUser authUser = new AuthUser();

                    authUser.setOrgDeptId(-1); //默认部门
                    authUser.setOrgAcctId(acctId);
                    authUser.setName(userInfo.getString("user_code")); //默认与ERP用户编码一致.
                    authUser.setErpUserCode(userInfo.getString("user_code")); //注意字段的对应关系
//                    authUser.setCellNo(userInfo.getString("cell_no")); //TODO 曹佳俊, 后台返回的信息中没有手机号
                    authUser.setEmail(userInfo.getString("email"));
                    authUser.setConnectedWithErp("Y");

                    authUser.setStatus("A"); //导入后默认为"有效态"
                    authUser.setEntryId(userId);
                    authUser.setEntryDatetime(new Date());

                    userList.add(authUser);
                }
            }

            //汇报进度
            int total = cmdRet.getTotal();
            int delta = cmdRet.getDelta();

            reporter.work(100 * delta / total - 1);

            Future<Integer> importFuture = Future.future();
            userService.importErpUsers(userList, importFuture);

            importFuture.setHandler(ret -> {
                if (importFuture.failed()) {
                    logger.warn("无法将ERP用户数据导入到121系统账户中.");
                }
            });

            if(cmdRet.isComplete()){
                logger.warn("成功! 已将ERP用户数据导入到121系统账户中.");
                reporter.finish();
            }
        });
    }

    /**
     * get /api/"服务名"/"组件名"/users/actions/erp/import
     *
     * @return
     */
    @Override
    public HandlerDescriptor getHanlderDesc() {
        HandlerDescriptor handlerDescriptor = super.getHanlderDesc();
        ActionURI uri = new ActionURI("users/actions/erp/import", HttpMethod.GET);
        handlerDescriptor.setRestApiURI(uri);
        return handlerDescriptor;
    }

    /**
     * <服务名>.user-management.users.erp.import
     *
     * @return
     */
    @Override
    public String getEventAddress() {
        return UserComponent.MANAGE_USER_ADDRESS + ".erp.import";
    }
}

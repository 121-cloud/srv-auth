package otocloud.auth.verticle;

import com.google.inject.Guice;
import com.google.inject.Injector;
import io.vertx.core.Context;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import otocloud.auth.common.util.GlobalDataPool;
import otocloud.auth.common.util.ShareableUtil;
import otocloud.auth.guice.BindingModule;
import otocloud.auth.handler.UserLoginHandler;
import otocloud.framework.core.HandlerDescriptor;
import otocloud.framework.core.OtoCloudComponent;
import otocloud.framework.core.OtoCloudService;
import otocloud.framework.core.OtoCloudServiceForVerticleImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * 利用应用框架编写.
 * <p>
 * Created by zhangye on 2015-10-14.
 *
 * @see OtoCloudService
 */
public class AuthService extends OtoCloudServiceForVerticleImpl {
    protected static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    private Injector injector;

    //声明为类的字段,用于后续延迟加载数据源(框架提供的vertx数据源)
    private BindingModule bindingModule;

    private Vertx vertx;

    private Context context;

    private JsonObject config;

    @Override
    public void init(Vertx vertx, Context context) {
        this.vertx = vertx;
        this.context = context;
        this.config = context.config();

        super.init(vertx, context);

        //如果有mongo_client配置,放入上下文当中.
        if (config.containsKey("mongo_client")) {
            JsonObject mongo_client = config.getJsonObject("mongo_client");
            GlobalDataPool.INSTANCE.put("mongo_client_at_auth_service", mongo_client);
        }

        //添加Guice依赖注入
        initGuiceInjector(vertx);

        //添加OAuth授权接口
//        initOAuthService(vertx);
    }

    /**
     * 在指定Vertx实例上添加授权接口.
     *
     * @param vertx
     */
    private void initOAuthService(Vertx vertx) {
        Context context = vertx.getOrCreateContext();
        Router mainRouter = ShareableUtil.getMainRouter(vertx);


        mainRouter.route(HttpMethod.GET, "/authorize").handler(routing -> {
            System.out.println("OAuthService, authorize");

            routing.next();
        });

        mainRouter.route(HttpMethod.POST, "/token").handler(routing -> {
            System.out.println("OAuthService, token");

            routing.next();
        });
    }

    private void initGuiceInjector(Vertx vertx) {
        bindingModule = new BindingModule();
        bindingModule.setVertx(vertx);
        bindingModule.setAuthService(this);

//        ValidationModule validationModule = new ValidationModule();
        injector = Guice.createInjector(bindingModule);
//        try {
//            injector = LifecycleInjector.builder()
//                    .withBootstrapModule(validationModule)
//                    .withModules(bindingModule)
//                    .usingBasePackages("otocloud.auth")
//                    .build().createInjector();
//        }catch(Exception ignore){
//            logger.info(ignore.getMessage());
//        }

    }

    @Override
    public void start(Future<Void> startFuture) throws Exception {
        Future<Void> authFuture = Future.future();

        authFuture.setHandler(result -> {
            if (result.succeeded()) {
                //注入数据源
                bindingModule.getDataSourceHolder().setJdbcDataSource(this.getSysDatasource());

                //向Webserver发起请求,开启授权功能.
                turnOnAuthOnWebServer(startFuture);
            } else {
                startFuture.fail(result.cause());
            }
        });

        super.start(authFuture);

        System.out.println(config()); //TODO 读取数据源配置
    }


    /**
     * 开启WebServer的授权功能.
     *
     * @param startFuture
     */
    private void turnOnAuthOnWebServer(Future<Void> startFuture) {
        JsonObject config = config();
        JsonObject api_register_server = config.getJsonObject("api_register_server");
        String webserver_name = api_register_server.getString("webserver_name");

        String address = webserver_name + ".configuration.put";
        JsonObject authConfig = new JsonObject();
        authConfig.put("auth.enabled", true);
        authConfig.put("auth.session_query_address", getServiceName() + ".user-management.query");
        authConfig.put("auth.login_url", "/api/" + getServiceName()
                + "/" + UserComponent.getComponentName()
                + "/" + UserLoginHandler.getActionUrl());

        //设置不需要经过安全验证的URL,例如退出URL,登录过程中需要的URL等.
        authConfig.put("auth.security_urls", getSecurityUrls());

        vertx.eventBus().<JsonObject>send(address, authConfig, ret -> {
            if (ret.failed()) {
                logger.warn("无法开启WebServer[ " + webserver_name + " ]的授权功能.");
            }

            JsonObject reply = ret.result().body();
            if (reply.getInteger("errCode") != 0) {
                logger.warn("无法开启WebServer[ " + webserver_name + " ]的授权功能."
                        + reply.getString("errMsg"));
            }

            startFuture.complete();
        });
    }

    /**
     * 设置不进行身份验证的API.
     * @return
     */
    private JsonArray getSecurityUrls(){
        JsonArray urls = new JsonArray();
        urls.add("/api/otocloud-auth/user-management/users/verify");
        urls.add("/api/otocloud-auth/user-management/users/actions/logout");
        urls.add("/api/otocloud-auth/user-management/users/actions/erp/login");
        urls.add("/api/otocloud-acct/account"); //注册接口.

        return urls;
    }

    @Override
    public void stop(Future<Void> stopFuture) throws Exception {
        close(stopFuture);
    }

    @Override
    public void registerRestAPIs(String compName, List<HandlerDescriptor> handlerDescs, Future<Void> regFuture) {
        super.registerRestAPIs(compName, handlerDescs, regFuture);
    }

    @Override
    public List<OtoCloudComponent> createServiceComponents() {
        List<OtoCloudComponent> retCloudComponents = new ArrayList<>();

        retCloudComponents.add(injector.getInstance(UserComponent.class));
        retCloudComponents.add(new ErpConnectionComponent(this.injector));
        retCloudComponents.add(new QueryComponent(this.injector));

        return retCloudComponents;
    }

    @Override
    public String getServiceName() {
        return "otocloud-auth";
    }

}

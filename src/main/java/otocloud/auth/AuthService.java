package otocloud.auth;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import otocloud.auth.post.AuthenticationComponent;
import otocloud.auth.user.UserComponent;
import otocloud.framework.core.HandlerDescriptor;
import otocloud.framework.core.OtoCloudComponent;
import otocloud.framework.core.OtoCloudService;
import otocloud.framework.core.OtoCloudServiceForVerticleImpl;
import otocloud.framework.core.OtoCloudServiceImpl;
import otocloud.persistence.dao.MongoDataSource;

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
    
    private MongoDataSource authSrvMongoDataSource;
    
	public MongoDataSource getAuthSrvMongoDataSource() {
		return authSrvMongoDataSource;
	}

	@Override
	public void afterInit(Future<Void> initFuture) {		
        //如果有mongo_client配置,放入上下文当中.
        if (this.srvCfg.containsKey("mongo_client")) {
            JsonObject mongoClientCfg = this.srvCfg.getJsonObject("mongo_client");
	        if(mongoClientCfg != null){
	        	authSrvMongoDataSource = new MongoDataSource();
	        	authSrvMongoDataSource.init(vertxInstance, mongoClientCfg);				
	        }
        }
        
        super.afterInit(initFuture);        
	}


    /**
     * 在指定Vertx实例上添加授权接口.
     *
     * @param vertx
     */
/*    private void initOAuthService(Vertx vertx) {
        //Context context = vertx.getOrCreateContext();
        Router mainRouter = ShareableUtil.getMainRouter(vertx);

        mainRouter.route(HttpMethod.GET, "/authorize").handler(routing -> {
            System.out.println("OAuthService, authorize");

            routing.next();
        });

        mainRouter.route(HttpMethod.POST, "/token").handler(routing -> {
            System.out.println("OAuthService, token");

            routing.next();
        });
    }*/

 
    @Override
    public void start(Future<Void> startFuture) throws Exception {
        Future<Void> authFuture = Future.future();

        authFuture.setHandler(result -> {
            if (result.succeeded()) {

                //向Webserver发起请求,开启授权功能.
                //turnOnAuthOnWebServer(startFuture);
                
                startFuture.complete();
            } else {
                startFuture.fail(result.cause());
            }
        });

        super.start(authFuture);
    }


    /**
     * 开启WebServer的授权功能.
     *
     * @param startFuture
     */
/*    private void turnOnAuthOnWebServer(Future<Void> startFuture) {
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
    }*/

    /**
     * 设置不进行身份验证的API.
     * @return
     */
/*    private JsonArray getSecurityUrls(){
        JsonArray urls = new JsonArray();
        urls.add("/api/otocloud-auth/user-management/users/verify");
        urls.add("/api/otocloud-auth/user-management/users/actions/logout");
        urls.add("/api/otocloud-auth/user-management/users/actions/erp/login");
        urls.add("/api/otocloud-acct/account"); //注册接口.

        return urls;
    }*/

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

        retCloudComponents.add(new UserComponent());
        retCloudComponents.add(new AuthenticationComponent());
        
        //erp连接组件，暂时注销
        //retCloudComponents.add(new ErpConnectionComponent(this.injector));

        return retCloudComponents;
    }

    @Override
    public String getServiceName() {
        return "otocloud-auth";
    }
    
    
    public static void main( String[] args )
    {
    	AuthService app = new AuthService();

    	OtoCloudServiceImpl.internalMain("log4j2.xml",
    										"otocloud-auth.json", 
    										app);
    	
    }   

}

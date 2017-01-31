/*package otocloud.auth.verticle;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import otocloud.framework.core.OtoCloudServiceDepOptions;
import otocloud.webserver.WebServerVerticle;

*//**
 * 1. 启动WebServer.
 * 2. 注册AuthService.
 * <p>
 * zhangyef@yonyou.com on 2015-12-16.
 *//*
public class AuthServiceDaemonWithMockUp {

    public static final String WEBSERVER_NAME = "121webserver-N01";
    protected static final Logger logger = LoggerFactory.getLogger(AuthServiceDaemon.class);
    *//**
     * 启动WebServer的端口.
     *//*
    private static final int HTTP_PORT = 8081;
    *//**
     * 配置WebServer的Mongo地址.
     * 注: AuthService的Mongo地址在Auth的配置文件中进行配置.
     *//*
//    private static final String MONGO_HOST = "localhost";
    private static final String MONGO_HOST = "10.10.23.112";
    private static final int MONGO_PORT = 27017;
    private static String authServiceDeployId = null;
    private static String webServerDeployId = null;
    private final Vertx vertx;

    //GatewayMockUp gatewayMockUp;

    public AuthServiceDaemonWithMockUp(Vertx vertx) {
        this.vertx = vertx;
        //this.gatewayMockUp = new GatewayMockUp();
    }

    *//**
     * 单独启动,方便前端页面调试.
     *
     * @param args
     *//*
    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();

        AuthServiceDaemonWithMockUp authServiceDaemon;

        authServiceDaemon = new AuthServiceDaemonWithMockUp(vertx);
        Future<Void> daemonFuture = Future.future();
        authServiceDaemon.start(daemonFuture);

        daemonFuture.setHandler(ret -> {
            if (ret.failed()) {
                logger.fatal("AuthService 后台进程启用失败.");
                return;
            }
            logger.info("Auth服务的访问地址是: http://localhost:" + HTTP_PORT);
        });
    }

    public void start(Future<Void> completeFuture) {
        Future<Void> future = Future.future();
        startWebServer(future);
        future.setHandler(result -> {
            if (result.succeeded()) {
                startAuthService(completeFuture);
            } else {
                logger.error("无法启动WebServer.");
            }
        });

        //单独运行时使用, 测试时不能使用
        //this.gatewayMockUp.mockUpLogin();
    }

    public void stop(Future<Void> completeFuture) {
        //单独运行时使用,测试时不能使用.
        //this.gatewayMockUp.tearDownLogin();

        vertx.undeploy(authServiceDeployId, result -> {
            if (result.succeeded()) {
                vertx.undeploy(webServerDeployId, ret -> {
                    vertx.close(
                            closeRet -> {
                                if (closeRet.succeeded()) {
                                    completeFuture.complete();
                                } else {
                                    completeFuture.fail("AuthService 无法停止.");
                                }
                            }
                    );
                });
            }
        });
    }

    *//**
     * 按照常量 otocloud.auth.verticle.AuthServiceDaemon#HTTP_PORT 启动WebServer.
     *
     * @param future
     *//*
    private void startWebServer(Future<Void> future) {

        //配置WebServer
        JsonObject deployConfig = new JsonObject();
        deployConfig.put("http.port", HTTP_PORT);
        deployConfig.put("webserver_name", WEBSERVER_NAME);
//        deployConfig.put("mongo_client", new JsonObject().put("host", MONGO_HOST).put("port", MONGO_PORT));
        DeploymentOptions deploymentOptions = new DeploymentOptions().setConfig(deployConfig);

        vertx.deployVerticle(WebServerVerticle.class.getName(), deploymentOptions, result -> {
            String deployId = result.result();
            webServerDeployId = deployId;
            System.out.println("WebServer 部署成功 [" + deployId + "]");
            future.complete();
        });
    }

    private void startAuthService(Future<Void> completeFuture) {

        Future<JsonObject> future = Future.future();

        //TODO 需要考虑在部署环境下的测试，尤其是对于配置文件的读取。
        String cfgFilePath = System.getProperty("user.dir") + "/target/config/" + "otocloud-auth.json";

        Vertx.vertx().fileSystem().readFile(cfgFilePath, result -> {
            if (result.succeeded()) {
                String fileContent = result.result().toString();
                System.out.println(fileContent);
                JsonObject srvCfg = new JsonObject(fileContent);

                future.complete(srvCfg);
            }
        });

        future.setHandler(result -> {
            JsonObject deployConfig = result.result();
            JsonObject options = deployConfig.getJsonObject("options");
            OtoCloudServiceDepOptions deploymentOptions = new OtoCloudServiceDepOptions();
            deploymentOptions.fromJson(options);
            vertx.deployVerticle(AuthService.class.getName(), deploymentOptions, depResult -> {
                if (depResult.failed()) {
                    logger.fatal("部署AuthService失败.");
                    completeFuture.fail("无法部署AuthService.");
                    return;
                }
                String authDepId = depResult.result();
                if (authDepId == null) {
                    completeFuture.fail("AuthService 无法启动。");
                }
                authServiceDeployId = authDepId;
                System.out.println("AuthService 部署成功 [" + authDepId + "]");
                completeFuture.complete();
            });
        });

    }


}
*/
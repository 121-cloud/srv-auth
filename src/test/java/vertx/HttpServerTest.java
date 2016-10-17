package vertx;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;

import java.util.concurrent.CountDownLatch;

/**
 * zhangyef@yonyou.com on 2015-11-12.
 */
public class HttpServerTest {
    protected static Logger logger = LoggerFactory.getLogger(HttpServerTest.class);

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        CountDownLatch latch = new CountDownLatch(2);

        vertx.deployVerticle(OAuthServerVerticle.class.getName(), authRet -> {
            if (authRet.succeeded()) {
                latch.countDown();
                logger.info("Start Verticles successfully.");
            }
        });

        vertx.deployVerticle(ServerVerticle.class.getName(), ret -> {
            if (ret.succeeded()) {

                latch.countDown();
                logger.info("ServerVerticle deployed.");
            }
        });

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private static void testDoubleServer() {
        Vertx vertx = Vertx.vertx();

        int port = 8081;

        HttpServer httpServer1 = vertx.createHttpServer();
        Router router1 = Router.router(vertx);
        router1.route(HttpMethod.GET, "/api/status").handler(context -> {
            logger.info("/api/status");
        });

        HttpServer httpServer2 = vertx.createHttpServer();
        Router router2 = Router.router(vertx);
        router2.route(HttpMethod.GET, "/api/authorize").handler(context -> {
            logger.info("/api/authorize");
        });

        httpServer1.requestHandler(router1::accept);

        httpServer1.listen(port, ret -> {
            if (ret.succeeded()) {
                logger.info("Server1 listens successfully");
            }
        });

        httpServer2.requestHandler(router2::accept);
        httpServer2.listen(port, ret -> {
            if (ret.succeeded()) {
                logger.info("Server2 listens successfully");
            }
        });
    }
}

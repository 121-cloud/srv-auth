package otocloud.auth.verticle;

import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import mockit.Mock;
import mockit.MockUp;
import otocloud.common.Command;
import otocloud.common.CommandDeliveryOptions;
import otocloud.common.CommandResult;
import otocloud.common.CommandScheme;

/**
 * zhangyef@yonyou.com on 2015-12-30.
 */
public class GatewayMockUp {

    private MockUp gatewayMockUp;
    private MockUp loginMockUp;

    public void mockUpLogin(){
        loginMockUp = new MockUp<Command>() {
            /**
             * 假设登录成功.
             * @param vertx
             * @param replyHandler
             */
            @Mock
            public void executeOnGateway(Vertx vertx, Handler<CommandResult> replyHandler) {
                JsonObject resultJson = new JsonObject();
                resultJson.put(CommandScheme.RESULT_STATUS_CODE, 0);
                JsonArray datas = new JsonArray();
                datas.add(new JsonObject().put("result_code", 0));
                resultJson.put(CommandScheme.RESULT_DATA, datas);
                CommandResult result = CommandResult.fromJson(resultJson.toString());

                replyHandler.handle(result);
            }
        };
    }

    public void tearDownLogin(){
        loginMockUp.tearDown();
    }

    public void mockUpImport(){
        gatewayMockUp = new MockUp<Command>() {
            /**
             * 假设导入成功.
             * @param vertx
             * @param replyHandler
             */
            @Mock
            public void executeOnGateway(Vertx vertx, CommandDeliveryOptions options, boolean multiTimeReply, Handler<CommandResult> replyHandler)  {
                JsonObject resultJson = new JsonObject();

                JsonArray datas = new JsonArray();

                JsonObject user = new JsonObject();
                user.put("name", "erp1");
                user.put("email", "erp1@yonyou.com");

                datas.add(user);

                resultJson.put(CommandScheme.RESULT_DATA, datas);
                resultJson.put(CommandScheme.RESULT_STATUS_CODE, 0); //用于分批提交时, 完成的标志.

                CommandResult result = CommandResult.fromJson(resultJson.toString());
                result.setDatas(datas);
                result.put(CommandScheme.TOTAL, 1);
                result.put(CommandScheme.DELTA, 1);

                replyHandler.handle(result);
            }
        };
    }

    public void tearDownImport(){
        gatewayMockUp.tearDown();
    }
}

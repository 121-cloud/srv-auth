package otocloud.auth.command;

import com.google.inject.Inject;
import io.vertx.core.Future;
import io.vertx.core.MultiMap;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import otocloud.auth.common.framework.CommandContext;
import otocloud.auth.common.framework.OtoCommand;

import java.util.Iterator;

/**
 * Created by zhangye on 2015-10-10.
 */
//@LazySingleton
public class CommandDispatcher {
    public static final String HTTP_METHOD = "method";
    public static final String INNER_OPERATION = "operation";

    @Inject
    CommandFactory factory;

    public void dispatch(Message<JsonObject> message) {
        if (isFromBrowser(message)) {
            MultiMap headers = message.headers();
            String opt = headers.get(HTTP_METHOD);

            JsonObject body = message.body();

            return;
        }

        String operation = message.headers().get(INNER_OPERATION);
        OtoCommand command = factory.getCommand(operation);

        CommandContext context = getContext(message.body());

        Future<JsonObject> cmdFuture = Future.future();
        command.executeFuture(context, cmdFuture);

        cmdFuture.setHandler(ret -> {
            if (ret.succeeded()) {
                message.reply(ret.result());
            } else {
                message.fail(1, "Fail");
            }
        });
    }

    private boolean isFromBrowser(Message<JsonObject> message) {
        return message.headers().contains(HTTP_METHOD);
    }


    private CommandContext getContext(JsonObject body) {
        CommandContext context = new CommandContext();
        Iterator<String> fieldItr = body.fieldNames().iterator();
        while (fieldItr.hasNext()) {
            String name = fieldItr.next();
            context.put(name, body.getValue(name));
        }
        return context;
    }
}

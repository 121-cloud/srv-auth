package otocloud.auth.common.framework;

import io.vertx.core.Future;
import io.vertx.core.Handler;


/**
 * Created by zhangye on 2015-10-08.
 */
public interface OtoCommand<R> {
    void execute();
    void execute(IContext context);

    void execute(IContext context, Handler<R> resultHandler);
    void executeFuture(CommandContext context, Future<R> future);
}

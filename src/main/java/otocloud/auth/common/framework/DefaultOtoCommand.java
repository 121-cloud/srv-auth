package otocloud.auth.common.framework;

import io.vertx.core.Future;
import io.vertx.core.Handler;


/**
 * Created by zhangye on 2015-10-09.
 */
public abstract class DefaultOtoCommand<R> implements OtoCommand<R> {
    @Override
    public void execute() {
        execute(new CommandContext());
    }

    @Override
    public void execute(IContext context) {
        execute(context, event -> {
        });
    }

    @Override
    public void execute(IContext context, Handler<R> resultHandler){

    }

    @Override
    public abstract void executeFuture(CommandContext context, Future<R> future);
}

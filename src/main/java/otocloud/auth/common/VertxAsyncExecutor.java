package otocloud.auth.common;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Vertx;

/**
 * 通过Vertx异步执行一段代码.
 * <p>
 * zhangyef@yonyou.com on 2015-10-29.
 */
public class VertxAsyncExecutor {

    private Vertx vertx;

    /**
     * 使用已知Vertx实例进行异步调用。
     * @param vertx
     * @return
     */
    public static VertxAsyncExecutor create(Vertx vertx){
        return new VertxAsyncExecutor(vertx);
    }

    /**
     * 使用新生成的Vertx实例进行异步调用。
     * @return
     */
    public static VertxAsyncExecutor create(){
        return new VertxAsyncExecutor(Vertx.vertx());
    }

    private VertxAsyncExecutor(Vertx vertx) {
        this.vertx = vertx;
    }

    public Vertx getVertx() {
        return vertx;
    }

    public void setVertx(Vertx vertx) {
        this.vertx = vertx;
    }



    /**
     * 异步执行指定的代码段, 并通过future返回执行结果.
     *
     * @param code       将要执行的代码段. 遇到异常，直接抛出异常.
     * @param doneFuture 执行结束后回调.
     * @param <T>        代码执行结果的类型, 即返回类型.
     */
    public <T> void execute(CodeBlock<T> code, Future<T> doneFuture) {
        vertx.executeBlocking(future -> {
            try {
                T result = code.execute();
                future.complete(result);
            } catch (Exception e) {
                future.fail(e);
            }
        }, (AsyncResult<T> ret) -> {
            if (ret.succeeded()) {
                T result = ret.result();
                doneFuture.complete(result);
            } else {
                doneFuture.fail(new RuntimeException(ret.cause()));
            }
        });
    }

    /**
     * 异步执行没有返回值的代码段.
     *
     * @param code       代码段.
     * @param voidFuture 代码结束后调用.
     */
    public void executeVoid(VoidCodeBlock code, Future<Void> voidFuture) {
        vertx.executeBlocking(future -> {
            try {
                code.execute();
            } catch (Exception e) {
                future.fail(e);
            }
        }, (AsyncResult<Void> ret) -> {
            if (ret.succeeded()) {
                voidFuture.complete();
            } else {
                voidFuture.fail(new RuntimeException(ret.cause()));
            }
        });
    }

    /**
     * 表示具有返回值的一段代码块.
     *
     * @param <T> 返回值类型.
     */
    @FunctionalInterface
    public interface CodeBlock<T> {
        T execute();
    }

    /**
     * 处理没有返回值的情况.
     */
    @FunctionalInterface
    public interface VoidCodeBlock {
        void execute() throws Exception;
    }
}

/*package otocloud.auth.common;

import io.vertx.core.Future;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;

*//**
 * zhangyef@yonyou.com on 2015-11-10.
 *//*
@RunWith(VertxUnitRunner.class)
public class VertxAsyncExecutorTest {
    @Test
    public void it_should_count_down_3_tasks(TestContext context) {
        final Async async = context.async();

        VertxAsyncExecutor executor = VertxAsyncExecutor.create();
        Future<Integer> future = Future.future();

        executor.execute(()->{
            CountDownLatch latch = new CountDownLatch(3);

            run_task_in_a_new_thread(1, latch, 1000);
            run_task_in_a_new_thread(2, latch, 1000);
            run_task_in_a_new_thread(3, latch, 1000);
            try {
                latch.await();
            } catch (InterruptedException e) {
                Assert.fail();
            }

            return 3;
        }, future);

        future.setHandler(ret -> {
            if(ret.succeeded()){
                System.out.println(ret.result());
                async.complete();
                async.await();
            }
        });
    }

    private void run_task_in_a_new_thread(int order, CountDownLatch latch, long millis){
        Thread t = new Thread(()->{
            try {
                Thread.sleep(millis);
                System.out.println(order + ", Finished: " + millis);
                latch.countDown();

            } catch (InterruptedException e) {
                Assert.fail();
            }
        });

        t.start();
    }
}
*/
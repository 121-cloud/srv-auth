package otocloud.auth.common;

import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import otocloud.auth.entity.MonitorInfoSchema;

import java.util.Objects;

/**
 * 向浏览器报告任务进度.
 * zhangyef@yonyou.com on 2015-12-29.
 */
public class ProcessReporter {
    private final int totalWork;
    protected Logger logger = LoggerFactory.getLogger(ProcessReporter.class);
    private JsonObject monitorInfo = new JsonObject();
    private int deltaWork;
    private int finishedWork;

    private String monitorAddress;

    private Vertx vertx;
    private EventBus bus;

    /**
     * 默认总工作值是100.
     *
     * @param vertx
     * @param monitorAddress
     */
    public ProcessReporter(Vertx vertx, String monitorAddress) {
        this(100, vertx, monitorAddress);
    }

    /**
     * @param totalWork      总工作值.
     * @param vertx
     * @param monitorAddress
     */
    public ProcessReporter(int totalWork, Vertx vertx, String monitorAddress) {
        Objects.requireNonNull(monitorAddress, "进度监听地址为空.");

        if (totalWork < 0) {
            throw new IllegalArgumentException("违反了约束: totalWork >= 0");
        }

        this.totalWork = totalWork;
        reset();

        this.monitorAddress = monitorAddress.trim();

        this.vertx = vertx;
        this.bus = this.vertx.eventBus();
    }

    public void reset() {
        this.deltaWork = 0;
        this.finishedWork = 0;

        monitorInfo.clear();

        monitorInfo.put(MonitorInfoSchema.TOTAL_WORK, this.totalWork);
        monitorInfo.put(MonitorInfoSchema.DELTA_WORK, this.deltaWork);
        monitorInfo.put(MonitorInfoSchema.FINISHED_WORK, this.finishedWork);
    }

    public void work(int deltaWork) {
        if (this.finishedWork == this.totalWork) {
            throw new IllegalArgumentException("Works have been finished. Please call [reset] method before " +
                    "[work] again.");
        }
        this.deltaWork = deltaWork;
        this.finishedWork += this.deltaWork;
        if (this.finishedWork > this.totalWork) {
            this.finishedWork = this.totalWork;
        }

        this.monitorInfo.put(MonitorInfoSchema.DELTA_WORK, this.deltaWork);
        this.monitorInfo.put(MonitorInfoSchema.FINISHED_WORK, this.finishedWork);

        logger.info("完成了进度: " + deltaWork);
        //发送消息的拷贝,使得各个消息之间不会相互干扰.
        bus.send(this.monitorAddress, this.monitorInfo.copy());
    }

    /**
     * 强制完成.
     */
    public void finish() {
        work(this.totalWork - this.finishedWork);
    }
}

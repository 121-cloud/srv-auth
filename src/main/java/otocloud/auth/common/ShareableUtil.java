package otocloud.auth.common;

import io.vertx.core.Vertx;
import io.vertx.core.shareddata.LocalMap;
import io.vertx.ext.web.Router;
import otocloud.common.ShareableData;

/**
 * 从指定Vertx实例中提取需要的共享对象.
 * 注意,根据{@link io.vertx.core.shareddata.Shareable Shareable}接口的要求,被共享的对象应是线程安全的.
 * <p>
 * zhangyef@yonyou.com on 2015-11-13.
 *
 * @see io.vertx.core.shareddata.Shareable
 */
public class ShareableUtil {

    /**
     * 因为RouterImpl是线程安全的,所以可使用Shareable接口,实现在一个vertx实例中共享.
     *
     * @param vertx Vertx实例.
     * @return WebServer所持有的主要路由器.
     */
    public static Router getMainRouter(Vertx vertx) {
        Router mainRouter;

        //WebServer表示由WebServer管理的共享数据.
        LocalMap<String, ShareableData> localMap = vertx.sharedData().getLocalMap("WebServer");
        ShareableData data = localMap.get("router");
        if (data != null) {
            mainRouter = (Router) data.getData();
        } else {
            mainRouter = Router.router(vertx);
            localMap.put("router", new ShareableData(mainRouter));
        }

        return mainRouter;
    }
}

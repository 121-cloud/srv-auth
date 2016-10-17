package otocloud.auth.verticle;

import com.google.inject.Injector;
import otocloud.auth.handler.query.UserStaticsHandler;
import otocloud.framework.core.OtoCloudComponentImpl;
import otocloud.framework.core.OtoCloudEventHandlerRegistry;

import java.util.ArrayList;
import java.util.List;

/**
 * zhangyef@yonyou.com on 2016-01-22.
 */
public class QueryComponent extends OtoCloudComponentImpl {
    private static final String COMPONENT_NAME = "query-management";

    private Injector injector;

    private UserStaticsHandler userStaticsHandler;

    public QueryComponent(Injector injector) {
        this.injector = injector;
        this.userStaticsHandler = new UserStaticsHandler(this, injector);
    }

    @Override
    public String getName() {
        return COMPONENT_NAME;
    }

    @Override
    public List<OtoCloudEventHandlerRegistry> registerEventHandlers() {
        List<OtoCloudEventHandlerRegistry> ret = new ArrayList<OtoCloudEventHandlerRegistry>();
        ret.add(userStaticsHandler);
        return ret;
    }
}

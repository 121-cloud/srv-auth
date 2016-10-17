package otocloud.auth.verticle;

import com.google.inject.Inject;
import com.google.inject.Injector;
import otocloud.auth.handler.erpconnection.ErpAccountBindHandler;
import otocloud.auth.handler.erpconnection.ErpAccountBindInfoHandler;
import otocloud.auth.handler.erpconnection.ErpAccountUnbindHandler;
import otocloud.framework.core.OtoCloudComponentImpl;
import otocloud.framework.core.OtoCloudEventHandlerRegistry;

import java.util.ArrayList;
import java.util.List;

/**
 * 处理与ERP连接的有关接口.
 * 如绑定ERP账户.
 * zhangyef@yonyou.com on 2016-01-20.
 */
public class ErpConnectionComponent extends OtoCloudComponentImpl {
    private static final String COMPONENT_NAME = "erp-connection";

    private Injector injector;
    private ErpAccountBindHandler erpAccountBindHandler;
    private ErpAccountBindInfoHandler erpAccountBindInfoHandler;
    private ErpAccountUnbindHandler erpAccountUnbindHandler;

    public ErpConnectionComponent(Injector injector) {
        this.injector = injector;
        erpAccountBindHandler = new ErpAccountBindHandler(this, injector);
        erpAccountBindInfoHandler = new ErpAccountBindInfoHandler(this, injector);
        erpAccountUnbindHandler = new ErpAccountUnbindHandler(this, injector);
    }



    @Override
    public String getName() {
        return COMPONENT_NAME;
    }

    @Override
    public List<OtoCloudEventHandlerRegistry> registerEventHandlers() {
        List<OtoCloudEventHandlerRegistry> ret = new ArrayList<OtoCloudEventHandlerRegistry>();
        ret.add(erpAccountBindHandler);
        ret.add(erpAccountBindInfoHandler);
        ret.add(erpAccountUnbindHandler);
        return ret;
    }
}

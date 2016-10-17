package otocloud.auth.verticle;

import otocloud.framework.core.OtoCloudComponentImpl;
import otocloud.framework.core.OtoCloudEventHandlerRegistry;

import java.util.List;

/**
 * 管理身份认证接口, 如登录和退出等.
 * zhangyef@yonyou.com on 2016-01-11.
 */
public class AuthenticationComponent extends OtoCloudComponentImpl {
    @Override
    public String getName() {
        return null;
    }

    @Override
    public List<OtoCloudEventHandlerRegistry> registerEventHandlers() {
        return null;
    }
}

package otocloud.auth.authorization;

import otocloud.framework.core.OtoCloudComponentImpl;
import otocloud.framework.core.OtoCloudEventHandlerRegistry;

import java.util.ArrayList;
import java.util.List;

/**
 * 权限接口
 * zhangyef@yonyou.com on 2016-01-11.
 */
public class AuthenticationComponent extends OtoCloudComponentImpl {
	private static final String COMPONENT_NAME = "authentication";
	
    public AuthenticationComponent() {
		super();
		// TODO Auto-generated constructor stub
	}

    @Override
    public String getName() {
        return COMPONENT_NAME;
    }

    @Override
    public List<OtoCloudEventHandlerRegistry> registerEventHandlers() {
        List<OtoCloudEventHandlerRegistry> ret = new ArrayList<OtoCloudEventHandlerRegistry>();

        ret.add(new ActivityAuthCreationHandler(this));
        ret.add(new ActivityAuthDeleteHandler(this));
        ret.add(new ActivityAuthQueryHandler(this));

        return ret;
    }
    
}

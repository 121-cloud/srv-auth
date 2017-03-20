package otocloud.auth.admin;

//import otocloud.acct.org.BizUnitQueryHandler;

import otocloud.framework.core.OtoCloudComponentImpl;
import otocloud.framework.core.OtoCloudEventHandlerRegistry;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangye on 2015-10-14.
 */
//@LazySingleton
public class AdminComponent extends OtoCloudComponentImpl {

	private static final String USER_COMPONENT_NAME = "user-management";

    public AdminComponent() {
		super();
		// TODO Auto-generated constructor stub
	}

    @Override
    public String getName() {
        return USER_COMPONENT_NAME;
    }

    @Override
    public List<OtoCloudEventHandlerRegistry> registerEventHandlers() {
        List<OtoCloudEventHandlerRegistry> ret = new ArrayList<OtoCloudEventHandlerRegistry>();

        ret.add(new AdminRegisterHandler(this));
        ret.add(new UserLoginHandler(this));        
        ret.add(new UserLogoutHandler(this));
        ret.add(new UserUpdateHandler(this));
        ret.add(new AdminDeleteHandler(this));
        ret.add(new CellNoQueryHandler(this));
        ret.add(new AcctOwnerGetHandler(this));
        ret.add(new AuthenticationHandler(this));
        ret.add(new UserCreationHandler(this));
        ret.add(new UserDeleteHandler(this));
        ret.add(new UserQueryHandler(this));

        return ret;
    }
    
}

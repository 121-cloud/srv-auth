package otocloud.auth.verticle;

import otocloud.auth.handler.*;
import otocloud.framework.core.OtoCloudComponentImpl;
import otocloud.framework.core.OtoCloudEventHandlerRegistry;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangye on 2015-10-14.
 */
//@LazySingleton
public class UserComponent extends OtoCloudComponentImpl {

	private static final String USER_COMPONENT_NAME = "user-management";

    public static final String MANAGE_USER_ADDRESS = "users";//响应用户注册

	
    public UserComponent() {
		super();
		// TODO Auto-generated constructor stub
	}

    @Override
    public String getName() {
        return USER_COMPONENT_NAME;
    }

    public static String getComponentName(){
        return USER_COMPONENT_NAME;
    }

    @Override
    public List<OtoCloudEventHandlerRegistry> registerEventHandlers() {
        List<OtoCloudEventHandlerRegistry> ret = new ArrayList<OtoCloudEventHandlerRegistry>();

        ret.add(new UserRegisterHandler(this));
        ret.add(new UserLoginHandler(this));        
        ret.add(new UserLogoutHandler(this));
        ret.add(new UserUpdateHandler(this));
        ret.add(new UserDeleteHandler(this));
        ret.add(new CellNoQueryHandler(this));
        ret.add(new TokenQueryHandler(this));
        ret.add(new BizUnitQueryHandler(this));
        ret.add(new UserQueryHandler(this));
        ret.add(new UserCreationHandler(this));
        ret.add(new UserAuthenticationHandler(this));

        return ret;
    }
    
}

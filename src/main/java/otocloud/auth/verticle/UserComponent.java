package otocloud.auth.verticle;

import com.google.inject.Inject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import otocloud.auth.handler.*;
import otocloud.auth.handler.messagechecker.UserCreationChecker;
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

    protected Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    public static final String MANAGE_USER_ADDRESS = "users";//响应用户注册

    @Inject
    private UserRegisterHandler userRegisterHandler;

    @Inject
    private UserLoginHandler userLoginHandler;

    @Inject
    private UserLogoutHandler userLogoutHandler;

    @Inject
    private UserUpdateHandler userUpdateHandler;

    @Inject
    private UserDeleteHandler userDeleteHandler;

    @Inject
    private CellNoQueryHandler cellNoQueryHandler;

    @Inject
    private TokenQueryHandler tokenQueryHandler;

    @Inject
    private DepartmentQueryHandler departmentQueryHandler;

    @Inject
    private UserQueryHandler userQueryHandler;

    @Inject
    private ErpUserLoginHandler erpUserLoginHandler;

    @Inject
    private ErpUserImportHandler erpUserImportHandler;

    @Inject
    private UserCreationHandler userCreationHandler;

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

        ret.add(userRegisterHandler);
        
        //UserLoginHandler userLoginHandler = new UserLoginHandler(this);
        ret.add(userLoginHandler);
        
        ret.add(userLogoutHandler);
        ret.add(userUpdateHandler);
        ret.add(userDeleteHandler);
        ret.add(cellNoQueryHandler);
        ret.add(tokenQueryHandler);
        ret.add(departmentQueryHandler);
        ret.add(userQueryHandler);
        ret.add(erpUserLoginHandler);
        ret.add(erpUserImportHandler);
        ret.add(userCreationHandler);

        return ret;
    }
}

package otocloud.auth.command;

import com.google.inject.Inject;
import otocloud.auth.common.framework.OtoCommand;

import javax.annotation.PostConstruct;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by zhangye on 2015-10-10.
 */
//@LazySingleton
public class CommandFactory {

    private ConcurrentHashMap<String, OtoCommand> commandHashMap = new ConcurrentHashMap<>();

    @Inject
    AddUserCommand addUserCommand;

    @Inject
    DeleteUserCommand deleteUserCommand;

    @Inject
    UpdateUserCommand updateUserCommand;

    @Inject
    QueryUserCommand queryUserCommand;

    @Inject
    LoginCommand loginCommand;

    @Inject
    LogoutCommand logoutCommand;

    public CommandFactory(){

    }

    @PostConstruct
    private void init(){
        commandHashMap.put("add", addUserCommand);
        commandHashMap.put("delete", deleteUserCommand);
        commandHashMap.put("update", updateUserCommand);
        commandHashMap.put("query", queryUserCommand);
        commandHashMap.put("login", loginCommand);
        commandHashMap.put("logout", logoutCommand);
    }
    public OtoCommand getCommand(String operation) {
        return commandHashMap.get(operation);
    }
}

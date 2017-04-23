package otocloud.auth.admin;

import io.vertx.core.Future;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.sql.UpdateResult;

import org.jasypt.util.password.StrongPasswordEncryptor;

import otocloud.auth.common.RSAUtil;
import otocloud.auth.dao.UserDAO;
import otocloud.common.ActionURI;
import otocloud.framework.core.CommandMessage;
import otocloud.framework.core.HandlerDescriptor;
import otocloud.framework.core.OtoCloudComponentImpl;
import otocloud.framework.core.OtoCloudEventHandlerImpl;

/**
 * Created by zhangye on 2015-10-20.
 */

public class UserUpdateHandler extends OtoCloudEventHandlerImpl<JsonObject> {
	

	private static final String ADDRESS = "update";
	
    /**
     * 线程安全的加密类.
     */
    private final StrongPasswordEncryptor passwordEncryptor;

    public UserUpdateHandler(OtoCloudComponentImpl componentImpl) {
        super(componentImpl);
        
        passwordEncryptor = new StrongPasswordEncryptor();
    }
    
    /**
     * 对密码明文加密.
     *
     * @param userPassword
     * @return
     */
    private String encryptPassword(String userPassword) {
        //使用SHA-256加密算法
        return passwordEncryptor.encryptPassword(userPassword);
    }
    
    
    /* 
     * { 
     * 		  id: 
	 *	      name: 用户名
	 *	      password: 
	 *	      cell_no: 电话
	 *	      email: 邮箱	  
     * }
     * 
     */
    @Override
    public void handle(CommandMessage<JsonObject> msg) {
/*        boolean isLegal = busMessageChecker.checkUpdateUserInfo(msg.body(), errMsg -> {
            msg.fail(ErrCode.BUS_MSG_FORMAT_ERR.getCode(), errMsg);
        });

        if (!isLegal) {
            return;
        }*/
    	
    	JsonObject body = msg.body();
    	
		JsonObject params = body.getJsonObject("queryParams");		
		
		Long userId = Long.parseLong(params.getString("id"));
    	
        JsonObject content = body.getJsonObject("content");
    	
    	//Long userId = content.getLong("id", 0L);
    	String name = content.getString("name", "");
    	String pwd = content.getString("password", "");
    	String cell_no = content.getString("cell_no", "");
    	String email = content.getString("email", "");    
    	
        //解密来自客户端的密码
        String plainText = RSAUtil.decrypt(pwd, "ufsoft*123");
        //加密用户密码并存储
        String encryPwd = encryptPassword(plainText);

        Future<UpdateResult> future = Future.future();
        
        UserDAO userDAO = new UserDAO(this.componentImpl.getSysDatasource());
        userDAO.update(userId, name, encryPwd, cell_no, email, future);

        future.setHandler(ret -> {
            if (ret.succeeded()) {
                msg.reply(ret.result().toJson());
            } else {
            	Throwable errThrowable = ret.cause();
    			String errMsgString = errThrowable.getMessage();
    			this.componentImpl.getLogger().error(errMsgString, errThrowable);
    			msg.fail(100, errMsgString);
            }
        });
    }


    @Override
    public String getEventAddress() {
        return ADDRESS;
    }

    @Override
    public HandlerDescriptor getHanlderDesc() {
        HandlerDescriptor handlerDescriptor = super.getHanlderDesc();
        ActionURI uri = new ActionURI(ADDRESS + "/:id", HttpMethod.PUT);
        handlerDescriptor.setRestApiURI(uri);
        return handlerDescriptor;
    }
}

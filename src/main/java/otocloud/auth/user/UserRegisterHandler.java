package otocloud.auth.user;

import javax.validation.ConstraintViolationException;

import org.apache.commons.lang3.StringUtils;
import org.jasypt.util.password.StrongPasswordEncryptor;

import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import otocloud.auth.common.ErrCode;
import otocloud.auth.common.RSAUtil;
import otocloud.auth.common.Required;
import otocloud.auth.common.ViolationMessageBuilder;
import otocloud.auth.dao.UserDAO;
import otocloud.auth.user.UserComponent;
import otocloud.common.ActionURI;
import otocloud.common.SessionSchema;
import otocloud.framework.core.HandlerDescriptor;
import otocloud.framework.core.OtoCloudBusMessage;
import otocloud.framework.core.OtoCloudComponentImpl;
import otocloud.framework.core.OtoCloudEventHandlerImpl;

/**
 * 向系统中添加一个管理员用户.
 * Created by zhangye on 2015-10-14.
 */
public class UserRegisterHandler extends OtoCloudEventHandlerImpl<JsonObject> {
	
    /**
     * 线程安全的加密类.
     */
    private final StrongPasswordEncryptor passwordEncryptor;
	

    public UserRegisterHandler(OtoCloudComponentImpl componentImpl) {
        super(componentImpl);
        
        passwordEncryptor = new StrongPasswordEncryptor();
    }

    /**
     * 注册REST API。
     *
     * @return post /api/"服务名"/"组件名"/users
     */
    @Override
    public HandlerDescriptor getHanlderDesc() {
        HandlerDescriptor handlerDescriptor = super.getHanlderDesc();
        ActionURI uri = new ActionURI("users", HttpMethod.POST);
        handlerDescriptor.setRestApiURI(uri);
        return handlerDescriptor;
    }


    /**
     * 得到对应事件总线的地址。
     *
     * @return <服务名>.user-management.users.post
     */
    @Override
    public String getEventAddress() {
        return UserComponent.MANAGE_USER_ADDRESS + ".post";
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
    
    /**
     * 定义消息的content字段，触发验证操作.
     *
     * @param msg
     */
    private void triggerCheckContent(@Required({"content"}) JsonObject msg) {
        //do nothing.
    }
    
    /**
     * 定义消息格式的约束，触发验证操作.
     *
     * @param msg
     */
    private void triggerCheckCreateUserInfo(
            @Required({"name", "password", "org_acct_id", "cell_no", "email"}) JsonObject msg) {
        //do nothing.
    }

    /**
     * 检查消息总线是否符合"创建用户"的格式要求。
     *
     * @param msg        事件总线消息.
     * @param errHandler 错误处理.
     * @return true, 符合格式要求; false, 违反格式要求.
     */
    private boolean checkCreateUserInfo(JsonObject msg, Handler<String> errHandler) {

        try {
            triggerCheckContent(msg);
            triggerCheckCreateUserInfo(msg.getJsonObject("content").getJsonObject("user"));
            return true;
        } catch (ConstraintViolationException e) {
            if (errHandler != null) {
                errHandler.handle(ViolationMessageBuilder.build(e));
            }

            return false;
        }
    }

    
    

    /* 
     * {
     * 	  acct_id: 租户ID
     * 	  biz_unit_id: 业务单元ID
     * 	  mgr_post_id: IT管理员岗位ID
     * 	  user: {
     * 	  }
     * }
     * 
     */
    @Override
    public void handle(OtoCloudBusMessage<JsonObject> msg) {
        boolean isLegal = checkCreateUserInfo(msg.body(), errMsg -> {
            msg.fail(ErrCode.BUS_MSG_FORMAT_ERR.getCode(), errMsg);
        });

        if (!isLegal) {
            return;
        }

        JsonObject body = msg.body();
        JsonObject session = body.getJsonObject(SessionSchema.SESSION, null);
        
        JsonObject content = body.getJsonObject("content");
        Long acctId = content.getLong("acct_id");
        Long bizUnitId = content.getLong("biz_unit_id");
        Long mgrPostId = content.getLong("mgr_post_id");

        JsonObject userInfo = content.getJsonObject("user");
        //从Session中取出当前登录的用户ID
        Long currentUser = 0L; //默认为0，表示没有用户.
        if (session != null) {
            currentUser = session.getLong(SessionSchema.CURRENT_USER_ID, 0L);
        }
        userInfo.put("entry_id", currentUser);

        try {       
       
            //final User user = EntityFactory.fromJsonObject(userInfo, User.class);
        	
        	String cellNo = userInfo.getString("cell_no", "");

            //检查手机号字段是否为空.
            if (StringUtils.isBlank(cellNo)) {               	
				String errMsgString = "手机号不能为空.";
				this.componentImpl.getLogger().error(errMsgString);
				msg.fail(100, errMsgString);	
            }
            
            //解密来自客户端的密码
            String plainText = RSAUtil.decrypt(userInfo.getString("password", ""), "ufsoft*123");
            //加密用户密码并存储
            userInfo.put("password", encryptPassword(plainText));

            Future<Boolean> verifyFuture = Future.future();
            //检查手机号字段是否重复.(重复表示已经注册过)
            UserDAO userDAO = new UserDAO(this.componentImpl.getSysDatasource());
            userDAO.isRegisteredCellNo(cellNo, verifyFuture);
            
            verifyFuture.setHandler(ret -> {
                if (ret.failed()) {
					Throwable err = ret.cause();
					String errMsg = err.getMessage();
					componentImpl.getLogger().error(errMsg, err);	
					msg.fail(400, errMsg);
                    return;
                }

                boolean exists = ret.result();
                //手机号已经存在.
                if (exists) {
					String errMsg = "手机号已经存在, 不能重复注册.";
					componentImpl.getLogger().error(errMsg);	
					msg.fail(400, errMsg);
                    return;
                }

                Future<JsonObject> createUserfuture = Future.future();
                userDAO.create(userInfo, acctId, bizUnitId, mgrPostId, createUserfuture);
                createUserfuture.setHandler(userResult -> {
                    if (userResult.succeeded()) {
                    	JsonObject u = userResult.result();
                        //生成OpenID
                        /*String openId = UUID.randomUUID().toString();
                            u.setOpenID(openId);
*/
                        //usersLoggedIn.put(openId, u);

                        this.componentImpl.getLogger().info("管理员用户创建成功, userID：" + u.getLong("id"));
                        
                        try{                        
                        	msg.reply(u);
                        }catch (Exception e) {
                			String errMsgString = e.getMessage();
                			this.componentImpl.getLogger().error(errMsgString, e);
                			msg.fail(100, errMsgString);		
                        }
                    } else {
        				Throwable errThrowable = userResult.cause();
        				String errMsgString = errThrowable.getMessage();
        				this.componentImpl.getLogger().error("管理员用户创建失败." + errMsgString, errThrowable);
        				msg.fail(100, errMsgString);		
                    }
                });
            });


        } catch (Exception e) {
			String errMsgString = e.getMessage();
			this.componentImpl.getLogger().error("管理员用户创建失败." + errMsgString, e);
			msg.fail(100, errMsgString);		
        }

    }
}

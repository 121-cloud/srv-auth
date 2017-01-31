/*package otocloud.auth.handler;

import io.vertx.core.json.JsonObject;

import org.apache.oltu.oauth2.as.request.OAuthAuthzRequest;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;

import otocloud.framework.core.OtoCloudBusMessage;
import otocloud.framework.core.OtoCloudComponentImpl;
import otocloud.framework.core.OtoCloudEventHandlerImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

*//**
 * zhangyef@yonyou.com on 2015-11-11.
 *//*
public class AuthorizationHandler extends OtoCloudEventHandlerImpl<JsonObject> {

    public AuthorizationHandler(OtoCloudComponentImpl componentImpl) {
        super(componentImpl);
    }

    *//**
     * {
     * service: 服务实例名
     * component: 组件名
     * handler: 操作名
     * 
     *//*    
    @Override
    public void handle(OtoCloudBusMessage<JsonObject> message) {
        //构造请求,添加HTTP头部和查询参数.
        HttpServletRequest request = OAuthRequest.create(message);

        try {
            OAuthAuthzRequest authzRequest = new OAuthAuthzRequest(request);
            // 根据response_type创建response
            String responseType = authzRequest.getParam(OAuth.OAUTH_RESPONSE_TYPE);

            OAuthASResponse.OAuthAuthorizationResponseBuilder builder = OAuthASResponse
                    .authorizationResponse(request,
                            HttpServletResponse.SC_FOUND);


        } catch (OAuthProblemException e) {

        } catch (OAuthSystemException e) {

        }

    }

    @Override
    public String getEventAddress() {
        return null;
    }
}
*/
/*package otocloud.auth.entity;

import io.vertx.core.json.JsonObject;
import org.junit.Assert;
import org.junit.Test;

*//**
 * ResponseMessage Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>九月 29, 2015</pre>
 *//*
public class ResponseMessageTest {


    @Test
    public void testCodeMessage() throws Exception {
        ReplyCodeMessage replyCodeMessage = new ReplyCodeMessage();
        replyCodeMessage.setErrCode(0);
        replyCodeMessage.setErrMsg("ok");

        System.out.println(replyCodeMessage.toJson());

        JsonObject msg = new JsonObject(replyCodeMessage.toJson());
        Assert.assertEquals(msg.getString("errMsg"), "ok");
    }


} 
*/
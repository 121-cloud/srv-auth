package otocloud.auth.common.util;

import io.vertx.core.json.JsonObject;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import otocloud.auth.entity.User;

import java.util.UUID;

/** 
* Mapper Tester. 
* 
* @author zhangye 
* @since <pre>十月 21, 2015</pre> 
* @version 1.0 
*/ 
public class MapperTest { 

    @Before
    public void before() throws Exception { 
    } 

    @After
    public void after() throws Exception { 
    } 

   /** 
    * 
    * Method: mapToEntity(JsonObject entityInfo, Class<T> clazz) 
    * 
    */ 
    @Test
    public void testMapToEntity() throws Exception {
        JsonObject userInfo = new JsonObject();

        String openId = UUID.randomUUID().toString();

        userInfo.put("openId", openId);
        userInfo.put("name", "新的用户名");
        userInfo.put("password", "新的用户密码");
        userInfo.put("org_acct_id", 12);
        userInfo.put("cell_no", "1234567651");
        userInfo.put("email", "yezhang@yonyou.com");

        User user = Mapper.mapToEntity(userInfo, User.class);
        Assert.assertEquals(openId, user.getOpenID());
        Assert.assertEquals("新的用户名", user.getUserName());
        Assert.assertEquals("新的用户密码", user.getPassword());
        Assert.assertEquals(12, user.getOrgAcctId());
        Assert.assertEquals("1234567651", user.getCellNo());
        Assert.assertEquals("yezhang@yonyou.com", user.getEmail());
    }
    
    
} 

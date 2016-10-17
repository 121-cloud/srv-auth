package otocloud.auth.common.mail; 

import org.junit.Test; 
import org.junit.Before; 
import org.junit.After; 

/** 
* MailSender Tester. 
* 
* @author zhangye 
* @since <pre>一月 21, 2016</pre> 
* @version 1.0 
*/ 
public class MailSenderTest {
    MailSender sender;

    @Before
    public void before() throws Exception {
        sender = new MailSender();
    } 

    @After
    public void after() throws Exception { 
    } 

   /** 
    * 
    * Method: send(String title) 
    * 
    */ 
    @Test
    public void testSend() throws Exception { 
        sender.send("注册新用户.");
    } 
    
    
} 

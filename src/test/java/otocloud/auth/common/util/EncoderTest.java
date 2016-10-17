package otocloud.auth.common.util;

import org.apache.commons.codec.digest.DigestUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/** 
* Encoder Tester. 
* 
* @author zhangye 
* @since <pre>十一月 20, 2015</pre> 
* @version 1.0 
*/ 
public class EncoderTest { 

    @Before
    public void before() throws Exception { 
    } 

    @After
    public void after() throws Exception { 
    } 

   /** 
    * 
    * Method: md5(String plainText) 
    * 
    */ 
    @Test
    public void testMd5() throws Exception {
        String request = "get /api/myapp/fun/1";
        String md5 = DigestUtils.md5Hex(request);

        System.out.println(md5);
    } 
    
    
   /** 
    * 
    * Method: encode(String algorithm, String str) 
    * 
    */ 
    @Test
    public void testEncode() throws Exception { 
        //TODO: Test goes here... 
        /* 
        try { 
           Method method = Encoder.getClass().getMethod("encode", String.class, String.class); 
           method.setAccessible(true); 
           method.invoke(<Object>, <Parameters>); 
        } catch(NoSuchMethodException e) { 
        } catch(IllegalAccessException e) { 
        } catch(InvocationTargetException e) { 
        } 
        */ 
    } 
    
   /** 
    * 
    * Method: getFormattedText(byte[] bytes) 
    * 
    */ 
    @Test
    public void testGetFormattedText() throws Exception { 
        //TODO: Test goes here... 
        /* 
        try { 
           Method method = Encoder.getClass().getMethod("getFormattedText", byte[].class); 
           method.setAccessible(true); 
           method.invoke(<Object>, <Parameters>); 
        } catch(NoSuchMethodException e) { 
        } catch(IllegalAccessException e) { 
        } catch(InvocationTargetException e) { 
        } 
        */ 
    } 
    
} 

/*package otocloud.auth.entity;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.Column;
import java.lang.reflect.Field;

*//**
 * User Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>九月 28, 2015</pre>
 *//*
public class UserTest {
    User user;

    @Before
    public void before() throws Exception {
        user = new User();
    }

    @After
    public void after() throws Exception {
        user = null;
    }

    @Test
    public void testToJson() throws Exception {
        user.setID(123);
        user.setOpenID("openID1");
        user.setUserName("somename");
        user.setPassword("*&*&*");

        System.out.println(user.toJson());

        Assert.assertEquals(user.toJson(), user.toJsonObject().toString());
        Assert.assertTrue(user.toJsonObject().getInteger("id").equals(123));
    }

    @Test
    public void testFromJson() throws Exception {
        String houseStr =
                "{\"userName\":\"somename\",\"password\":\"*&*&*\"," +
                        "\"cellNo\":null,\"email\":null,\"id\":2," +
                        "\"openID\":\"openID1\"}";
        User user = EntityFactory.fromJson(houseStr, User.class);

        Assert.assertEquals(2, user.getID());
    }

    @Test
    public void testColumnAnnotation() throws Exception {
        User user = new User();
        user.setID(123);
        Field[] fields = User.class.getDeclaredFields();
        for (Field field : fields) {
            Column column = field.getAnnotation(Column.class);
            System.out.println(column != null ? column.name() : "null");
            field.setAccessible(true);
            System.out.println(field.get(user));
        }
    }


}
*/
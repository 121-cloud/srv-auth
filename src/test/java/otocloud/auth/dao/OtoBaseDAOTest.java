package otocloud.auth.dao;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.sql.UpdateResult;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import otocloud.auth.base.GuiceContextTestBase;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * OtoBaseDAO Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>十月 20, 2015</pre>
 */
@RunWith(VertxUnitRunner.class)
public class OtoBaseDAOTest extends GuiceContextTestBase{

    private OtoBaseDAO baseDAO;

    @Before
    public void before(TestContext context) throws Exception {
        super.setUp();

        baseDAO = injector.getInstance(OtoBaseDAO.class);
    }

    @After
    public void after(TestContext context) throws Exception {
        vertx.close(context.asyncAssertSuccess());
    }

    /**
     * Method: updateBy(String tableName, JsonObject values, JsonObject where, Future<UpdateResult>
     * doneFuture)
     */
    @Test
    public void testUpdateBy(TestContext context) throws Exception {
        final Async async = context.async();

        Future<UpdateResult> doneFuture = Future.future();
        baseDAO.updateBy("auth_user", new JsonObject()
                        .put("email", "updateByBaseDAO@yonyou.com")
                        .put("last_pwd_changed_datetime", "2015-10-26 11:03"),
                new JsonObject().put("id", "79"), doneFuture);
        doneFuture.setHandler(ret -> {
            if (ret.succeeded()) {
                System.out.println(ret.result());
                async.complete();
            } else {
                context.fail(ret.cause());
            }
        });
    }

    /**
     * Method: makeWhereConditionClause(JsonObject where)
     */
    @Test
    public void testMakeWhereConditionClause() throws Exception {
        String whereCondition1 = "para1=?";
        String whereCondition2 = "para1=? AND para2=?";
        String whereCondition3 = ""; //nothing

        JsonObject condition1 = new JsonObject().put("para1", "1");
        JsonObject condition2_1 = new JsonObject().put("para1", "1").put("para2", "2");
        JsonObject condition2_2 = new JsonObject().put("para1", "1").put("para2", 2);
        JsonObject condition3 = new JsonObject(); //nothing

        Assert.assertEquals(whereCondition1, baseDAO.makeWhereConditionClause(condition1));
        Assert.assertEquals(whereCondition2, baseDAO.makeWhereConditionClause(condition2_1));
        Assert.assertEquals(whereCondition2, baseDAO.makeWhereConditionClause(condition2_2));
        Assert.assertEquals(whereCondition3, baseDAO.makeWhereConditionClause(condition3));
    }

    /**
     * Method: makeUpdateSetClause(JsonObject values)
     */
    @Test
    public void testMakeUpdateSetClause() throws Exception {
        try {
            Method method = OtoBaseDAO.class.getMethod("makeUpdateSetClause", JsonObject.class);
            method.setAccessible(true);

            String updateSet1 = "data='2015-10-19'";
            String updateSet2 = "data='2015-10-19'";
            JsonObject update1 = new JsonObject().put("date", "2015-10-19");
            JsonObject update2 = new JsonObject().put("date", "2015-10-19").put("time", "10:23:00");
            method.invoke(baseDAO, update1);

        } catch (NoSuchMethodException e) {
        } catch (IllegalAccessException e) {
        } catch (InvocationTargetException e) {
        }

    }


}

package jmock;

import com.google.inject.Binder;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import mockit.*;
import mockit.integration.junit4.JMockit;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * zhangyef@yonyou.com on 2015-11-25.
 */
//@RunWith(JMockit.class)
public class DaoTest {

    private Injector injector;

    @Before
    public void setUp(){
        injector = Guice.createInjector(new Module() {
            @Override
            public void configure(Binder binder) {
                binder.bind(MyService.class).to(MyServiceImpl.class);
            }
        });
    }

    @Test
    public void it_should_get_sum() {
        new MockUp<MyDAO>(){
            @Mock
            public int getSum(){
                return 2;
            }

        };

        MyService service = injector.getInstance(MyService.class);

        Assert.assertEquals(2, service.sum());
    }
}

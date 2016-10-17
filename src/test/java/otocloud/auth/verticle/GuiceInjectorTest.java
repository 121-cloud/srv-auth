package otocloud.auth.verticle;

import com.google.inject.Guice;
import com.google.inject.Injector;
import io.vertx.core.Vertx;
import org.junit.Ignore;
import org.junit.Test;
import otocloud.auth.guice.BindingModule;

/**
 * 测试Guice加载时间.
 * zhangyef@yonyou.com on 2015-11-26.
 */
@Ignore("测试Guice加载时间 - 开发态测试")
public class GuiceInjectorTest {
    private Injector injector;

    //声明为类的字段,用于后续延迟加载数据源(框架提供的vertx数据源)
    private BindingModule bindingModule;

    Vertx vertx = Vertx.vertx();

    @Test
    public void it_should_load_quickly(){
        initGuiceInjector(vertx);

        UserComponent com = injector.getInstance(UserComponent.class);

        com.getName();
    }

    private void initGuiceInjector(Vertx vertx) {
        bindingModule = new BindingModule();
        bindingModule.setVertx(vertx);

        injector = Guice.createInjector(bindingModule);

//        injector = LifecycleInjector.builder()
//                .usingBasePackages("otocloud.auth")
//                .withModules(bindingModule).build().createInjector();
    }
}

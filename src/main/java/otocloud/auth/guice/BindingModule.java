package otocloud.auth.guice;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import io.vertx.core.Vertx;
import otocloud.auth.common.JdbcDataSourceHolder;
import otocloud.auth.verticle.AuthService;
import otocloud.auth.verticle.UserComponent;
import otocloud.framework.core.OtoCloudComponentImpl;
import ru.vyarus.guice.validator.ImplicitValidationModule;

/**
 * zhangyef@yonyou.com on 2015-11-17.
 */
public class BindingModule extends AbstractModule {

    private AuthService authService;

    private Vertx vertx;
    private JdbcDataSourceHolder dataSourceHolder;

    public BindingModule() {
        dataSourceHolder = new JdbcDataSourceHolder();
    }

    @Override
    protected void configure() {
        bind(Vertx.class).toInstance(vertx);
        if (authService != null) {
            bind(AuthService.class).toInstance(authService);
        }
        bind(JdbcDataSourceHolder.class).toInstance(dataSourceHolder);

        bind(OtoCloudComponentImpl.class)
                .annotatedWith(Names.named("UserComponent"))
                .to(UserComponent.class);

        //添加Mybatis
        install(new DBModule());

        //验证
        install(new ImplicitValidationModule());
    }

    public void setVertx(Vertx vertx) {
        this.vertx = vertx;
    }

    public void setAuthService(AuthService authService) {
        this.authService = authService;
    }

    public JdbcDataSourceHolder getDataSourceHolder() {
        return dataSourceHolder;
    }
}

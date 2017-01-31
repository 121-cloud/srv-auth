/*package otocloud.auth.base;

import com.google.inject.Guice;
import com.google.inject.Injector;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import org.junit.Before;
import otocloud.auth.common.util.GlobalDataPool;
import otocloud.auth.guice.BindingModule;
import otocloud.persistence.dao.JdbcDataSource;

*//**
 * zhangyef@yonyou.com on 2015-11-23.
 *//*
public class GuiceContextTestBase {
    protected Injector injector;
    protected Vertx vertx;
    protected JdbcDataSource dataSource;

    private BindingModule bindingModule;
    private String DB_URL = "jdbc:mysql://10.10.23.112:3306/121db_new?useUnicode=true&characterEncoding=UTF-8";
//    private String DB_URL = "jdbc:mysql://localhost:3306/121db_new?useUnicode=true&characterEncoding=UTF-8";

    @Before
    public void setUp() {
        vertx = Vertx.vertx();

        dataSource = makeSysDBSource();

//        String mongoIP = "localhost";
        String mongoIP = "10.10.23.112";

        GlobalDataPool.INSTANCE.put("mongo_client_at_auth_service",
                new JsonObject().put("host", mongoIP).put("port", 27017));

        initGuiceModule(vertx);
    }

    private void initGuiceModule(Vertx vertx) {
        bindingModule = new BindingModule();

        bindingModule.setVertx(vertx);

        //注入数据源
        bindingModule.getDataSourceHolder().setJdbcDataSource(dataSource);

        injector = Guice.createInjector(bindingModule);
//        injector = LifecycleInjector.builder()
//                .withBootstrapModule(new ValidationModule())
//                .withModules(bindingModule).build().createInjector();
    }

    private JdbcDataSource makeSysDBSource() {
        JsonObject sysDSConfig = new JsonObject();
        sysDSConfig.put("sharedpool", "jdbc-auth");
        JsonObject connConfig = new JsonObject();

        connConfig.put("url", DB_URL);
        connConfig.put("driver_class", "");
        connConfig.put("max_pool_size", 30);
        connConfig.put("user", "test");
        connConfig.put("password", "test");
        sysDSConfig.put("config", connConfig);

        JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.init(vertx, sysDSConfig);
        return dataSource;
    }
}
*/
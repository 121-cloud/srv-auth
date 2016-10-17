package otocloud.auth.guice;

import com.google.inject.name.Names;
import org.mybatis.guice.XMLMyBatisModule;
import otocloud.auth.service.UserService;
import otocloud.auth.service.UserServiceImpl;

import java.util.Properties;

/**
 * zhangyef@yonyou.com on 2015-11-23.
 */
public class DBModule extends XMLMyBatisModule {

    private Properties createTestProperties(){
        Properties myBatisProperties = new Properties();
//        myBatisProperties.setProperty("JDBC.url", "jdbc:mysql://10.10.23.112:3306/121db_new");
//        myBatisProperties.setProperty("JDBC.username", "test");
//        myBatisProperties.setProperty("JDBC.password", "test");
//        myBatisProperties.setProperty("JDBC.autoCommit", "false");

        return myBatisProperties;
    }

    @Override
    protected void initialize() {
        setClassPathResource("mybatis-config.xml");

//        Names.bindProperties(this.binder(), createTestProperties());

        bind(UserService.class).to(UserServiceImpl.class);

    }

}

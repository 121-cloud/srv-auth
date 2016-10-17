package otocloud.auth.mybatis;

import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.datasource.unpooled.UnpooledDataSourceFactory;

/**
 * zhangyef@yonyou.com on 2015-12-04.
 */
public class HikariDataSourceFactory extends UnpooledDataSourceFactory {
    public HikariDataSourceFactory(){
        this.dataSource = new HikariDataSource();
    }
}

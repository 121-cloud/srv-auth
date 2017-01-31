package otocloud.auth.dao;

import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

import otocloud.persistence.dao.JdbcDataSource;
import otocloud.persistence.dao.OperatorDAO;



/**
 * Created by zhangye on 2015-10-15.
 * Modified by lijing on 2017-1-29
 */
public class EntityDAO extends OperatorDAO {
	
    protected Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    public EntityDAO(JdbcDataSource dataSource) {
        super(dataSource);
    }

/*    *//**
     * 根据实体对象，生成UPDATE语句中的SET子句字符串。
     * <p>
     * 如果merged为true，则实体对象的null属性<strong>会</strong>放到SET子句中。
     * 如果merged为false，则实体对象的null属性<strong>不会</strong>放到SET子句中。
     *
     * @param entity 被处理的实体对象.
     * @param merged 标记对象中的null字段是否放置在SET子句中. true, 与数据库合并空字段; false, 覆盖数据库字段.
     * @param <T>    对象类型.
     * @return SET 子句(不包含SET关键字).
     *//*
    public final <T> String mapToSetClause(T entity, boolean merged) {
        Class clazz = entity.getClass();

        Field[] fields = clazz.getDeclaredFields();

        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            Column column = field.getAnnotation(Column.class);

            //没有被注解，则不处理.
            if (column == null) {
                continue;
            }
            String setKey = column.name();

            try {
                if (StringUtils.isBlank(setKey)) {
                    setKey = field.getName(); //默认使用字段名
                }
                field.setAccessible(true);
                Object newValue = field.get(entity);
                field.setAccessible(false);

                //值为null，则跳过。
                if (merged && newValue == null) {
                    continue;
                }

                if (i != 0) {
                    //不是第一个，添加分隔符
                    builder.append(", ");
                }

                builder.append(setKey).append("=");
                builder.append(makeSetValue(newValue));

            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return builder.toString();
    }*/

    /**
     * TODO 处理不同数据类型.
     *
     * @param value
     * @return
     */
/*    private String makeSetValue(Object value) {
        StringBuilder builder = new StringBuilder();
        if (value == null) {
            return "''";
        }

        if (value instanceof String) {
            builder.append("'").append(value).append("'");
        } else {
            builder.append(value);
        }

        return builder.toString();
    }*/

}

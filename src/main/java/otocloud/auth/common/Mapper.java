package otocloud.auth.common;
/*package otocloud.auth.common.util;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.sql.ResultSet;
import org.apache.commons.lang3.StringUtils;
import otocloud.auth.entity.JsonKey;

import javax.persistence.Column;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

*//**
 * 对象映射工具类。
 * Created by zhangye on 2015-10-21.
 *//*
public class Mapper {
    *//**
     * 将JsonObject映射为业务实体对象.
     * <p>
     * 当业务实体中配置了{@linkplain JsonKey}注解，并且JsonObject中含有该注解所在的对象属性时，发生映射.
     * 其他情况，业务实体对象的属性被忽略，或者JsonObject中的对象被忽略.
     *
     * @param entityInfo JSON对象.
     * @param clazz      业务对象的class属性.
     * @param <T>        业务对象的类型泛型.
     * @return 映射后的业务实体对象.
     *//*
    public final static <T> T mapToEntity(JsonObject entityInfo, Class<T> clazz) {
        T t = null;
        try {
            t = clazz.newInstance();

            Field[] fields = clazz.getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                Field field = fields[i];

                JsonKey jsonKey = field.getAnnotation(JsonKey.class);

                //没有被注解的属性被自动忽略
                if (jsonKey == null) {
                    continue;
                }

                String key = jsonKey.name();
                if (StringUtils.isBlank(key)) {
                    key = field.getName();
                }

                //只处理JsonObject中包含的属性。
                if (entityInfo.containsKey(key)) {
                    field.setAccessible(true);
                    field.set(t, entityInfo.getValue(key));
                    field.setAccessible(false);
                }
            }
        } catch (InstantiationException e) {
            t = null;
        } catch (IllegalAccessException e) {
            t = null;
        }

        return t;
    }


    private static <T> T mapDBJsonToEntity(JsonObject entityJson, Class<T> clazz) {
        try {
            T entity = clazz.newInstance();
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                Column column = field.getAnnotation(Column.class);
                String fieldName = field.getName();

                if (column != null && column.name() != null) {
                    fieldName = column.name();
                }
                Object value = entityJson.getValue(fieldName);
                if(value != null) {

                    field.set(entity, value);
                }
            }

            return entity;

        } catch (InstantiationException | IllegalAccessException ignore) {

        }

        return null;
    }

    *//**
     * 将查询结果映射为业务实体对象。
     * <p>
     * 映射规则：根据数据库表的列名与实体对象字段名是否相同进行映射。名称相同则映射，不相同不映射。
     * <ol>
     * <li>如果实体对象的字段被 {@linkplain Column} 注解，则按照注解的名称进行匹配。</li>
     * <li>如果数据表具有某个字段，实体对象不具有该字段，则该字段被自动忽略。</li>
     * <li>如果实体对象具有某个字段，数据表不具有该字段，则实体对象中该字段的值为null。</li>
     * </ol>
     *
     * @param set   查询结果集
     * @param clazz 被转换的实体对象的class属性
     * @param <T>   实体对象泛型
     * @return 映射后的实体对象列表
     *//*
    public static <T> List<T> mapToEntity(ResultSet set, Class<T> clazz) {
        List<T> entities = new LinkedList<>();

        try {
            if (set.getNumRows() < 1) {
                return null;
            }

            List<JsonArray> results = set.getResults();

            Field[] fields = clazz.getDeclaredFields();
            //分析clazz中的字段注解
            HashMap<String, Field> fieldHashMap = new HashMap<>(fields.length);

            for (Field field : fields) {
                Column column = field.getAnnotation(Column.class);
                if (column == null || column.name() == null) {
                    fieldHashMap.put(field.getName(), field);
                } else {
                    fieldHashMap.put(column.name(), field);
                }
            }

            List<String> columnNames = set.getColumnNames();
            String[] columnNameArray = columnNames.toArray(new String[columnNames.size()]);

            for (JsonArray row : results) {
                T entity = clazz.newInstance();
                int columnNum = row.size();
                for (int i = 0; i < columnNum; i++) {
                    Object value = row.getValue(i);
                    //反射中不允许设置空值，如果结果为空，则跳过设置。
                    if (value == null) {
                        continue;
                    }
                    if (fieldHashMap.containsKey(columnNameArray[i])) {
                        Field settedField = fieldHashMap.get(columnNameArray[i]);
                        boolean isAccessable = settedField.isAccessible();
                        if (isAccessable) {
                            settedField.set(entity, value);
                        } else {
                            settedField.setAccessible(true);
                            settedField.set(entity, value);
                            settedField.setAccessible(false);
                        }
                    }
                }

                entities.add(entity);
            }

        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return entities;
    }

    *//**
     * 转换业务实体列表.
     * 需要对导出的业务实体字段标记"@expose"
     * @param list
     * @return
     *//*
    public static JsonArray toJsonArray(List list) {
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .excludeFieldsWithoutExposeAnnotation().create();
        String jsonArrayStr = gson.toJson(list);
        return new JsonArray(jsonArrayStr);
    }

    public static JsonObject toJson(Object o){
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .excludeFieldsWithoutExposeAnnotation().create();
        try {
            JsonObject jsonObject = new JsonObject(gson.toJson(o));
            return jsonObject;
        }catch (Exception e){

            return new JsonObject();
        }
    }
}
*/
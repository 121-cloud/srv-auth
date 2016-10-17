package otocloud.auth.entity;

import otocloud.auth.common.util.Mapper;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 用于标记实体属性与{@linkplain io.vertx.core.json.JsonObject JsonObject}中Key的映射关系。
 * <p>
 * 如果某个属性被{@link JsonKey}标注，并且JsonObject中含有该属性名对应的Key，则在
 * {@linkplain Mapper#mapToEntity(io.vertx.core.json.JsonObject, java.lang.Class) Mapper.mapToEntity(JsonObject,
 * Class)}过程中，
 * 会将JsonObject中的Value设置到实体的属性中。
 * <p>
 * Created by zhangye on 2015-10-21.
 */
@Target({FIELD})
@Retention(RUNTIME)
public @interface JsonKey {
    /**
     * (可选的) JsonObject中Key的名字. 默认为属性名.
     */
    String name() default "";
}

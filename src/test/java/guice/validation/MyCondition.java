package guice.validation;

import otocloud.auth.common.validator.RequiredValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * zhangyef@yonyou.com on 2015-12-21.
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MyConditionValidator.class)
public @interface MyCondition {

    String message() default "总线消息体格式约束 [必须包含的字段]";

    /**
     * 设置必须存在的JsonObject字段.
     *
     * @return
     */
    String[] value() default {};

    /**
     * 设置必须存在的JsonObject可选字段集合.
     * JsonObject中有且仅有一个集合中的字段.
     *
     * @return
     */
    String[] choices() default {};

    Class<?>[] groups() default {};

    Class<Payload>[] payload() default {};
}

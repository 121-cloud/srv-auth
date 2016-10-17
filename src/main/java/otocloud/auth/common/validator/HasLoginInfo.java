package otocloud.auth.common.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by zhangye on 2015-10-10.
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = HasLoginInfoValidator.class)
public @interface HasLoginInfo {
    String message() default "添加用户约束.";

    Class<?>[] groups() default {};

    Class<Payload>[] payload() default {};
}

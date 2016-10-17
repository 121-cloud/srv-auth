package guice.validation;

import io.vertx.core.json.JsonObject;
import otocloud.auth.common.validator.Required;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * zhangyef@yonyou.com on 2015-12-21.
 */
public class MyConditionValidator implements ConstraintValidator<MyCondition, Integer> {
    MyCondition condition;
    @Override
    public void initialize(MyCondition constraintAnnotation) {
        condition = constraintAnnotation;
    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        return value != 0;
    }
}

package otocloud.auth.common.validator;

import io.vertx.core.json.JsonObject;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Created by zhangye on 2015-10-10.
 */
public class HasLoginInfoValidator implements ConstraintValidator<HasLoginInfo, JsonObject> {

    @Override
    public void initialize(HasLoginInfo constraintAnnotation) {
    }

    @Override
    public boolean isValid(JsonObject value, ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();

        return checkValue(context, value);
    }

    private boolean checkValue(ConstraintValidatorContext context, JsonObject value) {
        boolean isValid = true;

        String userName = value.getString("userName");
        String password = value.getString("password");


        if (userName == null || userName.length() == 0) {
            isValid = false;
            context.buildConstraintViolationWithTemplate("{user.userName.required}").addConstraintViolation();
        }

        if (password == null || password.length() == 0) {
            isValid = false;
            context.buildConstraintViolationWithTemplate("{user.password.required}").addConstraintViolation();
        }

        return isValid;
    }
}

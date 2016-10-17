package otocloud.auth.common.validator;


import io.vertx.core.json.JsonObject;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Created by zhangye on 2015-10-16.
 */
public class RequiredValidator implements ConstraintValidator<Required, JsonObject> {
    private String[] properties;
    private String[] choices;

    @Override
    public void initialize(Required required) {
        properties = required.value();
        choices = required.choices();
    }

    @Override
    public boolean isValid(JsonObject value, ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();
        if (properties == null) {
            return notValid();
        }

        boolean isValid = checkRequired(value, context);

        if (isValid) {
            isValid = checkChoice(value, context);
        }


        return isValid;
    }

    /**
     * 检查必须存在一个的可选字段集合.
     * 在所有字段中,JsonObject必须包含其中一个字段.
     *
     * @param value Json对象.
     * @param context 校验上下文.
     * @return true,校验通过,消息满足业务要求; false,校验失败,消息不符合业务要求.
     */
    private boolean checkChoice(JsonObject value, ConstraintValidatorContext context) {
        boolean isValid = true;
        if (isValid && choices.length > 0) {
            isValid = false;
            for (int i = 0; i < choices.length; i++) {
                String choice = choices[i];
                if (value.containsKey(choice)) {
                    isValid = true;
                    break;
                }
            }
        }
        if (!isValid) {
            context.buildConstraintViolationWithTemplate("{eventbus.message.prop.oneOf}:" + choices)
                    .addConstraintViolation();
        }
        return isValid;
    }

    /**
     * 检查全部必选字段.
     * 设置的所有字段都必须包含在JsonObject中.
     *
     * @param value
     * @param context
     * @return
     */
    private boolean checkRequired(JsonObject value, ConstraintValidatorContext context) {
        boolean isValid = true;
        for (int i = 0; i < properties.length; i++) {
            String prop = properties[i];
            if (!value.containsKey(prop)) {
                context.buildConstraintViolationWithTemplate(prop + "{eventbus.message.prop.required}")
                        .addConstraintViolation();
                isValid = false;
            }
        }
        return isValid;
    }

    private boolean notValid() {
        return false;
    }


}

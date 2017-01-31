package otocloud.auth.common;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by zhangye on 2015-10-16.
 */
public class ViolationMessageBuilder {
    private static final String NEW_LINE = System.getProperty("line.separator");

    public static String build(ConstraintViolationException e) {
        StringBuilder msgBuilder = new StringBuilder();
        msgBuilder.append("参数校验错误:" + NEW_LINE);

        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        Iterator<ConstraintViolation<?>> itr = violations.iterator();
        while (itr.hasNext()) {
            ConstraintViolation<?> violation = itr.next();
            msgBuilder.append(violation.getMessage() + NEW_LINE);
        }

        return msgBuilder.toString();
    }
}


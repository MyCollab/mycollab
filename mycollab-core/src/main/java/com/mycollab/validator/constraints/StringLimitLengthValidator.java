package com.mycollab.validator.constraints;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@SuppressWarnings("ucd")
public class StringLimitLengthValidator implements ConstraintValidator<StringLimitLength, String> {

    @Override
    public void initialize(StringLimitLength constraintAnnotation) {

    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.trim().equals("") || value.length() > 3) {
            return false;
        }
        return value.matches("\\w*");
    }
}

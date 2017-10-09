package com.mycollab.validator.constraints;

import org.apache.commons.beanutils.PropertyUtils;
import org.joda.time.LocalDate;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Date;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class DateComparisonValidator implements ConstraintValidator<DateComparison, Object> {
    private String firstDateField;
    private String lastDateField;

    @Override
    public void initialize(DateComparison constraintAnnotation) {
        this.firstDateField = constraintAnnotation.firstDateField();
        this.lastDateField = constraintAnnotation.lastDateField();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        try {
            Date firstValue = (Date) PropertyUtils.getProperty(value, firstDateField);
            Date lastValue = (Date) PropertyUtils.getProperty(value, lastDateField);
            if (firstValue != null && lastValue != null) {
                LocalDate firstDate = new LocalDate(PropertyUtils.getProperty(value, firstDateField));
                LocalDate lastDate = new LocalDate(PropertyUtils.getProperty(value, lastDateField));
                return firstDate.compareTo(lastDate) <= 0;
            }
            return true;
        } catch (Exception ex) {
            return true;
        }
    }
}

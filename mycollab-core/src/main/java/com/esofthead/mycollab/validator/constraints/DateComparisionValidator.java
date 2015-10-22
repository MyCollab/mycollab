/**
 * This file is part of mycollab-core.
 *
 * mycollab-core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-core.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.validator.constraints;

import org.apache.commons.beanutils.PropertyUtils;
import org.joda.time.LocalDate;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Date;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class DateComparisionValidator implements ConstraintValidator<DateComparision, Object> {
    private String firstDateField;
    private String lastDateField;

    @Override
    public void initialize(DateComparision constraintAnnotation) {
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

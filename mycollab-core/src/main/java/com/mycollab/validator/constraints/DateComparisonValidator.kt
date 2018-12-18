/**
 * Copyright © MyCollab
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http:></http:>//www.gnu.org/licenses/>.
 */
package com.mycollab.validator.constraints

import org.apache.commons.beanutils.PropertyUtils
import java.time.LocalDate
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
class DateComparisonValidator : ConstraintValidator<DateComparison, Any> {
    private var firstDateField: String? = null
    private var lastDateField: String? = null

    override fun initialize(constraintAnnotation: DateComparison) {
        this.firstDateField = constraintAnnotation.firstDateField
        this.lastDateField = constraintAnnotation.lastDateField
    }

    override fun isValid(value: Any, context: ConstraintValidatorContext): Boolean {
        return try {
            val firstValue = PropertyUtils.getProperty(value, firstDateField) as? LocalDate
            val lastValue = PropertyUtils.getProperty(value, lastDateField) as? LocalDate
            if (firstValue != null && lastValue != null) {
                return firstValue.isBefore(lastValue)
            }
            true
        } catch (ex: Exception) {
            true
        }
    }
}

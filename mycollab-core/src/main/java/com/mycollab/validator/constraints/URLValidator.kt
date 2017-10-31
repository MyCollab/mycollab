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

import com.mycollab.core.utils.StringUtils

import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
class URLValidator : ConstraintValidator<URL, String> {

    override fun initialize(constraintAnnotation: URL) {}

    override fun isValid(value: String, context: ConstraintValidatorContext): Boolean =
            if (!StringUtils.isBlank(value)) {
                value.matches("[-a-zA-Z0-9@:%_\\+.~#?&//=]{2,256}\\.[a-z]{2,4}\\b(\\/[-a-zA-Z0-9@:%_\\+.~#?&//=]*)?".toRegex())
            } else true
}

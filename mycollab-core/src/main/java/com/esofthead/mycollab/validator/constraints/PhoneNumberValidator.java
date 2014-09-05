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

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.esofthead.mycollab.core.utils.StringUtils;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 *
 */
@SuppressWarnings("ucd")
public class PhoneNumberValidator implements
		ConstraintValidator<PhoneNumber, String> {

	@Override
	public void initialize(PhoneNumber constraintAnnotation) {
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		return StringUtils.isValidPhoneNumber(value);
	}

}

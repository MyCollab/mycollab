/**
 * This file is part of mycollab-web.
 *
 * mycollab-web is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-web is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-web.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.core.utils;


/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public class PasswordCheckerUtil {

	public static final String[] partialRegexChecks = { ".*[a-z]+.*", // lower
			".*[A-Z]+.*", // upper
			".*[\\d]+.*", // digits
			".*[@#$%]+.*" // symbols
	};

	public static void checkValidPassword(String password) {
		if (password.length() < 6) {
			throw new InvalidPasswordException(
					"Your password is too short - must be at least 6 characters");
		} else {
			checkPasswordStrength(password);
		}
	}

	private static void checkPasswordStrength(String password) {
		int strengthPercentage = 0;

		if (password.matches(partialRegexChecks[0])) {
			strengthPercentage += 25;
		}
		if (password.matches(partialRegexChecks[1])) {
			strengthPercentage += 25;
		}
		if (password.matches(partialRegexChecks[2])) {
			strengthPercentage += 25;
		}
		if (password.matches(partialRegexChecks[3])) {
			strengthPercentage += 25;
		}

		if (strengthPercentage < 50)
			throw new InvalidPasswordException(
					"Password must contain at least one digit letter, one character and one symbol");
	}
}
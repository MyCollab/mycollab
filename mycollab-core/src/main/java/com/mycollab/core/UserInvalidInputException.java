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
package com.mycollab.core;

/**
 * This exception when user do some invalid action such as typing wrong
 * password, invalid input. Note that MyCollab catch this type exception to
 * recognize user mistake
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class UserInvalidInputException extends MyCollabException {
	private static final long serialVersionUID = 1L;

	public UserInvalidInputException(final String message) {
		super(message);
	}

	public UserInvalidInputException(final Throwable e) {
		super(e);
	}

	public UserInvalidInputException(String message, Throwable e) {
		super(message, e);
	}
}

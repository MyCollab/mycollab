/**
 * This file is part of mycollab-services.
 *
 * mycollab-services is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-services is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-services.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.events;

import com.esofthead.mycollab.eventmanager.ApplicationEvent;

/**
 * 
 * @author MyCollab Ltd.
 *
 */
public class SessionEvent {
	public static class UserProfileChangeEvent extends ApplicationEvent {
		private static final long serialVersionUID = 1L;

		private String fieldChange;

		public UserProfileChangeEvent(Object source, String fieldChange,
				Object data) {
			super(source, data);

			this.fieldChange = fieldChange;
		}

		public String getFieldChange() {
			return fieldChange;
		}
	}
}

/**
 * This file is part of mycollab-mobile.
 *
 * mycollab-mobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-mobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-mobile.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.mobile.module.crm.events;

import com.esofthead.mycollab.eventmanager.ApplicationEvent;

/**
 * @author MyCollab Ltd.
 * @since 3.0
 */
public class CrmEvent {

	public static class GotoHome extends ApplicationEvent {
		private static final long serialVersionUID = -6550809291368983694L;

		public GotoHome(Object source, Object data) {
			super(source, data);
		}
	}

	public static class PushView extends ApplicationEvent {
		private static final long serialVersionUID = -7423399367234620364L;

		public PushView(Object source, Object data) {
			super(source, data);
		}
	}

	public static class NavigateBack extends ApplicationEvent {
		private static final long serialVersionUID = 2564051233745790618L;

		public NavigateBack(Object source, Object data) {
			super(source, data);
		}
	}
}
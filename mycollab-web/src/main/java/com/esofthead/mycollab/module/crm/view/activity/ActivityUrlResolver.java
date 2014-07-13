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
package com.esofthead.mycollab.module.crm.view.activity;

import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.crm.events.ActivityEvent;
import com.esofthead.mycollab.module.crm.view.CrmUrlResolver;

/**
 * 
 * @author MyCollab Ltd.
 * @since 2.0
 * 
 */
public class ActivityUrlResolver extends CrmUrlResolver {
	public ActivityUrlResolver() {
		this.addSubResolver("calendar", new ActivityCalendartUrlResolver());
		this.addSubResolver("todo", new ActivityTodoAddUrlResolver());
		this.addSubResolver("task", new ActivityTaskUrlResolver());
		this.addSubResolver("meeting", new MeetingUrlResolver());
		this.addSubResolver("call", new CallUrlResolver());
	}

	public static class ActivityCalendartUrlResolver extends CrmUrlResolver {
		@Override
		protected void handlePage(String... params) {
			EventBusFactory.getInstance().post(
					new ActivityEvent.GotoCalendar(this, null));
		}
	}

	public static class ActivityTodoAddUrlResolver extends CrmUrlResolver {
		@Override
		protected void handlePage(String... params) {
			EventBusFactory.getInstance().post(
					new ActivityEvent.GotoTodoList(this, null));
		}
	}
}

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
package com.esofthead.mycollab.mobile.module.crm.view.activity;

import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.mobile.module.crm.CrmUrlResolver;
import com.esofthead.mycollab.mobile.module.crm.events.ActivityEvent;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.4.0
 * 
 */
public class ActivityUrlResolver extends CrmUrlResolver {
	public ActivityUrlResolver() {
		this.addSubResolver("list", new ActivityListUrlResolver());
		this.addSubResolver("task", new ActivityTaskUrlResolver());
		this.addSubResolver("meeting", new MeetingUrlResolver());
		this.addSubResolver("call", new CallUrlResolver());
	}

	public static class ActivityListUrlResolver extends CrmUrlResolver {
		@Override
		protected void handlePage(String... params) {
			EventBusFactory.getInstance().post(
					new ActivityEvent.GotoList(this, null));
		}
	}
}

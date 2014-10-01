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

import com.esofthead.mycollab.common.UrlEncodeDecoder;
import com.esofthead.mycollab.common.UrlTokenizer;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.mobile.module.crm.CrmUrlResolver;
import com.esofthead.mycollab.mobile.module.crm.events.ActivityEvent;
import com.esofthead.mycollab.module.crm.domain.Task;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.4.0
 * 
 */
public class ActivityTaskUrlResolver extends CrmUrlResolver {
	public ActivityTaskUrlResolver() {
		this.addSubResolver("add", new TaskAddUrlResolver());
		this.addSubResolver("edit", new TaskEditUrlResolver());
		this.addSubResolver("preview", new TaskPreviewUrlResolver());
	}

	public static class TaskAddUrlResolver extends CrmUrlResolver {
		@Override
		protected void handlePage(String... params) {
			EventBusFactory.getInstance().post(
					new ActivityEvent.TaskAdd(this, new Task()));
		}
	}

	public static class TaskEditUrlResolver extends CrmUrlResolver {
		@Override
		protected void handlePage(String... params) {
			int meetingId = new UrlTokenizer(params[0]).getInt();
			EventBusFactory.getInstance().post(
					new ActivityEvent.TaskEdit(this, meetingId));
		}
	}

	public static class TaskPreviewUrlResolver extends CrmUrlResolver {
		@Override
		protected void handlePage(String... params) {
			int accountId = new UrlTokenizer(params[0]).getInt();
			EventBusFactory.getInstance().post(
					new ActivityEvent.TaskRead(this, accountId));
		}
	}
}

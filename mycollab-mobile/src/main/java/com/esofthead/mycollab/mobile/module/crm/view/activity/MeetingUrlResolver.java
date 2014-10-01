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

import com.esofthead.mycollab.common.UrlTokenizer;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.mobile.module.crm.CrmUrlResolver;
import com.esofthead.mycollab.mobile.module.crm.events.ActivityEvent;
import com.esofthead.mycollab.module.crm.domain.MeetingWithBLOBs;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.4.0
 * 
 */
public class MeetingUrlResolver extends CrmUrlResolver {
	public MeetingUrlResolver() {
		this.addSubResolver("add", new MeetingAddUrlResolver());
		this.addSubResolver("edit", new MeetingEditUrlResolver());
		this.addSubResolver("preview", new MeetingPreviewUrlResolver());
	}

	public static class MeetingAddUrlResolver extends CrmUrlResolver {
		@Override
		protected void handlePage(String... params) {
			EventBusFactory.getInstance().post(
					new ActivityEvent.MeetingAdd(this, new MeetingWithBLOBs()));
		}
	}

	public static class MeetingEditUrlResolver extends CrmUrlResolver {
		@Override
		protected void handlePage(String... params) {
			int meetingId = new UrlTokenizer(params[0]).getInt();
			EventBusFactory.getInstance().post(
					new ActivityEvent.MeetingEdit(this, meetingId));
		}
	}

	public static class MeetingPreviewUrlResolver extends CrmUrlResolver {
		@Override
		protected void handlePage(String... params) {
			int accountId = new UrlTokenizer(params[0]).getInt();
			EventBusFactory.getInstance().post(
					new ActivityEvent.MeetingRead(this, accountId));
		}
	}
}

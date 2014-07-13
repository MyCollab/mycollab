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

import com.esofthead.mycollab.common.UrlEncodeDecoder;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.crm.domain.MeetingWithBLOBs;
import com.esofthead.mycollab.module.crm.events.ActivityEvent;
import com.esofthead.mycollab.module.crm.view.CrmUrlResolver;

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
			String decodeUrl = UrlEncodeDecoder.decode(params[0]);
			int meetingId = Integer.parseInt(decodeUrl);
			EventBusFactory.getInstance().post(
					new ActivityEvent.MeetingEdit(this, meetingId));
		}
	}

	public static class MeetingPreviewUrlResolver extends CrmUrlResolver {
		@Override
		protected void handlePage(String... params) {
			String decodeUrl = UrlEncodeDecoder.decode(params[0]);
			int accountId = Integer.parseInt(decodeUrl);
			EventBusFactory.getInstance().post(
					new ActivityEvent.MeetingRead(this, accountId));
		}
	}
}

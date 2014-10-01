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

import com.esofthead.mycollab.common.UrlTokenizer;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.crm.domain.CallWithBLOBs;
import com.esofthead.mycollab.module.crm.events.ActivityEvent;
import com.esofthead.mycollab.module.crm.view.CrmUrlResolver;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 *
 */
public class CallUrlResolver extends CrmUrlResolver {
	public CallUrlResolver() {
		this.addSubResolver("add", new CallAddUrlResolver());
		this.addSubResolver("edit", new CallEditUrlResolver());
		this.addSubResolver("preview", new CallPreviewUrlResolver());
	}

	public static class CallAddUrlResolver extends CrmUrlResolver {
		@Override
		protected void handlePage(String... params) {
			EventBusFactory.getInstance().post(
					new ActivityEvent.CallAdd(this, new CallWithBLOBs()));
		}
	}

	public static class CallEditUrlResolver extends CrmUrlResolver {
		@Override
		protected void handlePage(String... params) {
			int meetingId = new UrlTokenizer(params[0]).getInt();
			EventBusFactory.getInstance().post(
					new ActivityEvent.CallEdit(this, meetingId));
		}
	}

	public static class CallPreviewUrlResolver extends CrmUrlResolver {
		@Override
		protected void handlePage(String... params) {
			int accountId = new UrlTokenizer(params[0]).getInt();
			EventBusFactory.getInstance().post(
					new ActivityEvent.CallRead(this, accountId));
		}
	}
}

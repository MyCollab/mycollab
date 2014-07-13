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
package com.esofthead.mycollab.module.crm.view.setting;

import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.crm.events.CrmSettingEvent;
import com.esofthead.mycollab.module.crm.view.CrmUrlResolver;

public class CrmSettingUrlResolver extends CrmUrlResolver {

	public CrmSettingUrlResolver() {
		this.addSubResolver("notification",
				new NotificationSettingUrlResolver());
		this.addSubResolver("customlayout", new CustomLayoutUrlResolver());
	}

	public static class NotificationSettingUrlResolver extends CrmUrlResolver {
		@Override
		protected void handlePage(String... params) {
			EventBusFactory.getInstance().post(
					new CrmSettingEvent.GotoNotificationSetting(this, null));
		}
	}

	public static class CustomLayoutUrlResolver extends CrmUrlResolver {
		@Override
		protected void handlePage(String... params) {
			EventBusFactory.getInstance().post(
					new CrmSettingEvent.GotoCustomViewSetting(this, null));
		}
	}
}

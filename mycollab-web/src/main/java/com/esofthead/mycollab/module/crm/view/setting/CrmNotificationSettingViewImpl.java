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

import com.esofthead.mycollab.module.crm.domain.CrmNotificationSetting;
import com.esofthead.mycollab.module.crm.service.CrmNotificationSettingService;
import com.esofthead.mycollab.module.project.view.settings.NotificationSettingViewComponent;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.mvp.AbstractPageView;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 *
 */
@ViewComponent
public class CrmNotificationSettingViewImpl extends AbstractPageView implements
		CrmNotificationSettingView {
	private static final long serialVersionUID = 1L;

	private NotificationSettingViewComponent<CrmNotificationSetting, CrmNotificationSettingService> component;

	@Override
	public void showNotificationSettings(CrmNotificationSetting notification) {
		this.removeAllComponents();
		if (notification == null)
			notification = new CrmNotificationSetting();
		component = new NotificationSettingViewComponent<CrmNotificationSetting, CrmNotificationSettingService>(
				notification,
				ApplicationContextUtil
						.getSpringBean(CrmNotificationSettingService.class)) {
			private static final long serialVersionUID = 1L;
		};
		this.addComponent(component);
	}
}

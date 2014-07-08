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
package com.esofthead.mycollab.module.project.view.settings;

import com.esofthead.mycollab.module.project.domain.ProjectNotificationSetting;
import com.esofthead.mycollab.module.project.service.ProjectNotificationSettingService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.mvp.AbstractPageView;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;

/**
 * 
 * @author MyCollab Ltd.
 * @since 2.0
 * 
 */
@ViewComponent
public class ProjectNotificationSettingViewImpl extends AbstractPageView
		implements ProjectNotificationSettingView {
	private static final long serialVersionUID = 1L;

	private NotificationSettingViewComponent<ProjectNotificationSetting, ProjectNotificationSettingService> component;

	public ProjectNotificationSettingViewImpl() {
		this.setMargin(true);
	}

	@Override
	public void showNotificationSettings(ProjectNotificationSetting notification) {
		this.removeAllComponents();

		if (notification == null) {
			notification = new ProjectNotificationSetting();
		}
		component = new NotificationSettingViewComponent<ProjectNotificationSetting, ProjectNotificationSettingService>(
				notification,
				ApplicationContextUtil
						.getSpringBean(ProjectNotificationSettingService.class)) {
		};
		this.addComponent(component);
	}
}

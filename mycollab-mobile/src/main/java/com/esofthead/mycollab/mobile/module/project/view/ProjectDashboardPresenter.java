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
package com.esofthead.mycollab.mobile.module.project.view;

import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.mobile.module.project.view.parameters.ProjectScreenData;
import com.esofthead.mycollab.mobile.ui.AbstractMobilePresenter;
import com.esofthead.mycollab.module.project.ProjectLinkGenerator;
import com.esofthead.mycollab.module.project.ProjectRolePermissionCollections;
import com.esofthead.mycollab.module.project.i18n.ProjectCommonI18nEnum;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.ScreenData;
import com.esofthead.mycollab.vaadin.ui.NotificationUtil;
import com.vaadin.ui.ComponentContainer;

/**
 * @author MyCollab Ltd.
 * @since 4.4.0
 */
public class ProjectDashboardPresenter extends AbstractMobilePresenter<ProjectDashboardView> {
	private static final long serialVersionUID = -2645763046888609751L;

	public ProjectDashboardPresenter() {
		super(ProjectDashboardView.class);
	}

	@Override
	protected void onGo(ComponentContainer container, ScreenData<?> data) {

		if (data instanceof ProjectScreenData.Edit) {
			// TODO: Handle edit project
		} else {
			if (CurrentProjectVariables
					.canRead(ProjectRolePermissionCollections.PROJECT)) {
				super.onGo(container, data);
				view.displayDashboard();
				AppContext.addFragment(ProjectLinkGenerator
						.generateProjectLink(CurrentProjectVariables
								.getProject().getId()), AppContext
						.getMessage(ProjectCommonI18nEnum.VIEW_DASHBOARD));
			} else {
				NotificationUtil.showMessagePermissionAlert();
			}
		}
	}
}

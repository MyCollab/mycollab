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

import com.esofthead.mycollab.core.MyCollabException;
import com.esofthead.mycollab.core.utils.ClassUtils;
import com.esofthead.mycollab.module.project.i18n.ProjectCommonI18nEnum;
import com.esofthead.mycollab.module.project.view.ProjectView;
import com.esofthead.mycollab.module.project.view.parameters.ProjectMemberScreenData;
import com.esofthead.mycollab.module.project.view.parameters.ProjectRoleScreenData;
import com.esofthead.mycollab.module.project.view.parameters.ProjectSettingScreenData;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.PresenterResolver;
import com.esofthead.mycollab.vaadin.mvp.ScreenData;
import com.esofthead.mycollab.vaadin.ui.AbstractPresenter;
import com.vaadin.ui.ComponentContainer;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class UserSettingPresenter extends AbstractPresenter<UserSettingView> {
	private static final long serialVersionUID = 1L;

	public UserSettingPresenter() {
		super(UserSettingView.class);
	}

	@Override
	public void go(ComponentContainer container, ScreenData<?> data) {
		super.go(container, data, false);
	}

	@Override
	protected void onGo(ComponentContainer container, ScreenData<?> data) {
		ProjectView projectViewContainer = (ProjectView) container;
		projectViewContainer.gotoSubView("member");

		AbstractPresenter<?> presenter;
		if (ClassUtils.instanceOf(data, ProjectRoleScreenData.Search.class,
				ProjectRoleScreenData.Add.class,
				ProjectRoleScreenData.Read.class)) {
			view.gotoSubView(AppContext
					.getMessage(ProjectCommonI18nEnum.VIEW_ROLES));
			presenter = PresenterResolver
					.getPresenter(ProjectRolePresenter.class);
		} else if (ClassUtils.instanceOf(data,
				ProjectMemberScreenData.Read.class,
				ProjectMemberScreenData.Search.class,
				ProjectMemberScreenData.Add.class,
				ProjectMemberScreenData.InviteProjectMembers.class)) {
			view.gotoSubView(AppContext
					.getMessage(ProjectCommonI18nEnum.VIEW_USERS));
			presenter = PresenterResolver
					.getPresenter(ProjectUserPresenter.class);
		} else if (ClassUtils.instanceOf(data,
				ProjectSettingScreenData.ViewSettings.class)) {
			view.gotoSubView(AppContext
					.getMessage(ProjectCommonI18nEnum.VIEW_SETTINGS));
			presenter = PresenterResolver
					.getPresenter(ProjectSettingPresenter.class);
		} else {
			throw new MyCollabException("No support screen data: " + data);
		}

		presenter.go(view, data);
	}
}

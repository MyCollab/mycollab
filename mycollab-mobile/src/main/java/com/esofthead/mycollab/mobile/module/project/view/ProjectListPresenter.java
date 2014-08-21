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

import com.esofthead.mycollab.mobile.module.project.ui.ProjectModuleNavigationMenu;
import com.esofthead.mycollab.mobile.ui.ListPresenter;
import com.esofthead.mycollab.module.project.domain.SimpleProject;
import com.esofthead.mycollab.module.project.domain.criteria.ProjectSearchCriteria;
import com.esofthead.mycollab.module.project.i18n.ProjectCommonI18nEnum;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.ScreenData;
import com.esofthead.vaadin.mobilecomponent.MobileNavigationManager;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.UI;

/**
 * @author MyCollab Ltd.
 *
 * @since 4.4.0
 *
 */
public class ProjectListPresenter extends
		ListPresenter<ProjectListView, ProjectSearchCriteria, SimpleProject> {

	private static final long serialVersionUID = 35574182873793474L;

	public ProjectListPresenter() {
		super(ProjectListView.class);
	}

	@Override
	protected void onGo(ComponentContainer container, ScreenData<?> data) {
		super.onGo(container, data);
		ProjectModuleNavigationMenu projectModuleMenu = new ProjectModuleNavigationMenu();
		projectModuleMenu.selectButton(AppContext
				.getMessage(ProjectCommonI18nEnum.M_VIEW_PROJECT_LIST));

		MobileNavigationManager currentNavigationManager = (MobileNavigationManager) UI
				.getCurrent().getContent();
		currentNavigationManager.setNavigationMenu(projectModuleMenu);
	}

}

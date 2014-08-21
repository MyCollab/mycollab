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

import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.StringSearchField;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectMemberStatusConstants;
import com.esofthead.mycollab.module.project.domain.SimpleProject;
import com.esofthead.mycollab.module.project.domain.criteria.ProjectMemberSearchCriteria;
import com.esofthead.mycollab.module.project.domain.criteria.ProjectRoleSearchCriteria;
import com.esofthead.mycollab.module.project.i18n.ProjectCommonI18nEnum;
import com.esofthead.mycollab.module.project.view.parameters.ProjectRoleScreenData;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.AbstractPageView;
import com.esofthead.mycollab.vaadin.mvp.PageView;
import com.esofthead.mycollab.vaadin.mvp.PresenterResolver;
import com.esofthead.mycollab.vaadin.mvp.ScreenData;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.ui.TabsheetDecor;
import com.vaadin.ui.Component;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;
import com.vaadin.ui.TabSheet.Tab;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 */
@ViewComponent
public class UserSettingViewImpl extends AbstractPageView implements
		UserSettingView {
	private static final long serialVersionUID = 1L;

	private ProjectUserPresenter userPresenter;
	private ProjectRolePresenter rolePresenter;
	private ProjectSettingPresenter settingPresenter;

	private final TabsheetDecor myProjectTab;

	public UserSettingViewImpl() {
		this.setWidth("100%");
		this.myProjectTab = new TabsheetDecor();
		this.myProjectTab.setStyleName("tab-style3");

		this.addComponent(myProjectTab);

		this.buildComponents();
	}

	private void buildComponents() {
		this.userPresenter = PresenterResolver
				.getPresenter(ProjectUserPresenter.class);
		this.myProjectTab.addTab(this.userPresenter.getView(),
				AppContext.getMessage(ProjectCommonI18nEnum.VIEW_USERS));

		this.rolePresenter = PresenterResolver
				.getPresenter(ProjectRolePresenter.class);
		this.myProjectTab.addTab(this.rolePresenter.getView(),
				AppContext.getMessage(ProjectCommonI18nEnum.VIEW_ROLES));

		this.settingPresenter = PresenterResolver
				.getPresenter(ProjectSettingPresenter.class);
		this.myProjectTab.addTab(this.settingPresenter.getView(),
				AppContext.getMessage(ProjectCommonI18nEnum.VIEW_SETTINGS));

		this.myProjectTab
				.addSelectedTabChangeListener(new SelectedTabChangeListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void selectedTabChange(SelectedTabChangeEvent event) {
						final Tab tab = ((TabsheetDecor) event.getTabSheet())
								.getSelectedTabInfo();
						final String caption = tab.getCaption();
						final SimpleProject project = CurrentProjectVariables
								.getProject();

						if (AppContext.getMessage(
								ProjectCommonI18nEnum.VIEW_USERS).equals(
								caption)) {
							final ProjectMemberSearchCriteria criteria = new ProjectMemberSearchCriteria();
							criteria.setProjectId(new NumberSearchField(project
									.getId()));
							criteria.setStatus(new StringSearchField(
									ProjectMemberStatusConstants.ACTIVE));
							UserSettingViewImpl.this.userPresenter
									.go(UserSettingViewImpl.this,
											new ScreenData.Search<ProjectMemberSearchCriteria>(
													criteria));
						} else if (AppContext.getMessage(
								ProjectCommonI18nEnum.VIEW_ROLES).equals(
								caption)) {
							final ProjectRoleSearchCriteria criteria = new ProjectRoleSearchCriteria();
							criteria.setProjectId(new NumberSearchField(project
									.getId()));
							UserSettingViewImpl.this.rolePresenter.go(
									UserSettingViewImpl.this,
									new ProjectRoleScreenData.Search(criteria));
						} else if (AppContext.getMessage(
								ProjectCommonI18nEnum.VIEW_SETTINGS).equals(
								caption)) {
							settingPresenter.go(UserSettingViewImpl.this, null);
						}

					}
				});

	}

	@Override
	public Component gotoSubView(final String name) {
		final PageView component = (PageView) this.myProjectTab.selectTab(name)
				.getComponent();
		return component;
	}
}

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

import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.mobile.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.mobile.module.project.events.BugEvent;
import com.esofthead.mycollab.mobile.module.project.events.MessageEvent;
import com.esofthead.mycollab.mobile.module.project.events.MilestoneEvent;
import com.esofthead.mycollab.mobile.module.project.events.ProjectMemberEvent;
import com.esofthead.mycollab.mobile.module.project.events.TaskEvent;
import com.esofthead.mycollab.mobile.ui.AbstractMobileSwipeView;
import com.esofthead.mycollab.module.project.domain.SimpleProject;
import com.esofthead.mycollab.module.project.i18n.ProjectCommonI18nEnum;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

/**
 * @author MyCollab Ltd.
 * @since 4.4.0
 */

@ViewComponent
public class ProjectDashboardViewImpl extends AbstractMobileSwipeView implements
		ProjectDashboardView {
	private static final long serialVersionUID = 2364544271302929730L;

	private final CssLayout mainLayout;

	public ProjectDashboardViewImpl() {
		super();
		this.setCaption(AppContext
				.getMessage(ProjectCommonI18nEnum.VIEW_DASHBOARD));
		mainLayout = new CssLayout();
		mainLayout.setSizeFull();
		mainLayout.setStyleName("project-dashboard");
		this.setContent(mainLayout);
	}

	@Override
	public void displayDashboard() {
		mainLayout.removeAllComponents();
		SimpleProject currentProject = CurrentProjectVariables.getProject();
		VerticalLayout projectInfo = new VerticalLayout();
		projectInfo.setStyleName("project-info-layout");
		projectInfo.setWidth("100%");
		projectInfo.setDefaultComponentAlignment(Alignment.TOP_CENTER);

		Label projectIcon = new Label(
				"<span aria-hidden=\"true\" data-icon=\"&#xe614\"></span>");
		projectIcon.setStyleName("project-icon");
		projectIcon.setContentMode(ContentMode.HTML);
		projectIcon.setWidthUndefined();
		projectInfo.addComponent(projectIcon);

		Label projectName = new Label(currentProject.getName());
		projectName.setWidthUndefined();
		projectName.setStyleName("project-name");
		projectInfo.addComponent(projectName);

		GridLayout projectModulesList = new GridLayout(2, 3);
		projectModulesList.setStyleName("project-modules-layout");
		projectModulesList.setWidth("100%");
		projectModulesList.setSpacing(true);
		projectModulesList.setDefaultComponentAlignment(Alignment.TOP_CENTER);

		projectModulesList.addComponent(new ProjectModuleButton(AppContext
				.getMessage(ProjectCommonI18nEnum.VIEW_MESSAGE), "&#xf04f;"));

		projectModulesList.addComponent(new ProjectModuleButton(AppContext
				.getMessage(ProjectCommonI18nEnum.VIEW_MILESTONE), "&#xf075;"));

		projectModulesList.addComponent(new ProjectModuleButton(AppContext
				.getMessage(ProjectCommonI18nEnum.VIEW_TASK), "&#xe60f;"));

		projectModulesList.addComponent(new ProjectModuleButton(AppContext
				.getMessage(ProjectCommonI18nEnum.VIEW_BUG), "&#xf188;"));

		// projectModulesList.addComponent(new ProjectModuleButton(AppContext
		// .getMessage(ProjectCommonI18nEnum.VIEW_FILE), "&#xf017;"));
		//
		// projectModulesList.addComponent(new ProjectModuleButton(AppContext
		// .getMessage(ProjectCommonI18nEnum.VIEW_RISK), "&#xf02d;"));
		//
		// projectModulesList.addComponent(new ProjectModuleButton(AppContext
		// .getMessage(ProjectCommonI18nEnum.VIEW_PROBLEM), "&#xf0d2;"));
		//
		// projectModulesList.addComponent(new ProjectModuleButton(AppContext
		// .getMessage(ProjectCommonI18nEnum.VIEW_TIME), "&#xe612;"));
		//
		// projectModulesList.addComponent(new ProjectModuleButton(AppContext
		// .getMessage(ProjectCommonI18nEnum.VIEW_STANDAUP), "&#xf0c0;"));

		projectModulesList.addComponent(
				new ProjectModuleButton(AppContext
						.getMessage(ProjectCommonI18nEnum.VIEW_USERS),
						"&#xe601;"), 0, 2, 1, 2);

		mainLayout.addComponent(projectInfo);
		mainLayout.addComponent(projectModulesList);
	}

	private class ProjectModuleButton extends Button {
		private static final long serialVersionUID = -6193679382567699005L;
		private String buttonId;

		public ProjectModuleButton(String id, String iconCode) {
			super("<span aria-hidden=\"true\" data-icon=\"" + iconCode
					+ "\"></span><div class=\"screen-reader-text\">" + id
					+ "</div>");
			this.setWidth("100%");
			this.setStyleName("project-module-btn");
			this.buttonId = id;
			this.setHtmlContentAllowed(true);
			this.addClickListener(new Button.ClickListener() {

				private static final long serialVersionUID = -6012218269990812630L;

				@Override
				public void buttonClick(Button.ClickEvent evt) {
					final String buttonId = ((ProjectModuleButton) evt
							.getButton()).getButtonId();
					if (AppContext.getMessage(
							ProjectCommonI18nEnum.VIEW_MESSAGE)
							.equals(buttonId)) {
						EventBusFactory.getInstance().post(
								new MessageEvent.GotoList(this, null));
					} else if (AppContext.getMessage(
							ProjectCommonI18nEnum.VIEW_MILESTONE).equals(
							buttonId)) {
						EventBusFactory.getInstance().post(
								new MilestoneEvent.GotoList(this, null));

					} else if (AppContext.getMessage(
							ProjectCommonI18nEnum.VIEW_TASK).equals(buttonId)) {
						EventBusFactory.getInstance().post(
								new TaskEvent.GotoList(this, null));
					} else if (AppContext.getMessage(
							ProjectCommonI18nEnum.VIEW_BUG).equals(buttonId)) {
						EventBusFactory.getInstance().post(
								new BugEvent.GotoList(this, null));
					} else if (AppContext.getMessage(
							ProjectCommonI18nEnum.VIEW_USERS).equals(buttonId)) {
						EventBusFactory.getInstance().post(
								new ProjectMemberEvent.GotoList(this, null));
					}
				}
			});
		}

		public String getButtonId() {
			return this.buttonId;
		}
	}
}

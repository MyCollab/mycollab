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
package com.esofthead.mycollab.mobile.module.project.ui;

import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.mobile.module.project.events.BugEvent;
import com.esofthead.mycollab.mobile.module.project.events.MessageEvent;
import com.esofthead.mycollab.mobile.module.project.events.MilestoneEvent;
import com.esofthead.mycollab.mobile.module.project.events.ProjectEvent;
import com.esofthead.mycollab.mobile.module.project.events.ProjectMemberEvent;
import com.esofthead.mycollab.mobile.module.project.events.TaskEvent;
import com.esofthead.mycollab.mobile.module.project.view.parameters.ProjectScreenData;
import com.esofthead.mycollab.mobile.ui.AbstractNavigationMenu;
import com.esofthead.mycollab.mobile.ui.IconConstants;
import com.esofthead.mycollab.module.project.i18n.ProjectCommonI18nEnum;
import com.esofthead.mycollab.vaadin.AppContext;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.0
 * 
 */
public class InsideProjectNavigationMenu extends AbstractNavigationMenu {
	private static final long serialVersionUID = 1L;

	public InsideProjectNavigationMenu() {
		super();

		// setWidth("100%");
		//
		// UserPanel userPanel = new UserPanel();
		// userPanel.setWidth("100%");
		// addComponent(userPanel);

		final MenuButton prjListBtn = new MenuButton(
				AppContext
						.getMessage(ProjectCommonI18nEnum.M_VIEW_PROJECT_LIST),
				IconConstants.PROJECT_DASHBOARD);
		addMenu(prjListBtn);

		final MenuButton activityBtn = new MenuButton(
				AppContext
						.getMessage(ProjectCommonI18nEnum.M_VIEW_PROJECT_ACTIVITIES),
				IconConstants.PROJECT_ACTIVITIES);
		addMenu(activityBtn);

		final MenuButton messageBtn = new MenuButton(
				AppContext.getMessage(ProjectCommonI18nEnum.VIEW_MESSAGE),
				IconConstants.PROJECT_MESSAGE);
		addMenu(messageBtn);

		final MenuButton phaseBtn = new MenuButton(
				AppContext.getMessage(ProjectCommonI18nEnum.VIEW_MILESTONE),
				IconConstants.PROJECT_MILESTONE);
		addMenu(phaseBtn);

		final MenuButton taskBtn = new MenuButton(
				AppContext.getMessage(ProjectCommonI18nEnum.VIEW_TASK),
				IconConstants.PROJECT_TASK);
		addMenu(taskBtn);

		final MenuButton bugBtn = new MenuButton(
				AppContext.getMessage(ProjectCommonI18nEnum.VIEW_BUG),
				IconConstants.PROJECT_BUG);
		addMenu(bugBtn);

		// final MenuButton fileBtn = new MenuButton(
		// AppContext.getMessage(ProjectCommonI18nEnum.VIEW_FILE),
		// IconConstants.PROJECT_FILE);
		// addMenu(fileBtn);
		//
		// final MenuButton riskBtn = new MenuButton(
		// AppContext.getMessage(ProjectCommonI18nEnum.VIEW_RISK),
		// IconConstants.PROJECT_RISK);
		// addMenu(riskBtn);
		//
		// final MenuButton problemBtn = new MenuButton(
		// AppContext.getMessage(ProjectCommonI18nEnum.VIEW_PROBLEM),
		// IconConstants.PROJECT_PROBLEM);
		// addMenu(problemBtn);
		//
		// final MenuButton timeBtn = new MenuButton(
		// AppContext.getMessage(ProjectCommonI18nEnum.VIEW_TIME),
		// IconConstants.PROJECT_TIME);
		// addMenu(timeBtn);
		//
		// final MenuButton standupBtn = new MenuButton(
		// AppContext.getMessage(ProjectCommonI18nEnum.VIEW_STANDAUP),
		// IconConstants.PROJECT_STANDUP);
		// addMenu(standupBtn);

		final MenuButton userBtn = new MenuButton(
				AppContext.getMessage(ProjectCommonI18nEnum.VIEW_USERS),
				IconConstants.PROJECT_USER);
		addMenu(userBtn);
	}

	@Override
	protected Button.ClickListener createDefaultButtonClickListener() {

		return new Button.ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(final ClickEvent event) {
				final String caption = ((MenuButton) event.getButton())
						.getBtnId();
				if (AppContext.getMessage(
						ProjectCommonI18nEnum.M_VIEW_PROJECT_LIST).equals(
						caption)) {
					EventBusFactory.getInstance().post(
							new ProjectEvent.GotoProjectList(this, null));
				} else if (AppContext.getMessage(
						ProjectCommonI18nEnum.M_VIEW_PROJECT_ACTIVITIES)
						.equals(caption)) {
					EventBusFactory.getInstance().post(
							new ProjectEvent.MyProjectActivities(this,
									new ProjectScreenData.ViewActivities()));
				} else if (AppContext.getMessage(
						ProjectCommonI18nEnum.VIEW_MESSAGE).equals(caption)) {
					EventBusFactory.getInstance().post(
							new MessageEvent.GotoList(this, null));
				} else if (AppContext.getMessage(
						ProjectCommonI18nEnum.VIEW_MILESTONE).equals(caption)) {
					EventBusFactory.getInstance().post(
							new MilestoneEvent.GotoList(this, null));

				} else if (AppContext.getMessage(
						ProjectCommonI18nEnum.VIEW_TASK).equals(caption)) {
					EventBusFactory.getInstance().post(
							new TaskEvent.GotoList(this, null));
				} else if (AppContext
						.getMessage(ProjectCommonI18nEnum.VIEW_BUG).equals(
								caption)) {
					EventBusFactory.getInstance().post(
							new BugEvent.GotoList(this, null));
				} else if (AppContext.getMessage(
						ProjectCommonI18nEnum.VIEW_USERS).equals(caption)) {
					EventBusFactory.getInstance().post(
							new ProjectMemberEvent.GotoList(this, null));
				}

			}
		};
	}
}

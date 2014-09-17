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
import com.esofthead.mycollab.mobile.module.project.events.MessageEvent;
import com.esofthead.mycollab.mobile.module.project.events.ProjectEvent;
import com.esofthead.mycollab.mobile.module.project.events.TaskEvent;
import com.esofthead.mycollab.mobile.ui.AbstractNavigationMenu;
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
				"&#xe614;");
		addMenu(prjListBtn);

		final MenuButton activityBtn = new MenuButton(
				AppContext
						.getMessage(ProjectCommonI18nEnum.M_VIEW_PROJECT_ACTIVITIES),
				"&#xe610;");
		addMenu(activityBtn);

		final MenuButton messageBtn = new MenuButton(
				AppContext.getMessage(ProjectCommonI18nEnum.VIEW_MESSAGE),
				"&#xf04f;");
		addMenu(messageBtn);

		final MenuButton phaseBtn = new MenuButton(
				AppContext.getMessage(ProjectCommonI18nEnum.VIEW_MILESTONE),
				"&#xf075;");
		addMenu(phaseBtn);

		final MenuButton taskBtn = new MenuButton(
				AppContext.getMessage(ProjectCommonI18nEnum.VIEW_TASK),
				"&#xe60f;");
		addMenu(taskBtn);

		final MenuButton bugBtn = new MenuButton(
				AppContext.getMessage(ProjectCommonI18nEnum.VIEW_BUG),
				"&#xf188;");
		addMenu(bugBtn);

		final MenuButton fileBtn = new MenuButton(
				AppContext.getMessage(ProjectCommonI18nEnum.VIEW_FILE),
				"&#xf017;");
		addMenu(fileBtn);

		final MenuButton riskBtn = new MenuButton(
				AppContext.getMessage(ProjectCommonI18nEnum.VIEW_RISK),
				"&#xf02d;");
		addMenu(riskBtn);

		final MenuButton problemBtn = new MenuButton(
				AppContext.getMessage(ProjectCommonI18nEnum.VIEW_PROBLEM),
				"&#xf0d2;");
		addMenu(problemBtn);

		final MenuButton timeBtn = new MenuButton(
				AppContext.getMessage(ProjectCommonI18nEnum.VIEW_TIME),
				"&#xe612;");
		addMenu(timeBtn);

		final MenuButton standupBtn = new MenuButton(
				AppContext.getMessage(ProjectCommonI18nEnum.VIEW_STANDAUP),
				"&#xf0c0;");
		addMenu(standupBtn);

		final MenuButton userBtn = new MenuButton(
				AppContext.getMessage(ProjectCommonI18nEnum.VIEW_USERS),
				"&#xe601;");
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
						ProjectCommonI18nEnum.VIEW_MESSAGE).equals(caption)) {
					EventBusFactory.getInstance().post(
							new MessageEvent.GotoList(this, null));
				} else if (AppContext.getMessage(
						ProjectCommonI18nEnum.VIEW_TASK).equals(caption)) {
					EventBusFactory.getInstance().post(
							new TaskEvent.GotoList(this, null));
				}

			}
		};
	}
}

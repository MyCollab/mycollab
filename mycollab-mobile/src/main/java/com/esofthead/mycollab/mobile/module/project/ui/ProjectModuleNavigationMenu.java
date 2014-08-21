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
import com.esofthead.mycollab.mobile.module.project.events.ProjectEvent;
import com.esofthead.mycollab.mobile.ui.AbstractNavigationMenu;
import com.esofthead.mycollab.module.project.i18n.ProjectCommonI18nEnum;
import com.esofthead.mycollab.vaadin.AppContext;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

/**
 * @author MyCollab Ltd.
 * 
 * @since 4.4.0
 * 
 */
public class ProjectModuleNavigationMenu extends AbstractNavigationMenu {

	private static final long serialVersionUID = -4087918174690391176L;

	public ProjectModuleNavigationMenu() {
		super();

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

		final MenuButton ticketBtn = new MenuButton(
				AppContext
						.getMessage(ProjectCommonI18nEnum.M_VIEW_PROJECT_FOLLOWING_TICKETS),
				"&#xe613;");
		addMenu(ticketBtn);
	}

	@Override
	protected ClickListener createDefaultButtonClickListener() {
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
				}
			}
		};
	}

}

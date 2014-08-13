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

import com.esofthead.mycollab.mobile.ui.AbstractNavigationMenu;
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

		final MenuButton dashboardBtn = new MenuButton("Dashboard", "&#xf0e4;");
		addMenu(dashboardBtn);

		final MenuButton messageBtn = new MenuButton("Messages", "&#xf04f;");
		addMenu(messageBtn);

		final MenuButton phaseBtn = new MenuButton("Phases", "&#xf075;");
		addMenu(phaseBtn);

		final MenuButton taskBtn = new MenuButton("Tasks", "&#xe60f;");
		addMenu(taskBtn);

		final MenuButton bugBtn = new MenuButton("Bugs", "&#xf188;");
		addMenu(bugBtn);

		final MenuButton fileBtn = new MenuButton("Files", "&#xf017;");
		addMenu(fileBtn);

		final MenuButton riskBtn = new MenuButton("Risks", "&#xf02d;");
		addMenu(riskBtn);

		final MenuButton problemBtn = new MenuButton("Problems", "&#xf0d2;");
		addMenu(problemBtn);

		final MenuButton timeBtn = new MenuButton("Time", "&#xe612;");
		addMenu(timeBtn);

		final MenuButton standupBtn = new MenuButton("Standup", "&#xf0c0;");
		addMenu(standupBtn);

		final MenuButton userBtn = new MenuButton("Users & Settings",
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

			}
		};
	}
}

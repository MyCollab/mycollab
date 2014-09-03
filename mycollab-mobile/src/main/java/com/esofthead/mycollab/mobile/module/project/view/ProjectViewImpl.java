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

import com.esofthead.mycollab.mobile.module.project.ui.InsideProjectNavigationMenu;
import com.esofthead.mycollab.mobile.ui.AbstractMobilePageView;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.vaadin.mobilecomponent.MobileNavigationManager;
import com.vaadin.ui.UI;

/**
 * @author MyCollab Ltd.
 * @since 4.4.0
 */

@ViewComponent
public class ProjectViewImpl extends AbstractMobilePageView implements
		ProjectView {
	private static final long serialVersionUID = -5411890906862175950L;

	public ProjectViewImpl() {
		((MobileNavigationManager) UI.getCurrent().getContent())
				.setNavigationMenu(new InsideProjectNavigationMenu());
	}
}

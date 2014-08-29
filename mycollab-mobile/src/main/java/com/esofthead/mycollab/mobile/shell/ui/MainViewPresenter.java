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
package com.esofthead.mycollab.mobile.shell.ui;

import com.esofthead.mycollab.mobile.mvp.AbstractPresenter;
import com.esofthead.mycollab.vaadin.mvp.ScreenData;
import com.esofthead.vaadin.mobilecomponent.MobileNavigationManager;
import com.vaadin.ui.ComponentContainer;

/**
 * @author MyCollab Ltd.
 * @since 3.0
 */
public class MainViewPresenter extends AbstractPresenter<MainView> {
	private static final long serialVersionUID = 7699660189568510585L;

	public MainViewPresenter() {
		super(MainView.class);
	}

	@Override
	protected void onGo(ComponentContainer navigationManager, ScreenData<?> data) {
		((MobileNavigationManager) navigationManager).setNavigationMenu(null);
		((MobileNavigationManager) navigationManager).navigateTo(view
				.getWidget());
	}
}

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
package com.esofthead.mycollab.mobile.module.crm.view;

import com.esofthead.mycollab.mobile.mvp.AbstractPresenter;
import com.esofthead.mycollab.vaadin.mvp.ScreenData;
import com.vaadin.addon.touchkit.ui.NavigationManager;

/**
 * @author MyCollab Ltd.
 * @since 3.0
 */
public class ActivityStreamPresenter extends
		AbstractPresenter<ActivityStreamView> {
	private static final long serialVersionUID = -7072237366355902761L;

	public ActivityStreamPresenter() {
		super(ActivityStreamView.class);
	}

	@Override
	protected void onGo(NavigationManager container, ScreenData<?> data) {
		container.navigateTo(view.getWidget());
	}
}

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
package com.esofthead.mycollab.mobile.ui;

import com.esofthead.mycollab.vaadin.mvp.PageView;
import com.esofthead.vaadin.mobilecomponent.MobileNavigationView;
import com.vaadin.addon.touchkit.ui.NavigationManager;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;

/**
 * @author MyCollab Inc.
 * 
 * @since 4.3.1
 */
public class AbstractMobileSwipeView extends MobileNavigationView implements PageView {
	private static final long serialVersionUID = -5179416042698544018L;

	public AbstractMobileSwipeView() {
		super();
		this.setStyleName("mobilenavview");
        this.setToggleButton(true);
	}

	@Override
	public ComponentContainer getWidget() {
		return this;
	}

	@Override
	public void addViewListener(ViewListener listener) {
		addListener(ViewEvent.VIEW_IDENTIFIER, ViewEvent.class, listener,
				ViewListener.viewInitMethod);
	}

	@Override
	public NavigationManager getNavigationManager() {
		Component parent = this.getParent();
		while (parent != null) {
			if (parent instanceof NavigationManager)
				return (NavigationManager) parent;
			else
				parent = parent.getParent();
		}
		return null;
	}

}

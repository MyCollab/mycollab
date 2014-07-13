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
import com.vaadin.addon.touchkit.ui.TabBarView;
import com.vaadin.server.Resource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.TabSheet.Tab;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.2
 * 
 */
public class AbstractMobileTabPageView extends TabBarView implements PageView {
	private static final long serialVersionUID = 664039475002291943L;

	@Override
	public ComponentContainer getWidget() {
		return this;
	}

	/*
	 * Now we use EventBus to manage events
	 * 
	 * @see com.esofthead.mycollab.eventmanager.EventBusFactory
	 */
	@Deprecated
	@Override
	public void addViewListener(ViewListener listener) {
		// Do nothing
	}

	@Override
	public Tab addTab(Component tabContent, String caption, Resource icon) {
		Tab newTab = super.addTab(tabContent, caption, icon);
		((Button) newTab).setHtmlContentAllowed(true);
		return newTab;
	}

}

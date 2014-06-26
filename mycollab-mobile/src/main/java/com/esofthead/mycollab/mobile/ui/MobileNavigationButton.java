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

import com.vaadin.addon.touchkit.ui.NavigationButton;
import com.vaadin.ui.Component;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.2
 * 
 */
public class MobileNavigationButton extends NavigationButton {
	private static final long serialVersionUID = 5591853157860002587L;

	public MobileNavigationButton() {
		super();
	}

	public MobileNavigationButton(String caption) {
		super(caption);
	}

	public MobileNavigationButton(Component targetView) {
		super(targetView);
	}

	public MobileNavigationButton(String caption, Component targetView) {
		super(caption, targetView);
	}

	@Override
	public void beforeClientResponse(boolean initial) {
		// Override to stop auto set caption for button
	}
}

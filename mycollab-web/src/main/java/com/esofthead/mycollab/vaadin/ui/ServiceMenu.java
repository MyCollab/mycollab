/**
 * This file is part of mycollab-web.
 *
 * mycollab-web is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-web is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-web.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.vaadin.ui;

import java.util.Iterator;

import org.vaadin.peter.buttongroup.ButtonGroup;

import com.vaadin.server.Resource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public class ServiceMenu extends ButtonGroup {
	private static final long serialVersionUID = 1L;

	private static final String COMPONENT_STYLENAME = "service-menu";
	private static final String SELECTED_STYLENAME = "selected";

	public ServiceMenu() {
		super();
		this.setStyleName(COMPONENT_STYLENAME);
	}

	public void addService(String serviceName, Resource linkIcon, ClickListener listener) {
        Button newService = new Button(serviceName, listener);
		newService.setIcon(linkIcon);

		this.addButton(newService);
	}

	public void selectService(int index) {
		Iterator<Component> iterator = this.iterator();

		int i = 0;
		while (iterator.hasNext()) {
			Component comp = iterator.next();
			if (i == index) {
				comp.addStyleName(SELECTED_STYLENAME);
			} else {
				comp.removeStyleName(SELECTED_STYLENAME);
			}
			i++;
		}
	}

}

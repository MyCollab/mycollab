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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.vaadin.hene.popupbutton.PopupButton;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.1
 * 
 */
public class NotificationButton extends PopupButton implements
		PopupButton.PopupVisibilityListener {
	private static final long serialVersionUID = 2908372640829060184L;

	private final List<Component> notificationItems;
	private final VerticalLayout notificationContainer;

	public NotificationButton() {
		super();
		notificationItems = new ArrayList<Component>();
		notificationContainer = new VerticalLayout();
		notificationContainer.setMargin(true);
		this.setContent(notificationContainer);
		this.setIcon(MyCollabResource
				.newResource("icons/16/notification-indicator.png"));
		this.setStyleName("notification-button");

		addPopupVisibilityListener(this);
	}

	@Override
	public void popupVisibilityChange(PopupVisibilityEvent event) {
		notificationContainer.removeAllComponents();

		if (notificationItems.size() > 0) {
			Iterator<Component> iterator = notificationItems.iterator();
			while (iterator.hasNext()) {
				notificationContainer.addComponent(iterator.next());
			}
		} else {
			Label noItemLbl = new Label("No notification right now");
			notificationContainer.addComponent(noItemLbl);
			notificationContainer.setComponentAlignment(noItemLbl,
					Alignment.MIDDLE_CENTER);
		}
	}

	public void addNotification(Component notification) {
		notificationItems.add(notification);
		updateCaption();
	}

	public void removeNotification(Component notification) {
		notificationItems.remove(notification);
		updateCaption();
	}

	protected void updateCaption() {
		if (notificationItems.size() > 0) {
			this.setCaption(String.valueOf(notificationItems.size()));
		} else {
			this.setCaption(null);
		}
	}
}

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
import java.util.List;

import org.vaadin.hene.popupbutton.PopupButton;

import com.esofthead.mycollab.common.ui.components.AbstractNotification;
import com.esofthead.mycollab.eventmanager.ApplicationEventListener;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.shell.events.ShellEvent;
import com.google.common.eventbus.Subscribe;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.1
 * 
 */
public class NotificationButton extends PopupButton implements
		PopupButton.PopupVisibilityListener,
		ApplicationEventListener<ShellEvent.NewNotification> {
	private static final long serialVersionUID = 2908372640829060184L;

	private final List<AbstractNotification> notificationItems;
	private final VerticalLayout notificationContainer;

	public NotificationButton() {
		super();
		notificationItems = new ArrayList<AbstractNotification>();
		notificationContainer = new VerticalLayout();
		notificationContainer.setMargin(true);
		this.setContent(notificationContainer);
		this.setIcon(MyCollabResource
				.newResource(WebResourceIds._16_notification_indicator));
		this.setStyleName("notification-button");

		addPopupVisibilityListener(this);
		EventBusFactory.getInstance().register(this);
	}

	@Override
	public void popupVisibilityChange(PopupVisibilityEvent event) {
		notificationContainer.removeAllComponents();

		if (notificationItems.size() > 0) {
			for (AbstractNotification item : notificationItems) {
				HorizontalLayout notificationItem = new HorizontalLayout();
				notificationItem.setSpacing(true);
				Label notificationType = new Label(item.getType() + ":");
				notificationType.setStyleName("notification-type");
				notificationType.addStyleName("notification-type-"
						+ item.getType());
				notificationItem.addComponent(notificationType);

				Label notificationLbl = new Label(item.renderContent(),
						ContentMode.HTML);
				notificationItem.addComponent(notificationLbl);
				notificationItem.setExpandRatio(notificationLbl, 1.0f);
				notificationContainer.addComponent(notificationItem);
			}
		} else {
			Label noItemLbl = new Label("There is no notification right now");
			notificationContainer.addComponent(noItemLbl);
			notificationContainer.setComponentAlignment(noItemLbl,
					Alignment.MIDDLE_CENTER);
		}
	}

	public void addNotification(AbstractNotification notification) {
		notificationItems.add(notification);
		updateCaption();
	}

	public void removeNotification(AbstractNotification notification) {
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

	@Subscribe
	@Override
	public void handle(ShellEvent.NewNotification event) {
		if (event.getData() instanceof AbstractNotification) {
			addNotification((AbstractNotification) event.getData());
		}
	}
}

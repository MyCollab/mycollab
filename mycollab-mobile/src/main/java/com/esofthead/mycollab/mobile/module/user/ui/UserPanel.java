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
package com.esofthead.mycollab.mobile.module.user.ui;

import com.esofthead.mycollab.eventmanager.ApplicationEvent;
import com.esofthead.mycollab.eventmanager.ApplicationEventListener;
import com.esofthead.mycollab.eventmanager.EventBus;
import com.esofthead.mycollab.events.SessionEvent;
import com.esofthead.mycollab.events.SessionEvent.UserProfileChangeEvent;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.UserAvatarControlFactory;
import com.vaadin.server.Sizeable;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;

/**
 * 
 * @author MyCollab Ltd.
 * @since 3.0
 * 
 */
public class UserPanel extends HorizontalLayout {
	private static final long serialVersionUID = 1L;

	private final Image userAvatar;
	private final Button userName;

	public UserPanel() {
		super();
		setStyleName("user-info-panel");
		setSpacing(true);
		setMargin(true);
		setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
		setHeight(Sizeable.SIZE_UNDEFINED, Sizeable.Unit.PIXELS);

		userAvatar = new Image();
		userAvatar.setWidth("24px");
		userAvatar.setHeight("24px");

		userName = new Button();
		userName.setWidth("100%");
		userName.setStyleName("user-btn");

		setUserInfo();

		addComponent(userAvatar);
		addComponent(userName);
		setExpandRatio(userName, 1.0f);

		// add listener to listen the change avatar or user information to
		// update panel display info
		EventBus.getInstance()
				.addListener(
						new ApplicationEventListener<SessionEvent.UserProfileChangeEvent>() {
							private static final long serialVersionUID = 1L;

							@Override
							public Class<? extends ApplicationEvent> getEventType() {
								return SessionEvent.UserProfileChangeEvent.class;
							}

							@Override
							public void handle(UserProfileChangeEvent event) {
								setUserInfo();
							}
						});

	}

	private void setUserInfo() {
		userAvatar.setSource(UserAvatarControlFactory.createAvatarResource(
				AppContext.getUserAvatarId(), 24));
		userName.setCaption(AppContext.getSession().getDisplayName());
	}

}

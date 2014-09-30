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
package com.esofthead.mycollab.common.ui.components;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.EventListener;

import com.esofthead.mycollab.eventmanager.ApplicationEvent;
import com.esofthead.mycollab.module.ecm.domain.ExternalDrive;
import com.vaadin.ui.Window;
import com.vaadin.util.ReflectTools;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.5.2
 *
 */
public abstract class AbstractCloudDriveOAuthWindow extends Window {
	private static final long serialVersionUID = 1L;

	public void addExternalDriveConnectedListener(
			ExternalDriveConnectedListener listener) {
		this.addListener(ExternalDriveConnectedEvent.VIEW_IDENTIFIER,
				ExternalDriveConnectedEvent.class, listener,
				ExternalDriveConnectedListener.viewInitMethod);
	}

	public static interface ExternalDriveConnectedListener extends
			EventListener, Serializable {
		public static final Method viewInitMethod = ReflectTools.findMethod(
				ExternalDriveConnectedListener.class, "connectedSuccess",
				ExternalDriveConnectedEvent.class);

		void connectedSuccess(ExternalDriveConnectedEvent event);
	}

	public static class ExternalDriveConnectedEvent extends ApplicationEvent {
		private static final long serialVersionUID = 1L;

		public static final String VIEW_IDENTIFIER = "externalDriveConnectedSuccess";

		public ExternalDriveConnectedEvent(Object source, ExternalDrive data) {
			super(source, data);
		}
	}
}

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
package com.esofthead.mycollab.shell.events;

import com.esofthead.mycollab.eventmanager.ApplicationEvent;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
@SuppressWarnings("serial")
public class ShellEvent {

	public static class NotifyErrorEvent extends ApplicationEvent {

		public NotifyErrorEvent(Object source, Throwable data) {
			super(source, data);
		}
	}

	public static class GotoMainPage extends ApplicationEvent {

		public GotoMainPage(Object source, Object data) {
			super(source, data);
		}
	}

	public static class LogOut extends ApplicationEvent {

		public LogOut(Object source, Object data) {
			super(source, data);
		}
	}

	public static class GotoProjectModule extends ApplicationEvent {

		public GotoProjectModule(Object source, Object data) {
			super(source, data);
		}
	}

	public static class GotoCrmModule extends ApplicationEvent {

		public GotoCrmModule(Object source, Object data) {
			super(source, data);
		}
	}

	public static class GotoFileModule extends ApplicationEvent {

		public GotoFileModule(Object source, Object data) {
			super(source, data);
		}
	}

	public static class GotoUserAccountModule extends ApplicationEvent {

		public GotoUserAccountModule(Object source, Object data) {
			super(source, data);
		}
	}

	public static class GotoConsolePage extends ApplicationEvent {

		public GotoConsolePage(Object source, Object data) {
			super(source, data);
		}
	}

	public static class GotoForgotPasswordPage extends ApplicationEvent {

		public GotoForgotPasswordPage(Object source, Object data) {
			super(source, data);
		}
	}

	public static class NewNotification extends ApplicationEvent {

		public NewNotification(Object source, Object data) {
			super(source, data);
		}
	}
}

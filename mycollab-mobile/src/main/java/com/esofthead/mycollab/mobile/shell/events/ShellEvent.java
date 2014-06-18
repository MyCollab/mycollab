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
package com.esofthead.mycollab.mobile.shell.events;

import com.esofthead.mycollab.eventmanager.ApplicationEvent;

/**
 * @author MyCollab Ltd.
 * @since 3.0
 */
public class ShellEvent {

	public static class GotoLoginView extends ApplicationEvent {
		private static final long serialVersionUID = -551175801973985055L;

		public GotoLoginView(Object source, Object data) {
			super(source, data);
		}
	}

	public static class GotoMainPage extends ApplicationEvent {
		private static final long serialVersionUID = -2176247115043539217L;

		public GotoMainPage(Object source, Object data) {
			super(source, data);
		}
	}

	public static class GotoCrmModule extends ApplicationEvent {
		private static final long serialVersionUID = 4059100422490245421L;

		public GotoCrmModule(Object source, Object data) {
			super(source, data);
		}
	}
}

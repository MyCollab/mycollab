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
package com.esofthead.mycollab.mobile.module.project.events;

import com.esofthead.mycollab.eventmanager.ApplicationEvent;

/**
 * @author MyCollab Ltd.
 * 
 * @since 4.4.0
 *
 */
public class ProjectEvent {
	public static class GotoProjectList extends ApplicationEvent {
		private static final long serialVersionUID = -3090515127947058922L;

		public GotoProjectList(Object source, Object data) {
			super(source, data);
		}

	}

	public static class GotoLogin extends ApplicationEvent {

		private static final long serialVersionUID = -295885940569206839L;

		public GotoLogin(Object source, Object data) {
			super(source, data);
		}

	}

	public static class PlainLogin extends ApplicationEvent {

		private static final long serialVersionUID = -4428146398305430922L;

		public PlainLogin(Object source, Object data) {
			super(source, data);
		}

	}

	public static class GotoMyProject extends ApplicationEvent {

		private static final long serialVersionUID = -4439780369902098133L;

		public GotoMyProject(Object source, Object data) {
			super(source, data);
		}
	}

	public static class MyProjectActivities extends ApplicationEvent {

		private static final long serialVersionUID = -4804098825918852657L;

		public MyProjectActivities(Object source, Object data) {
			super(source, data);
		}

	}

	public static class AllActivities extends ApplicationEvent {
		private static final long serialVersionUID = 4660705128694728789L;

		public AllActivities(Object source, Object data) {
			super(source, data);
		}
	}
}

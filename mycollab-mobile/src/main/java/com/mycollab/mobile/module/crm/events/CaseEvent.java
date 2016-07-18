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
package com.mycollab.mobile.module.crm.events;

import com.mycollab.events.ApplicationEvent;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.0
 * 
 */
public class CaseEvent {
	public static class Save extends ApplicationEvent {
		private static final long serialVersionUID = -8233913139722949767L;

		public Save(Object source, Object data) {
			super(source, data);
		}
	}

	public static class Search extends ApplicationEvent {
		private static final long serialVersionUID = 1753078348208999466L;

		public Search(Object source, Object data) {
			super(source, data);
		}
	}

	public static class GotoList extends ApplicationEvent {
		private static final long serialVersionUID = -4100336975752268823L;

		public GotoList(Object source, Object data) {
			super(source, data);
		}
	}

	public static class GotoAdd extends ApplicationEvent {
		private static final long serialVersionUID = 5537133797081861218L;

		public GotoAdd(Object source, Object data) {
			super(source, data);
		}
	}

	public static class GotoRead extends ApplicationEvent {
		private static final long serialVersionUID = 2024499664273723194L;

		public GotoRead(Object source, Object data) {
			super(source, data);
		}
	}

	public static class GotoEdit extends ApplicationEvent {
		private static final long serialVersionUID = 28575528879169859L;

		public GotoEdit(Object source, Object data) {
			super(source, data);
		}
	}

	public static class GoToRelatedItems extends ApplicationEvent {
		private static final long serialVersionUID = 5283096123753126321L;

		public GoToRelatedItems(Object source, Object data) {
			super(source, data);
		}

	}
}

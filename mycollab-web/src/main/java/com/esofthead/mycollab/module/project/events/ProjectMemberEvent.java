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

package com.esofthead.mycollab.module.project.events;

import java.io.Serializable;
import java.util.List;

import com.esofthead.mycollab.eventmanager.ApplicationEvent;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public class ProjectMemberEvent {

	public static class InviteProjectMembers implements Serializable {
		private static final long serialVersionUID = 1L;

		private List<String> inviteEmails;
		private Integer roleId;
		private String inviteMessage;

		public InviteProjectMembers(List<String> emails, Integer roleId,
				String inviteMessage) {
			this.inviteEmails = emails;
			this.roleId = roleId;
			this.inviteMessage = inviteMessage;
		}

		public List<String> getInviteEmails() {
			return inviteEmails;
		}

		public Integer getRoleId() {
			return roleId;
		}

		public String getInviteMessage() {
			return inviteMessage;
		}

		public void setInviteMessage(String inviteMessage) {
			this.inviteMessage = inviteMessage;
		}
	}

	public static class Search extends ApplicationEvent {
		private static final long serialVersionUID = 1L;

		public Search(Object source, Object data) {
			super(source, data);
		}
	}

	public static class GotoList extends ApplicationEvent {
		private static final long serialVersionUID = 1L;

		public GotoList(Object source, Object data) {
			super(source, data);
		}
	}

	public static class GotoInviteMembers extends ApplicationEvent {
		private static final long serialVersionUID = 1L;

		public GotoInviteMembers(Object source, Object data) {
			super(source, data);
		}
	}

	public static class GotoRead extends ApplicationEvent {
		private static final long serialVersionUID = 1L;

		public GotoRead(Object source, Object data) {
			super(source, data);
		}
	}

	public static class GotoEdit extends ApplicationEvent {
		private static final long serialVersionUID = 1L;

		public GotoEdit(Object source, Object data) {
			super(source, data);
		}
	}
}

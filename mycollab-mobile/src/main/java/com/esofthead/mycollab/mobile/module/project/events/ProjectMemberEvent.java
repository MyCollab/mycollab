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

import java.io.Serializable;
import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 4.5.2
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
        private static final long serialVersionUID = -4614125940848752982L;

        public Search(Object source, Object data) {
            super(source, data);
        }
    }

    public static class GotoList extends ApplicationEvent {
        private static final long serialVersionUID = 5713208630722347339L;

        public GotoList(Object source, Object data) {
            super(source, data);
        }
    }

    public static class GotoInviteMembers extends ApplicationEvent {
        private static final long serialVersionUID = 8393204762541970672L;

        public GotoInviteMembers(Object source, Object data) {
            super(source, data);
        }
    }

    public static class GotoRead extends ApplicationEvent {
        private static final long serialVersionUID = -7306650933057476044L;

        public GotoRead(Object source, Object data) {
            super(source, data);
        }
    }

    public static class GotoEdit extends ApplicationEvent {
        private static final long serialVersionUID = -5237825362340624200L;

        public GotoEdit(Object source, Object data) {
            super(source, data);
        }
    }
}

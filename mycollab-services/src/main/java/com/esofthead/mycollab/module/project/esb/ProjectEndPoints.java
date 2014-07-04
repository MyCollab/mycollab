/**
 * This file is part of mycollab-services.
 *
 * mycollab-services is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-services is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-services.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.module.project.esb;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public class ProjectEndPoints {

	public static final String PROJECT_REMOVE_ENDPOINT = "direct:projectDelete";

	public static final String PROJECT_MEMBER_DELETE_ENDPOINT = "direct:projectMemberDelete";

	public static final String PROJECT_MESSAGE_REMOVE_ENDPOINT = "direct:messageDelete";

	public static final String PROJECT_BUG_REMOVE_ENDPOINT = "direct:bugDelete";

	public static final String PROJECT_COMPONENT_REMOVE_ENDPOINT = "direct:componentDelete";

	public static final String PROJECT_VERSION_REMOVE_ENDPOINT = "direct:versionDelete";

	public static final String PROJECT_MILESTONE_REMOVE_ENDPOINT = "direct:milestoneDelete";

	public static final String PROJECT_TASK_REMOVE_ENDPOINT = "direct:taskDelete";

	public static final String PROJECT_TASKLIST_REMOVE_ENDPOINT = "direct:taskListDelete";

	public static final String PROJECT_SEND_INVITATION_USER = "direct:projectMemberInvitation";
}

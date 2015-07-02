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
package com.esofthead.mycollab.module.project.esb

/**
 *
 * @author MyCollab Ltd.
 * @since 1.0
 *
 */
object ProjectEndPoints {
    val PROJECT_REMOVE_ENDPOINT: String = "direct:projectDelete"
    val PROJECT_MEMBER_DELETE_ENDPOINT: String = "direct:projectMemberDelete"
    val PROJECT_MESSAGE_REMOVE_ENDPOINT: String = "direct:messageDelete"
    val PROJECT_BUG_REMOVE_ENDPOINT: String = "direct:bugDelete"
    val PROJECT_COMPONENT_REMOVE_ENDPOINT: String = "direct:componentDelete"
    val PROJECT_VERSION_REMOVE_ENDPOINT: String = "direct:versionDelete"
    val PROJECT_MILESTONE_REMOVE_ENDPOINT: String = "direct:milestoneDelete"
    val PROJECT_TASK_REMOVE_ENDPOINT: String = "direct:taskDelete"
    val PROJECT_TASKLIST_REMOVE_ENDPOINT: String = "direct:taskListDelete"
    val PROJECT_SEND_INVITATION_USER: String = "direct:projectMemberInvitation"
}
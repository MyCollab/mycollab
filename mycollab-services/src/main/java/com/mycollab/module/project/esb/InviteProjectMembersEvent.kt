package com.mycollab.module.project.esb

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
class InviteProjectMembersEvent(val emails: Array<String>, val projectId: Int, val projectRoleId: Int?,
                                val inviteUser: String, val inviteMessage: String, val sAccountId: Int) {
}
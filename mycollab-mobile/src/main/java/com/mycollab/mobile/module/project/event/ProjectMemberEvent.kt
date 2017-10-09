package com.mycollab.mobile.module.project.event

import com.mycollab.vaadin.event.ApplicationEvent

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
object ProjectMemberEvent {
    class InviteProjectMembers(val inviteEmails: List<String>, val roleId: Int,
                               val inviteMessage: String)

    class GotoList(source: Any, val data: Any?) : ApplicationEvent(source)

    class GotoInviteMembers(source: Any, val data: Any?) : ApplicationEvent(source)

    class GotoRead(source: Any, val data: Any?) : ApplicationEvent(source)

    class GotoEdit(source: Any, val data: Any?) : ApplicationEvent(source)
}
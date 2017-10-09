package com.mycollab.module.user.esb

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
class SendUserInvitationEvent(val invitee: String, val password:String, val inviter: String, val subdomain: String, val sAccountId: Int)
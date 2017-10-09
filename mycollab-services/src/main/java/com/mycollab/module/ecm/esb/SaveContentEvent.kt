package com.mycollab.module.ecm.esb

import com.mycollab.module.ecm.domain.Content

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
class SaveContentEvent(val content: Content, val createdUser: String, val sAccountId: Int?)
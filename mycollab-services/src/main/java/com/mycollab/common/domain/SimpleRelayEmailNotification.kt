package com.mycollab.common.domain

import com.mycollab.core.utils.StringUtils
import com.mycollab.module.user.domain.SimpleUser

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
open class SimpleRelayEmailNotification : RelayEmailNotificationWithBLOBs() {

    var accountLogo: String? = null
    var changeByUserFullName: String? = null
        get() = if (StringUtils.isBlank(field)) {
            StringUtils.extractNameFromEmail(changeby)
        } else field
    var notifyUsers: List<SimpleUser> = mutableListOf()
}

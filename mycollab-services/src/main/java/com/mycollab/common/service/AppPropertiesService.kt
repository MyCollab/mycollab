package com.mycollab.common.service

import java.util.Date

/**
 * @author MyCollab Ltd
 * @since 5.2.12
 */
interface AppPropertiesService {
    val sysId: String

    val startDate: Date

    val edition: String
}

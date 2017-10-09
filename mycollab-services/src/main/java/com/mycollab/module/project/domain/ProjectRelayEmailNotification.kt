package com.mycollab.module.project.domain

import com.mycollab.common.domain.SimpleRelayEmailNotification

/**
 * @author MyCollab Ltd
 * @since 1.0.0
 */
class ProjectRelayEmailNotification : SimpleRelayEmailNotification() {

    var projectId: Int = 0
}

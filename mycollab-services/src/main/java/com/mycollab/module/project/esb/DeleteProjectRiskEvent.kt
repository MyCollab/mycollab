package com.mycollab.module.project.esb

import com.mycollab.module.project.domain.Risk

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
class DeleteProjectRiskEvent(val risks: Array<Risk>, val username: String?, val accountId: Int)
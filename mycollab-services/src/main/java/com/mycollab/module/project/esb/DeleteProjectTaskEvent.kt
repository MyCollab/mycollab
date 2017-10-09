package com.mycollab.module.project.esb

import com.mycollab.module.project.domain.Task

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
class DeleteProjectTaskEvent(val tasks: Array<Task>, val username: String?, val accountId: Int)
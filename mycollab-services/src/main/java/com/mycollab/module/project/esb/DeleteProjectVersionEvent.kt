package com.mycollab.module.project.esb

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
class DeleteProjectVersionEvent(val username: String, val accountId: Int, val projectId: Int, val versionId: Int)
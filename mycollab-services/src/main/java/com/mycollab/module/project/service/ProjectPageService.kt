package com.mycollab.module.project.service

import com.mycollab.db.persistence.service.IService
import com.mycollab.module.page.domain.Page

/**
 *
 * @author MyCollab Ltd.
 * @since 4.5.6
 */
interface ProjectPageService : IService {
    /**
     *
     * @param page
     * @param createdUser
     * @param projectId
     * @param accountId
     */
    fun savePage(page: Page, createdUser: String, projectId: Int, accountId: Int)

    /**
     *
     * @param path
     * @param requestedUser
     * @return
     */
    fun getPage(path: String, requestedUser: String): Page
}

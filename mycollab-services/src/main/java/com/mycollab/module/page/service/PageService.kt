package com.mycollab.module.page.service

import com.mycollab.db.persistence.service.IService
import com.mycollab.module.page.domain.Folder
import com.mycollab.module.page.domain.Page
import com.mycollab.module.page.domain.PageResource
import com.mycollab.module.page.domain.PageVersion

/**
 * @author MyCollab Ltd.
 * @since 4.4.0
 */
interface PageService : IService {
    /**
     * @param page
     * @param createdUser
     */
    fun savePage(page: Page, createdUser: String)

    /**
     * @param path
     * @param requestedUser
     * @return
     */
    fun getPage(path: String, requestedUser: String): Page

    /**
     * @param path
     * @return
     */
    fun getFolder(path: String): Folder?

    /**
     * @param path
     * @return
     */
    fun getPageVersions(path: String): List<PageVersion>

    /**
     * @param path
     * @param versionName
     * @return
     */
    fun getPageByVersion(path: String, versionName: String): Page

    /**
     * @param path
     * @param versionName
     * @return the restore page
     */
    fun restorePage(path: String, versionName: String): Page

    /**
     * @param folder
     * @param createdUser
     */
    fun createFolder(folder: Folder, createdUser: String)

    /**
     * @param path
     * @return
     */
    fun getPages(path: String, requestedUser: String): List<Page>

    /**
     * @param path
     * @param requestedUser
     * @return
     */
    fun getResources(path: String, requestedUser: String): List<PageResource>

    /**
     * @param path
     */
    fun removeResource(path: String)
}

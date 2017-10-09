package com.mycollab.module.ecm.service

import com.mycollab.module.ecm.domain.Content
import com.mycollab.module.ecm.domain.Folder
import com.mycollab.module.ecm.domain.Resource

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
interface ContentJcrDao {

    fun saveContent(content: Content, createdUser: String)

    fun createFolder(folder: Folder, createdUser: String)

    fun rename(oldPath: String, newPath: String)

    fun getResource(path: String): Resource

    fun removeResource(path: String)

    fun getResources(path: String): List<Resource>

    fun getContents(path: String): List<Content>

    fun getSubFolders(path: String): List<Folder>

    fun searchResourcesByName(baseFolderPath: String, resourceName: String): List<Resource>

    fun moveResource(oldPath: String, destinationPath: String)
}

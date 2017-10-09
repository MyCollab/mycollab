package com.mycollab.module.ecm.service

import com.mycollab.cache.IgnoreCacheClass
import com.mycollab.db.persistence.service.IService
import com.mycollab.module.ecm.domain.Content
import com.mycollab.module.ecm.domain.Folder
import com.mycollab.module.ecm.domain.Resource

import java.io.InputStream

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@IgnoreCacheClass
interface ResourceService : IService {

    /**
     * @param baseFolderPath
     * @param folderName
     * @param description
     * @param createdBy
     * @return
     */
    fun createNewFolder(baseFolderPath: String, folderName: String, description: String, createdBy: String): Folder

    /**
     * @param path
     * @return
     */
    fun getResources(path: String): List<Resource>

    /**
     * @param path
     * @return
     */
    fun getResource(path: String): Resource

    /**
     * @param path
     * @return
     */
    fun getContents(path: String): List<Content>

    /**
     * @param path
     * @return
     */
    fun getSubFolders(path: String): List<Folder>

    /**
     * @param content
     * @param createdUser
     * @param refStream
     * @param sAccountId
     */
    fun saveContent(content: Content, createdUser: String, refStream: InputStream, sAccountId: Int?)

    /**
     * @param path
     */
    fun removeResource(path: String)

    /**
     * @param path
     * @param userDelete
     * @param sAccountId
     */
    fun removeResource(path: String, userDelete: String, isUpdateDriveInfo: Boolean?, sAccountId: Int?)

    /**
     * @param path
     * @return
     */
    fun getContentStream(path: String): InputStream

    /**
     * @param oldPath
     * @param newPath
     * @param userUpdate
     */
    fun rename(oldPath: String, newPath: String, userUpdate: String)

    /**
     * @param baseFolderPath
     * @param resourceName
     * @return
     */
    fun searchResourcesByName(baseFolderPath: String, resourceName: String): List<Resource>

    /**
     * @param oldPath
     * @param newPath
     * @param userMove
     */
    fun moveResource(oldPath: String, newPath: String, userMove: String)

    /**
     * @param path
     * @return
     */
    fun getParentFolder(path: String): Folder?
}

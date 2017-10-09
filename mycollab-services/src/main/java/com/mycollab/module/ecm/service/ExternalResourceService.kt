package com.mycollab.module.ecm.service

import com.mycollab.cache.IgnoreCacheClass
import com.mycollab.db.persistence.service.IService
import com.mycollab.module.ecm.domain.*

import java.io.InputStream

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@IgnoreCacheClass
interface ExternalResourceService : IService {
    /**
     * @param drive
     * @param path
     * @return
     */
    fun getResources(drive: ExternalDrive, path: String): List<Resource>

    /**
     * @param drive
     * @param path
     * @return
     */
    fun getSubFolders(drive: ExternalDrive, path: String): List<ExternalFolder>

    /**
     * @param drive
     * @param path
     * @return
     */
    fun getCurrentResourceByPath(drive: ExternalDrive, path: String): Resource

    /**
     * @param drive
     * @param childPath
     * @return
     */
    fun getParentResourceFolder(drive: ExternalDrive, childPath: String): Folder

    /**
     * @param drive
     * @param path
     * @return
     */
    fun createNewFolder(drive: ExternalDrive, path: String): Folder

    /**
     * @param drive
     * @param content
     * @param in
     */
    fun saveContent(drive: ExternalDrive, content: Content, `in`: InputStream)

    /**
     * @param drive
     * @param oldPath
     * @param newPath
     */
    fun rename(drive: ExternalDrive, oldPath: String, newPath: String)

    /**
     * @param drive
     * @param path
     */
    fun deleteResource(drive: ExternalDrive, path: String)

    /**
     * @param drive
     * @param path
     * @return
     */
    fun download(drive: ExternalDrive, path: String): InputStream

    /**
     * @param drive
     * @param fromPath
     * @param toPath
     */
    fun move(drive: ExternalDrive, fromPath: String, toPath: String)
}

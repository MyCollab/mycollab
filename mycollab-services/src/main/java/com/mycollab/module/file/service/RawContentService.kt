package com.mycollab.module.file.service

import com.mycollab.db.persistence.service.IService

import java.io.InputStream

/**
 * MyCollab content repository has two parts: we use jackrabbit to keep content
 * meta data (such as file name, path, tag and etc more in future). The service
 * to get content data (input stream) are handled by
 * `RawContentService`. In practice, you should not work in low-level
 * API as `RawContentService` but `ContentService`
 *
 * @author MyCollab Ltd.
 * @since 1.0
 */
interface RawContentService : IService {
    /**
     * Save content
     *
     * @param objectPath path of content
     * @param stream     input stream
     */
    fun saveContent(objectPath: String, stream: InputStream)

    /**
     * Get content stream
     *
     * @param objectPath path of content
     * @return stream of content has path `objectPath`, otherwise
     * return null
     */
    fun getContentStream(objectPath: String): InputStream

    /**
     * Remove content
     *
     * @param objectPath path of content
     */
    fun removePath(objectPath: String)

    /**
     * Rename content
     *
     * @param oldPath old path of content
     * @param newPath new path of content
     */
    fun renamePath(oldPath: String, newPath: String)

    /**
     * Move content
     *
     * @param oldPath         old path of content
     * @param destinationPath new path of content
     */
    fun movePath(oldPath: String, destinationPath: String)

    /**
     * Get size of content
     *
     * @param objectPath path of content
     * @return return size of content has path is `path`, return 0 if
     * content is not existed
     */
    fun getSize(objectPath: String): Long

}
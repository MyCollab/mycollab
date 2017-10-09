package com.mycollab.module.file

/**
 * @author MyCollab Ltd
 * @since 5.0.10
 */
object PathUtils {
    @JvmStatic fun buildPath(sAccountId: Int?, objectPath: String): String {
        return (if (sAccountId == null) "" else sAccountId.toString() + "/") + objectPath
    }

    @JvmStatic fun getProjectLogoPath(accountId: Int?, projectId: Int?): String {
        return String.format("%d/project/%d/.attachments", accountId, projectId)
    }

    @JvmStatic fun getEntityLogoPath(accountId: Int?): String {
        return String.format("%d/.assets", accountId)
    }

    @JvmStatic fun getProjectDocumentPath(accountId: Int?, projectId: Int?): String {
        return String.format("%d/project/%d/.page", accountId, projectId)
    }

    @JvmStatic fun buildLogoPath(sAccountId: Int?, logoFileName: String, logoSize: Int?): String {
        return String.format("%d/.assets/%s_%d.png", sAccountId, logoFileName, logoSize)
    }

    @JvmStatic fun buildFavIconPath(sAccountId: Int?, favIconFileName: String): String {
        return String.format("%d/.assets/%s.ico", sAccountId, favIconFileName)
    }
}

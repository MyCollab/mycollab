package com.mycollab.module.file

import com.mycollab.core.MyCollabException
import com.mycollab.module.crm.CrmTypeConstants
import com.mycollab.module.project.ProjectTypeConstants

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
object AttachmentUtils {
    private val COMMENT_PATH = "common-comment"

    @JvmStatic fun getCrmEntityCommentAttachmentPath(accountId: Int?, type: String, typeId: String, commentId: Int?): String {
        return String.format("%d/crm/.attachments/%s/%s/%s/%d", accountId, type.toLowerCase(), typeId, COMMENT_PATH, commentId)
    }

    @JvmStatic fun getProjectEntityAttachmentPath(accountId: Int?, projectId: Int?, type: String, typeId: String): String {
        return String.format("%d/project/%d/.attachments/%s/%s", accountId, projectId, type.toLowerCase(), typeId)
    }

    @JvmStatic fun getProjectEntityCommentAttachmentPath(accountId: Int?, projectId: Int?, type: String, typeId: String, commentId: Int?): String {
        return String.format("%d/project/%d/.attachments/%s/%s/%s/%d",
                accountId, projectId, type.toLowerCase(), typeId, COMMENT_PATH, commentId)
    }

    @JvmStatic fun getCommentAttachmentPath(type: String, accountId: Int?, extraTypeId: Int?, typeId: String, commentId: Int): String {
        return if (ProjectTypeConstants.BUG == type || ProjectTypeConstants.MESSAGE == type ||
                ProjectTypeConstants.MILESTONE == type
                || ProjectTypeConstants.RISK == type || ProjectTypeConstants.TASK == type
                || ProjectTypeConstants.PAGE == type
                || ProjectTypeConstants.BUG_COMPONENT == type || ProjectTypeConstants.BUG_VERSION == type
                || ProjectTypeConstants.INVOICE == type) {
            getProjectEntityCommentAttachmentPath(accountId, extraTypeId, type, typeId, commentId)
        } else if (CrmTypeConstants.ACCOUNT == type || CrmTypeConstants.CONTACT == type ||
                CrmTypeConstants.CAMPAIGN == type || CrmTypeConstants.LEAD == type ||
                CrmTypeConstants.OPPORTUNITY == type || CrmTypeConstants.CASE == type ||
                CrmTypeConstants.CALL == type || CrmTypeConstants.MEETING == type ||
                CrmTypeConstants.TASK == type) {
            getCrmEntityCommentAttachmentPath(accountId, type, typeId, commentId)
        } else {
            throw MyCollabException("Do not support comment attachment path " + type)
        }
    }
}

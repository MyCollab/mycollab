/**
 * Copyright © MyCollab
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.file

import com.mycollab.core.MyCollabException
import com.mycollab.module.project.ProjectTypeConstants

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
object AttachmentUtils {
    private val COMMENT_PATH = "common-comment"

    @JvmStatic fun getProjectEntityAttachmentPath(accountId: Int?, projectId: Int?, type: String, typeId: String): String =
            String.format("%d/project/%d/.attachments/%s/%s", accountId, projectId, type.toLowerCase(), typeId)

    @JvmStatic fun getProjectEntityCommentAttachmentPath(accountId: Int?, projectId: Int?, type: String, typeId: String, commentId: Int?): String =
            String.format("%d/project/%d/.attachments/%s/%s/%s/%d",
                    accountId, projectId, type.toLowerCase(), typeId, COMMENT_PATH, commentId)

    @JvmStatic fun getCommentAttachmentPath(type: String, accountId: Int?, extraTypeId: Int?, typeId: String, commentId: Int): String =
            if (ProjectTypeConstants.BUG == type || ProjectTypeConstants.MESSAGE == type ||
                    ProjectTypeConstants.MILESTONE == type
                    || ProjectTypeConstants.RISK == type || ProjectTypeConstants.TASK == type
                    || ProjectTypeConstants.PAGE == type
                    || ProjectTypeConstants.BUG_COMPONENT == type || ProjectTypeConstants.BUG_VERSION == type
                    || ProjectTypeConstants.INVOICE == type) {
                getProjectEntityCommentAttachmentPath(accountId, extraTypeId, type, typeId, commentId)
            } else {
                throw MyCollabException("Do not support comment attachment path $type")
            }
}

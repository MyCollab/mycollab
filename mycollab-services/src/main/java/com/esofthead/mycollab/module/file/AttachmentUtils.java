/**
 * This file is part of mycollab-services.
 *
 * mycollab-services is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-services is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-services.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.module.file;

import com.esofthead.mycollab.core.MyCollabException;
import com.esofthead.mycollab.module.crm.CrmTypeConstants;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class AttachmentUtils {
    private static final String COMMENT_PATH = "common-comment";

    public static String getCrmEntityCommentAttachmentPath(Integer accountId, String type, String typeId, Integer commentId) {
        return String.format("%d/crm/.attachments/%s/%s/%s/%d", accountId, type.toLowerCase(), typeId, COMMENT_PATH, commentId);
    }

    public static String getProjectEntityAttachmentPath(Integer accountId, Integer projectId,
                                                        String type, String typeId) {
        return String.format("%d/project/%d/.attachments/%s/%s", accountId,
                projectId, type.toLowerCase(), typeId);
    }

    public static String getProjectEntityCommentAttachmentPath(Integer accountId, Integer projectId,
                                                               String type, String typeId, Integer commentId) {
        return String.format("%d/project/%d/.attachments/%s/%s/%s/%d",
                accountId, projectId, type.toLowerCase(), typeId, COMMENT_PATH, commentId);
    }

    public static String getCommentAttachmentPath(String type, Integer accountId, Integer extraTypeId,
                                                  String typeId, int commentId) {

        if (ProjectTypeConstants.BUG.equals(type) || ProjectTypeConstants.MESSAGE.equals(type) ||
                ProjectTypeConstants.MILESTONE.equals(type) || ProjectTypeConstants.PROBLEM.equals(type)
                || ProjectTypeConstants.RISK.equals(type) || ProjectTypeConstants.TASK.equals(type)
                || ProjectTypeConstants.PAGE.equals(type)
                || ProjectTypeConstants.BUG_COMPONENT.equals(type) || ProjectTypeConstants.BUG_VERSION.equals(type)) {
            return getProjectEntityCommentAttachmentPath(accountId, extraTypeId, type, typeId, commentId);
        } else if (CrmTypeConstants.ACCOUNT.equals(type) || CrmTypeConstants.CONTACT.equals(type) ||
                CrmTypeConstants.CAMPAIGN.equals(type) || CrmTypeConstants.LEAD.equals(type) ||
                CrmTypeConstants.OPPORTUNITY.equals(type) || CrmTypeConstants.CASE.equals(type) ||
                CrmTypeConstants.CALL.equals(type) || CrmTypeConstants.MEETING.equals(type) ||
                CrmTypeConstants.TASK.equals(type)) {
            return getCrmEntityCommentAttachmentPath(accountId, type, typeId, commentId);
        } else {
            throw new MyCollabException("Do not support comment attachment path " + type);
        }
    }
}

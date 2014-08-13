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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.esofthead.mycollab.common.CommentType;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class AttachmentUtils {
	private static Logger log = LoggerFactory.getLogger(AttachmentUtils.class);

	public static String getCrmNoteAttachmentPath(int accountId, int noteId) {
		return String.format("%d/crm/.attachments/%s/%d", accountId,
				AttachmentType.CRM_NOTE_TYPE, noteId);
	}

	public static String getCrmNoteCommentAttachmentPath(int accountId,
			int noteId, int commentId) {
		return String.format("%d/crm/.attachments/%s/%d/%s/%d", accountId,
				AttachmentType.CRM_NOTE_TYPE, noteId,
				AttachmentType.COMMON_COMMENT, commentId);
	}

	public static String getProjectMessageAttachmentPath(int accountId,
			int projectId, int messageId) {
		return String.format("%d/project/%d/.attachments/%s/%d", accountId,
				projectId, AttachmentType.PROJECT_MESSAGE, messageId);
	}

	public static String getProjectMessageCommentAttachmentPath(int accountId,
			int projectId, int messageId, int commentId) {
		return String.format("%d/project/%d/.attachments/%s/%d/%s/%d",
				accountId, projectId, AttachmentType.PROJECT_MESSAGE,
				messageId, AttachmentType.COMMON_COMMENT, commentId);
	}

	public static String getProjectEntityAttachmentPath(int accountId,
			int projectId, AttachmentType type, int typeid) {
		return String.format("%d/project/%d/.attachments/%s/%d", accountId,
				projectId, type.toString(), typeid);
	}

	public static String getProjectBugAttachmentPath(int accountId,
			int projectId, int bugId) {
		return String.format("%d/project/%d/.attachments/%s/%d", accountId,
				projectId, AttachmentType.PROJECT_BUG_TYPE, bugId);
	}

	public static String getProjectBugCommentAttachmentPath(int accountId,
			int projectId, int bugId, int commentId) {
		return String.format("%d/project/%d/.attachments/%s/%d/%s/%d",
				accountId, projectId, AttachmentType.PROJECT_BUG_TYPE, bugId,
				AttachmentType.COMMON_COMMENT, commentId);
	}

	public static String getProjectMilestoneAttachmentPath(int accountId,
			int projectId, int milestoneId) {
		return String.format("%d/project/%d/.attachments/%s/%d", accountId,
				projectId, AttachmentType.PROJECT_MILESTONE, milestoneId);
	}

	public static String getProjectMilestoneCommentAttachmentPath(
			int accountId, int projectId, int milestoneId, int commentId) {
		return String.format("%d/project/%d/.attachments/%s/%d/%s/%d",
				accountId, projectId, AttachmentType.PROJECT_MILESTONE,
				milestoneId, AttachmentType.COMMON_COMMENT, commentId);
	}

	public static String getProjectTaskListCommentAttachmentPath(int accountId,
			int projectId, int tasklistId, int commentId) {
		return String.format("%d/project/%d/.attachments/%s/%d/%s/%d",
				accountId, projectId, AttachmentType.PROJECT_TASKLIST,
				tasklistId, AttachmentType.COMMON_COMMENT, commentId);
	}

	public static String getProjectPageCommentAttachmentPath(int accountId,
			int projectId, String pageId, int commentId) {
		return String.format("%d/project/%d/.attachments/%s/%s/%s/%d",
				accountId, projectId, AttachmentType.PROJECT_PAGE, pageId,
				AttachmentType.COMMON_COMMENT, commentId);
	}

	public static String getProjectTaskAttachmentPath(int accountId,
			int projectId, int taskId) {
		return String.format("%d/project/%d/.attachments/%s/%d", accountId,
				projectId, AttachmentType.PROJECT_TASK_TYPE, taskId);
	}

	public static String getProjectTaskCommentAttachmentPath(int accountId,
			int projectId, int taskId, int commentId) {
		return String.format("%d/project/%d/.attachments/%s/%d/%s/%d",
				accountId, projectId, AttachmentType.PROJECT_TASK_TYPE, taskId,
				AttachmentType.COMMON_COMMENT, commentId);
	}

	public static String getProjectRiskCommentAttachmentPath(int accountId,
			int projectId, int riskId, int commentId) {
		return String.format("%d/project/%d/.attachments/%s/%d/%s/%d",
				accountId, projectId, AttachmentType.PROJECT_RISK, riskId,
				AttachmentType.COMMON_COMMENT, commentId);
	}

	public static String getProjectProblemCommentAttachmentPath(int accountId,
			int projectId, int problemId, int commentId) {
		return String.format("%d/project/%d/.attachments/%s/%d/%s/%d",
				accountId, projectId, AttachmentType.PROJECT_PROBLEM,
				problemId, AttachmentType.COMMON_COMMENT, commentId);
	}

	public static String getProjectEntityCommentAttachmentPath(
			CommentType type, int accountId, int projectId, String typeid,
			int commentId) {
		String attachmentPath = "";

		if (CommentType.PRJ_BUG.equals(type)) {
			attachmentPath = AttachmentUtils
					.getProjectBugCommentAttachmentPath(accountId, projectId,
							Integer.parseInt(typeid), commentId);
		} else if (CommentType.PRJ_MESSAGE.equals(type)) {
			attachmentPath = AttachmentUtils
					.getProjectMessageCommentAttachmentPath(accountId,
							projectId, Integer.parseInt(typeid), commentId);
		} else if (CommentType.PRJ_MILESTONE.equals(type)) {
			attachmentPath = AttachmentUtils
					.getProjectMilestoneCommentAttachmentPath(accountId,
							projectId, Integer.parseInt(typeid), commentId);
		} else if (CommentType.PRJ_PROBLEM.equals(type)) {
			attachmentPath = AttachmentUtils
					.getProjectProblemCommentAttachmentPath(accountId,
							projectId, Integer.parseInt(typeid), commentId);
		} else if (CommentType.PRJ_RISK.equals(type)) {
			attachmentPath = AttachmentUtils
					.getProjectRiskCommentAttachmentPath(accountId, projectId,
							Integer.parseInt(typeid), commentId);
		} else if (CommentType.PRJ_TASK.equals(type)) {
			attachmentPath = AttachmentUtils
					.getProjectTaskCommentAttachmentPath(accountId, projectId,
							Integer.parseInt(typeid), commentId);
		} else if (CommentType.PRJ_TASK_LIST.equals(type)) {
			attachmentPath = AttachmentUtils
					.getProjectTaskListCommentAttachmentPath(accountId,
							projectId, Integer.parseInt(typeid), commentId);
		} else if (CommentType.PRJ_PAGE.equals(type)) {
			attachmentPath = AttachmentUtils
					.getProjectPageCommentAttachmentPath(accountId, projectId,
							typeid, commentId);
		} else {
			log.error("Do not support comment attachment path " + type);
		}

		return attachmentPath;

	}
}

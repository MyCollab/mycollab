/**
 * This file is part of mycollab-esb.
 *
 * mycollab-esb is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-esb is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-esb.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.module.project.esb.impl

import com.esofthead.mycollab.common.dao.CommentMapper
import com.esofthead.mycollab.common.domain.CommentExample
import com.esofthead.mycollab.module.ecm.service.ResourceService
import com.esofthead.mycollab.module.file.AttachmentUtils
import com.esofthead.mycollab.module.project.ProjectTypeConstants
import com.esofthead.mycollab.module.project.esb.DeleteProjectMessageCommand
import com.esofthead.mycollab.spring.ApplicationContextUtil
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component object DeleteProjectMessageCommandImpl {
    private val LOG: Logger = LoggerFactory.getLogger(classOf[DeleteProjectMessageCommandImpl])
}

@Component class DeleteProjectMessageCommandImpl extends DeleteProjectMessageCommand {
    def messageRemoved(username: String, accountId: Int, projectId: Int, messageId: Int) {
        DeleteProjectMessageCommandImpl.LOG.debug("Remove message id {} of project {} by user {}", Array(messageId,
            projectId, username))
        removeRelatedFiles(accountId, projectId, messageId)
        removeRelatedComments(messageId)
    }

    private def removeRelatedFiles(accountId: Int, projectId: Int, messageId: Int) {
        DeleteProjectMessageCommandImpl.LOG.debug("Delete files of bug {} in project {}", messageId, projectId)
        val resourceService: ResourceService = ApplicationContextUtil.getSpringBean(classOf[ResourceService])
        val attachmentPath: String = AttachmentUtils.getProjectEntityAttachmentPath(accountId, projectId, ProjectTypeConstants.MESSAGE, "" + messageId)
        resourceService.removeResource(attachmentPath, "", accountId)
    }

    private def removeRelatedComments(messageId: Int) {
        DeleteProjectMessageCommandImpl.LOG.debug("Delete related comments of message id {}", messageId)
        val commentMapper: CommentMapper = ApplicationContextUtil.getSpringBean(classOf[CommentMapper])
        val ex: CommentExample = new CommentExample
        ex.createCriteria.andTypeEqualTo(ProjectTypeConstants.MESSAGE).andExtratypeidEqualTo(messageId)
        commentMapper.deleteByExample(ex)
    }
}
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
import com.esofthead.mycollab.module.GenericCommandHandler
import com.esofthead.mycollab.module.ecm.service.ResourceService
import com.esofthead.mycollab.module.file.AttachmentUtils
import com.esofthead.mycollab.module.project.ProjectTypeConstants
import com.esofthead.mycollab.module.project.esb.DeleteProjectBugEvent
import com.google.common.eventbus.{AllowConcurrentEvents, Subscribe}
import org.slf4j.{Logger, LoggerFactory}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

object DeleteProjectBugCommandImpl {
    private val LOG: Logger = LoggerFactory.getLogger(classOf[DeleteProjectBugCommandImpl])
}

@Component class DeleteProjectBugCommandImpl extends GenericCommandHandler {
    @Autowired private val resourceService: ResourceService = null
    @Autowired private val commentMapper: CommentMapper = null

    @AllowConcurrentEvents
    @Subscribe
    def bugRemoved(event: DeleteProjectBugEvent): Unit = {
        removeRelatedFiles(event.accountId, event.projectId, event.bugId)
        removeRelatedComments(event.bugId)
    }

    private def removeRelatedFiles(accountId: Integer, projectId: Integer, bugId: Integer) {
        DeleteProjectBugCommandImpl.LOG.debug("Delete files of bug {} in project {}", Array(bugId, projectId))
        val attachmentPath: String = AttachmentUtils.getProjectEntityAttachmentPath(accountId, projectId,
            ProjectTypeConstants.BUG, "" + bugId)
        resourceService.removeResource(attachmentPath, "", accountId)
    }

    private def removeRelatedComments(bugId: Integer) {
        DeleteProjectBugCommandImpl.LOG.debug("Delete related comments of bug {}", bugId)
        val ex: CommentExample = new CommentExample
        ex.createCriteria.andTypeEqualTo(ProjectTypeConstants.BUG).andExtratypeidEqualTo(bugId)
        commentMapper.deleteByExample(ex)
    }
}
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
import com.esofthead.mycollab.module.project.esb.DeleteProjectTaskListEvent
import com.google.common.eventbus.{AllowConcurrentEvents, Subscribe}
import org.slf4j.{Logger, LoggerFactory}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

object DeleteProjectTaskListCommandImpl {
    private val LOG: Logger = LoggerFactory.getLogger(classOf[DeleteProjectTaskListCommandImpl])
}

@Component class DeleteProjectTaskListCommandImpl extends GenericCommandHandler {
    @Autowired private val resourceService: ResourceService = null

    @Autowired private val commentMapper: CommentMapper = null

    @AllowConcurrentEvents
    @Subscribe
    def removedTaskGroup(event: DeleteProjectTaskListEvent): Unit = {
        DeleteProjectTaskListCommandImpl.LOG.debug("Remove task list id {} of project {} by user {}",
            Array(event.taskListId, event.projectId, event.username))
        removeRelatedFiles(event.accountId, event.projectId, event.taskListId)
        removeRelatedComments(event.taskListId)
    }

    private def removeRelatedFiles(accountId: Integer, projectId: Integer, taskListId: Integer) {
        DeleteProjectTaskListCommandImpl.LOG.debug("Delete files of task list id {} in project {}",
            Array(taskListId, projectId))
        val attachmentPath: String = AttachmentUtils.getProjectEntityAttachmentPath(accountId, projectId,
            ProjectTypeConstants.TASK_LIST, "" + taskListId)
        resourceService.removeResource(attachmentPath, "", accountId)
    }

    private def removeRelatedComments(taskListId: Integer) {
        DeleteProjectTaskListCommandImpl.LOG.debug("Delete related comments of task list id {}", taskListId)
        val ex: CommentExample = new CommentExample
        ex.createCriteria.andTypeEqualTo(ProjectTypeConstants.TASK_LIST).andExtratypeidEqualTo(taskListId)
        commentMapper.deleteByExample(ex)
    }
}
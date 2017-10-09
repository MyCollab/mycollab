package com.mycollab.module.project.esb

import com.google.common.eventbus.AllowConcurrentEvents
import com.google.common.eventbus.Subscribe
import com.mycollab.common.dao.CommentMapper
import com.mycollab.common.domain.CommentExample
import com.mycollab.common.domain.TagExample
import com.mycollab.common.service.TagService
import com.mycollab.module.ecm.service.ResourceService
import com.mycollab.module.esb.GenericCommand
import com.mycollab.module.file.AttachmentUtils
import com.mycollab.module.project.ProjectTypeConstants
import com.mycollab.module.project.dao.PredecessorMapper
import com.mycollab.module.project.domain.PredecessorExample
import org.springframework.stereotype.Component

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
@Component
class DeleteProjectTaskCommand(private val resourceService: ResourceService,
                               private val commentMapper: CommentMapper,
                               private val predecessorMapper: PredecessorMapper,
                               private val tagService: TagService) : GenericCommand() {

    @AllowConcurrentEvents
    @Subscribe
    fun removedTask(event: DeleteProjectTaskEvent) {
        event.tasks.forEach { task ->
            removeRelatedFiles(event.accountId, task.projectid, task.id)
            removeRelatedComments(task.id)
            removePredecessorTasks(task.id)
            removeRelatedTags(task.id)
        }
    }

    private fun removeRelatedFiles(accountId: Int, projectId: Int, taskId: Int) {
        val attachmentPath = AttachmentUtils.getProjectEntityAttachmentPath(accountId, projectId,
                ProjectTypeConstants.TASK, "$taskId")
        resourceService.removeResource(attachmentPath, "", true, accountId)
    }

    private fun removeRelatedComments(taskId: Int) {
        val ex = CommentExample()
        ex.createCriteria().andTypeEqualTo(ProjectTypeConstants.TASK).andExtratypeidEqualTo(taskId)
        commentMapper.deleteByExample(ex)
    }

    private fun removePredecessorTasks(taskId: Int) {
        val ex = PredecessorExample()
        ex.or().andSourceidEqualTo(taskId).andSourcetypeEqualTo(ProjectTypeConstants.TASK)
        ex.or().andDescidEqualTo(taskId).andDesctypeEqualTo(ProjectTypeConstants.TASK)
        predecessorMapper.deleteByExample(ex)
    }

    private fun removeRelatedTags(taskId: Int) {
        val ex = TagExample()
        ex.createCriteria().andTypeEqualTo(ProjectTypeConstants.TASK).andTypeidEqualTo("$taskId")
        tagService.deleteByExample(ex)
    }
}
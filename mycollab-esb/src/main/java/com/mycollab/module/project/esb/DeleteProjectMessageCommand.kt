package com.mycollab.module.project.esb

import com.google.common.eventbus.AllowConcurrentEvents
import com.google.common.eventbus.Subscribe
import com.mycollab.common.dao.CommentMapper
import com.mycollab.common.domain.CommentExample
import com.mycollab.module.ecm.service.ResourceService
import com.mycollab.module.esb.GenericCommand
import com.mycollab.module.file.AttachmentUtils
import com.mycollab.module.project.ProjectTypeConstants
import org.springframework.stereotype.Component

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
@Component
class DeleteProjectMessageCommand(private val resourceService: ResourceService,
                                  private val commentMapper: CommentMapper) : GenericCommand() {

    @AllowConcurrentEvents
    @Subscribe
    fun removedMessage(event: DeleteProjectMessageEvent) {
        event.messages.forEach { message ->
            removeRelatedFiles(event.accountId, message.projectid, message.id)
            removeRelatedComments(message.id)
        }
    }

    private fun removeRelatedFiles(accountId: Int, projectId: Int, messageId: Int) {
        val attachmentPath = AttachmentUtils.getProjectEntityAttachmentPath(accountId, projectId,
                ProjectTypeConstants.MESSAGE, "" + messageId)
        resourceService.removeResource(attachmentPath, "", true, accountId)
    }

    private fun removeRelatedComments(messageId: Int) {
        val ex = CommentExample()
        ex.createCriteria().andTypeEqualTo(ProjectTypeConstants.MESSAGE).andExtratypeidEqualTo(messageId)
        commentMapper.deleteByExample(ex)
    }
}
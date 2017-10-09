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
class DeleteProjectVersionCommand(private val resourceService: ResourceService,
                                  private val commentMapper: CommentMapper) : GenericCommand() {

    @AllowConcurrentEvents
    @Subscribe
    fun removedVersion(event: DeleteProjectVersionEvent) {
        removeRelatedFiles(event.accountId, event.projectId, event.versionId)
        removeRelatedComments(event.versionId)
    }

    private fun removeRelatedFiles(accountId: Int, projectId: Int, versionId: Int) {
        val attachmentPath = AttachmentUtils.getProjectEntityAttachmentPath(accountId, projectId,
                ProjectTypeConstants.BUG_VERSION, "$versionId")
        resourceService.removeResource(attachmentPath, "", true, accountId)
    }

    private fun removeRelatedComments(bugId: Int) {
        val ex = CommentExample()
        ex.createCriteria().andTypeEqualTo(ProjectTypeConstants.BUG_VERSION).andExtratypeidEqualTo(bugId)
        commentMapper.deleteByExample(ex)
    }
}
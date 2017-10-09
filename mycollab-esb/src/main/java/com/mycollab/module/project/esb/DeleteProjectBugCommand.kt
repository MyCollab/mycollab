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
import org.springframework.stereotype.Component

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
@Component
class DeleteProjectBugCommand(private val resourceService: ResourceService,
                              private val commentMapper: CommentMapper,
                              private val tagService: TagService) : GenericCommand() {

    @AllowConcurrentEvents
    @Subscribe
    fun removeBugs(event: DeleteProjectBugEvent) {
        event.bugs.forEach {
            removeRelatedFiles(event.accountId, it.projectid, it.id)
            removeRelatedComments(it.id)
            removeRelatedTags(it.id)
        }
    }

    private fun removeRelatedFiles(accountId: Int, projectId: Int, bugId: Int) {
        val attachmentPath = AttachmentUtils.getProjectEntityAttachmentPath(accountId, projectId,
                ProjectTypeConstants.BUG, "" + bugId)
        resourceService.removeResource(attachmentPath, "", true, accountId)
    }

    private fun removeRelatedComments(bugId: Int) {
        val ex = CommentExample()
        ex.createCriteria().andTypeEqualTo(ProjectTypeConstants.BUG).andExtratypeidEqualTo(bugId)
        commentMapper.deleteByExample(ex)
    }

    private fun removeRelatedTags(bugId: Int) {
        val ex = TagExample()
        ex.createCriteria().andTypeEqualTo(ProjectTypeConstants.BUG).andTypeidEqualTo("$bugId")
        tagService.deleteByExample(ex)
    }
}
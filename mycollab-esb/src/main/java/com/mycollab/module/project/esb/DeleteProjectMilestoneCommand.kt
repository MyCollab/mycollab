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
class DeleteProjectMilestoneCommand(private val resourceService: ResourceService,
                                    private val commentMapper: CommentMapper,
                                    private val predecessorMapper: PredecessorMapper,
                                    private val tagService: TagService) : GenericCommand() {

    @AllowConcurrentEvents
    @Subscribe
    fun removedMilestone(event: DeleteProjectMilestoneEvent) {
        removeRelatedFiles(event.accountId, event.projectId, event.milestoneId)
        removeRelatedComments(event.milestoneId)
        removePredecessorMilestones(event.milestoneId)
        removeRelatedTags(event.milestoneId)
    }

    private fun removeRelatedFiles(accountId: Int, projectId: Int, milestoneId: Int) {
        val attachmentPath = AttachmentUtils.getProjectEntityAttachmentPath(accountId, projectId,
                ProjectTypeConstants.MILESTONE, "" + milestoneId)
        resourceService.removeResource(attachmentPath, "", true, accountId)
    }

    private fun removeRelatedComments(milestoneId: Int) {
        val ex = CommentExample()
        ex.createCriteria().andTypeEqualTo(ProjectTypeConstants.MILESTONE).andExtratypeidEqualTo(milestoneId)
        commentMapper.deleteByExample(ex)
    }

    private fun removePredecessorMilestones(milestoneId: Int) {
        val ex = PredecessorExample()
        ex.or().andSourceidEqualTo(milestoneId).andSourcetypeEqualTo(ProjectTypeConstants.MILESTONE)
        ex.or().andDescidEqualTo(milestoneId).andDesctypeEqualTo(ProjectTypeConstants.MILESTONE)
        predecessorMapper.deleteByExample(ex)
    }

    private fun removeRelatedTags(milestoneId: Int) {
        val ex = TagExample()
        ex.createCriteria().andTypeEqualTo(ProjectTypeConstants.MILESTONE).andTypeidEqualTo( "$milestoneId")
        tagService.deleteByExample(ex)
    }
}
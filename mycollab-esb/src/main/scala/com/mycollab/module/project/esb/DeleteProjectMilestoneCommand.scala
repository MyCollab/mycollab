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
package com.mycollab.module.project.esb

import com.google.common.eventbus.{AllowConcurrentEvents, Subscribe}
import com.mycollab.common.dao.CommentMapper
import com.mycollab.common.domain.{CommentExample, TagExample}
import com.mycollab.common.service.TagService
import com.mycollab.module.ecm.service.ResourceService
import com.mycollab.module.esb.GenericCommand
import com.mycollab.module.file.AttachmentUtils
import com.mycollab.module.project.ProjectTypeConstants
import com.mycollab.module.project.dao.PredecessorMapper
import com.mycollab.module.project.domain.PredecessorExample
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
  * @author MyCollab Ltd.
  * @since 1.0
  */
@Component class DeleteProjectMilestoneCommand extends GenericCommand {
  @Autowired private val resourceService: ResourceService = null
  @Autowired private val commentMapper: CommentMapper = null
  @Autowired private val predecessorMapper: PredecessorMapper = null
  @Autowired private val tagService: TagService = null

  @AllowConcurrentEvents
  @Subscribe
  def removedMilestone(event: DeleteProjectMilestoneEvent): Unit = {
    removeRelatedFiles(event.accountId, event.projectId, event.milestoneId)
    removeRelatedComments(event.milestoneId)
    removePredecessorMilestones(event.milestoneId)
    removeRelatedTags(event.milestoneId)
  }

  private def removeRelatedFiles(accountId: Integer, projectId: Integer, milestoneId: Integer) {
    val attachmentPath = AttachmentUtils.getProjectEntityAttachmentPath(accountId, projectId,
      ProjectTypeConstants.MILESTONE, "" + milestoneId)
    resourceService.removeResource(attachmentPath, "", accountId)
  }

  private def removeRelatedComments(milestoneId: Integer) {
    val ex = new CommentExample
    ex.createCriteria.andTypeEqualTo(ProjectTypeConstants.MILESTONE).andExtratypeidEqualTo(milestoneId)
    commentMapper.deleteByExample(ex)
  }

  private def removePredecessorMilestones(milestoneId: Integer): Unit = {
    val ex = new PredecessorExample
    ex.or().andSourceidEqualTo(milestoneId).andSourcetypeEqualTo(ProjectTypeConstants.MILESTONE)
    ex.or().andDescidEqualTo(milestoneId).andDesctypeEqualTo(ProjectTypeConstants.MILESTONE)
    predecessorMapper.deleteByExample(ex);
  }

  private def removeRelatedTags(milestoneId: Integer): Unit = {
    val ex = new TagExample
    ex.createCriteria().andTypeEqualTo(ProjectTypeConstants.MILESTONE).andTypeidEqualTo(milestoneId + "")
    tagService.deleteByExample(ex)
  }
}
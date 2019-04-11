/**
 * Copyright © MyCollab
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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
import com.mycollab.module.project.dao.TicketKeyMapper
import com.mycollab.module.project.domain.TicketKeyExample
import org.springframework.stereotype.Component

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
@Component
class DeleteProjectRiskCommand(private val resourceService: ResourceService,
                               private val commentMapper: CommentMapper,
                               private val tagService: TagService,
                               private val ticketKeyMapper: TicketKeyMapper) : GenericCommand() {

    @AllowConcurrentEvents
    @Subscribe
    fun removeRisk(event: DeleteProjectRiskEvent) {
        val riskIds = event.risks.map { it.id }.toCollection(mutableListOf())
        event.risks.forEach {
            removeRelatedFiles(event.accountId, it.projectid, it.id)
            removeRelatedTags(it.id)
        }
        removeRelatedComments(riskIds)
        removeTicketKeys(riskIds)
    }

    private fun removeRelatedFiles(accountId: Int, projectId: Int, riskId: Int) {
        val attachmentPath = AttachmentUtils.getProjectEntityAttachmentPath(accountId, projectId,
                ProjectTypeConstants.RISK, "$riskId")
        resourceService.removeResource(attachmentPath, "", true, accountId)
    }

    private fun removeRelatedComments(riskIds: MutableList<Int>) {
        val ex = CommentExample()
        ex.createCriteria().andTypeEqualTo(ProjectTypeConstants.RISK).andExtratypeidIn(riskIds)
        commentMapper.deleteByExample(ex)
    }

    private fun removeTicketKeys(riskIds: MutableList<Int>) {
        val ex = TicketKeyExample()
        ex.createCriteria().andTicketidIn(riskIds).andTickettypeEqualTo(ProjectTypeConstants.RISK)
        ticketKeyMapper.deleteByExample(ex)
    }

    private fun removeRelatedTags(riskId: Int) {
        val ex = TagExample()
        ex.createCriteria().andTypeEqualTo(ProjectTypeConstants.RISK).andTypeidEqualTo("$riskId")
        tagService.deleteByExample(ex)
    }
}
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
import com.mycollab.common.ModuleNameConstants
import com.mycollab.common.dao.ActivityStreamMapper
import com.mycollab.common.dao.CommentMapper
import com.mycollab.common.dao.OptionValMapper
import com.mycollab.common.domain.ActivityStreamExample
import com.mycollab.common.domain.CommentExample
import com.mycollab.common.domain.OptionValExample
import com.mycollab.module.ecm.service.ResourceService
import com.mycollab.module.esb.GenericCommand
import com.mycollab.module.page.service.PageService
import org.springframework.stereotype.Component

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
@Component
class DeleteProjectCommand(private val activityStreamMapper: ActivityStreamMapper,
                           private val commentMapper: CommentMapper,
                           private val resourceService: ResourceService,
                           private val pageService: PageService,
                           private val optionValMapper: OptionValMapper) : GenericCommand() {

    @AllowConcurrentEvents
    @Subscribe
    fun removedProject(event: DeleteProjectEvent) {
        event.projects.forEach { project ->
            deleteProjectActivityStream(project.id)
            deleteRelatedComments(project.id)
            deleteProjectFiles(event.accountId, project.id)
            deleteProjectPages(event.accountId, project.id)
            deleteProjectOptions(project.id)
        }
    }

    private fun deleteProjectActivityStream(projectId: Int) {
        val ex = ActivityStreamExample()
        ex.createCriteria().andExtratypeidEqualTo(projectId).andModuleEqualTo(ModuleNameConstants.PRJ)
        activityStreamMapper.deleteByExample(ex)
    }

    private fun deleteRelatedComments(projectId: Int) {
        val ex = CommentExample()
        ex.createCriteria().andExtratypeidEqualTo(projectId)
        commentMapper.deleteByExample(ex)
    }

    private fun deleteProjectFiles(accountId: Int, projectId: Int) {
        val rootPath = String.format("%d/project/%d", accountId, projectId)
        resourceService.removeResource(rootPath, "", true, accountId)
    }

    private fun deleteProjectPages(accountId: Int, projectId: Int) {
        val rootPath = String.format("%d/project/%d/.page", accountId, projectId)
        pageService.removeResource(rootPath)
    }

    private fun deleteProjectOptions(projectId: Int) {
        val ex = OptionValExample()
        ex.createCriteria().andExtraidEqualTo(projectId)
        optionValMapper.deleteByExample(ex)
    }
}
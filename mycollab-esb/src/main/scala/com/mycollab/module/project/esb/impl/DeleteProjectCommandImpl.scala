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
package com.mycollab.module.project.esb.impl

import com.mycollab.common.dao.CommentMapper
import com.mycollab.common.domain.CommentExample
import com.mycollab.module.page.service.PageService
import com.mycollab.module.project.esb.DeleteProjectEvent
import com.google.common.eventbus.{AllowConcurrentEvents, Subscribe}
import com.mycollab.common.ModuleNameConstants
import com.mycollab.common.dao.{ActivityStreamMapper, CommentMapper, OptionValMapper}
import com.mycollab.common.domain.{ActivityStreamExample, CommentExample, OptionValExample}
import com.mycollab.module.ecm.service.ResourceService
import com.mycollab.module.esb.GenericCommand
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
  * @author MyCollab Ltd.
  * @since 1.0
  */
@Component class DeleteProjectCommandImpl extends GenericCommand {
  @Autowired private val activityStreamMapper: ActivityStreamMapper = null
  @Autowired private val commentMapper: CommentMapper = null
  @Autowired private val resourceService: ResourceService = null
  @Autowired private val pageService: PageService = null
  @Autowired private val optionValMapper: OptionValMapper = null

  @AllowConcurrentEvents
  @Subscribe
  def removedProject(event: DeleteProjectEvent): Unit = {
    for (project <- event.projects) {
      deleteProjectActivityStream(project.getId)
      deleteRelatedComments(project.getId)
      deleteProjectFiles(event.accountId, project.getId)
      deleteProjectPages(event.accountId, project.getId)
      deleteProjectOptions(event.accountId, project.getId);
    }
  }

  private def deleteProjectActivityStream(projectId: Integer) {
    val ex = new ActivityStreamExample
    ex.createCriteria.andExtratypeidEqualTo(projectId).andModuleEqualTo(ModuleNameConstants.PRJ)
    activityStreamMapper.deleteByExample(ex)
  }

  private def deleteRelatedComments(projectId: Integer) {
    val ex = new CommentExample
    ex.createCriteria.andExtratypeidEqualTo(projectId)
    commentMapper.deleteByExample(ex)
  }

  private def deleteProjectFiles(accountid: Integer, projectId: Integer) {
    val rootPath = String.format("%d/project/%d", accountid, projectId)
    resourceService.removeResource(rootPath, "", accountid)
  }

  private def deleteProjectPages(accountid: Integer, projectId: Integer) {
    val rootPath = String.format("%d/project/%d/.page", accountid, projectId)
    pageService.removeResource(rootPath)
  }

  private def deleteProjectOptions(accountid: Integer, projectId: Integer): Unit = {
    val ex = new OptionValExample
    ex.createCriteria().andExtraidEqualTo(projectId)
    optionValMapper.deleteByExample(ex)
  }
}
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

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import com.esofthead.mycollab.common.ModuleNameConstants
import com.esofthead.mycollab.common.dao.ActivityStreamMapper
import com.esofthead.mycollab.common.dao.CommentMapper
import com.esofthead.mycollab.common.domain.ActivityStreamExample
import com.esofthead.mycollab.common.domain.CommentExample
import com.esofthead.mycollab.module.ecm.service.ResourceService
import com.esofthead.mycollab.module.page.service.PageService
import com.esofthead.mycollab.module.project.esb.DeleteProjectCommand
import com.esofthead.mycollab.spring.ApplicationContextUtil

@Component object DeleteProjectCommandImpl {
    private val LOG: Logger = LoggerFactory.getLogger(classOf[DeleteProjectCommandImpl])
}

@Component class DeleteProjectCommandImpl extends DeleteProjectCommand {
    def projectRemoved(accountId: Integer, projectId: Integer) {
        DeleteProjectCommandImpl.LOG.debug("Remove project {}", projectId)
        deleteProjectActivityStream(projectId)
        deleteRelatedComments(projectId)
        deleteProjectFiles(accountId, projectId)
        deleteProjectPages(accountId, projectId)
    }

    private def deleteProjectActivityStream(projectId: Integer) {
        DeleteProjectCommandImpl.LOG.debug("Delete activity stream of project {}", projectId)
        val activityStreamMapper: ActivityStreamMapper = ApplicationContextUtil.getSpringBean(classOf[ActivityStreamMapper])
        val ex: ActivityStreamExample = new ActivityStreamExample
        ex.createCriteria.andExtratypeidEqualTo(projectId).andModuleEqualTo(ModuleNameConstants.PRJ)
        activityStreamMapper.deleteByExample(ex)
    }

    private def deleteRelatedComments(projectId: Integer) {
        DeleteProjectCommandImpl.LOG.debug("Delete related comments")
        val commentMapper: CommentMapper = ApplicationContextUtil.getSpringBean(classOf[CommentMapper])
        val ex: CommentExample = new CommentExample
        ex.createCriteria.andExtratypeidEqualTo(projectId)
        commentMapper.deleteByExample(ex)
    }

    private def deleteProjectFiles(accountid: Integer, projectId: Integer) {
        DeleteProjectCommandImpl.LOG.debug("Delete files of project {}", projectId)
        val resourceService: ResourceService = ApplicationContextUtil.getSpringBean(classOf[ResourceService])
        val rootPath: String = String.format("%d/project/%d", accountid, projectId)
        resourceService.removeResource(rootPath, "", accountid)
    }

    private def deleteProjectPages(accountid: Integer, projectId: Integer) {
        DeleteProjectCommandImpl.LOG.debug("Delete pages of project")
        val wikiService: PageService = ApplicationContextUtil.getSpringBean(classOf[PageService])
        val rootPath: String = String.format("%d/project/%d/.page", accountid, projectId)
        wikiService.removeResource(rootPath)
    }
}
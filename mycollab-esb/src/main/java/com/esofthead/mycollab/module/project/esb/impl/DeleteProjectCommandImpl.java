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
package com.esofthead.mycollab.module.project.esb.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.esofthead.mycollab.common.ModuleNameConstants;
import com.esofthead.mycollab.common.dao.ActivityStreamMapper;
import com.esofthead.mycollab.common.dao.CommentMapper;
import com.esofthead.mycollab.common.domain.ActivityStreamExample;
import com.esofthead.mycollab.common.domain.CommentExample;
import com.esofthead.mycollab.module.ecm.service.ResourceService;
import com.esofthead.mycollab.module.project.esb.DeleteProjectCommand;
import com.esofthead.mycollab.module.wiki.service.WikiService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;

@Component
public class DeleteProjectCommandImpl implements DeleteProjectCommand {

	private static Logger log = LoggerFactory
			.getLogger(DeleteProjectCommandImpl.class);

	@Override
	public void projectRemoved(int accountId, int projectId) {
		log.debug("Remove project {}", projectId);

		deleteProjectActivityStream(projectId);
		deleteRelatedComments(projectId);
		deleteProjectFiles(accountId, projectId);
		deleteProjectPages(accountId, projectId);
	}

	private void deleteProjectActivityStream(int projectId) {
		log.debug("Delete activity stream of project {}", projectId);

		ActivityStreamMapper activityStreamMapper = ApplicationContextUtil
				.getSpringBean(ActivityStreamMapper.class);
		ActivityStreamExample ex = new ActivityStreamExample();
		ex.createCriteria().andExtratypeidEqualTo(projectId)
				.andModuleEqualTo(ModuleNameConstants.PRJ);
		activityStreamMapper.deleteByExample(ex);

	}

	private void deleteRelatedComments(int projectId) {
		log.debug("Delete related comments");
		CommentMapper commentMapper = ApplicationContextUtil
				.getSpringBean(CommentMapper.class);

		CommentExample ex = new CommentExample();
		ex.createCriteria().andExtratypeidEqualTo(projectId);
		commentMapper.deleteByExample(ex);
	}

	private void deleteProjectFiles(int accountid, int projectId) {
		log.debug("Delete files of project {}", projectId);

		ResourceService resourceService = ApplicationContextUtil
				.getSpringBean(ResourceService.class);

		String rootPath = String.format("%d/project/%d", accountid, projectId);
		resourceService.removeResource(rootPath, "", accountid);
	}

	private void deleteProjectPages(int accountid, int projectId) {
		log.debug("Delete pages of project");
		WikiService wikiService = ApplicationContextUtil
				.getSpringBean(WikiService.class);
		String rootPath = String.format("%d/project/%d/.page", accountid,
				projectId);
		wikiService.removeResource(rootPath);
	}

}

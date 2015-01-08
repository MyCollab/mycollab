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

import com.esofthead.mycollab.common.CommentType;
import com.esofthead.mycollab.common.dao.CommentMapper;
import com.esofthead.mycollab.common.domain.CommentExample;
import com.esofthead.mycollab.module.ecm.service.ResourceService;
import com.esofthead.mycollab.module.file.AttachmentUtils;
import com.esofthead.mycollab.module.project.esb.DeleteProjectMilestoneCommand;
import com.esofthead.mycollab.spring.ApplicationContextUtil;

@Component
public class DeleteProjectMilestoneCommandImpl implements
		DeleteProjectMilestoneCommand {

	private static final Logger LOG = LoggerFactory
			.getLogger(DeleteProjectMilestoneCommandImpl.class);

	@Override
	public void milestoneRemoved(String username, int accountId, int projectId,
			int milestoneId) {
		LOG.debug("Remove milestone id {} of project {} by user {}",
				milestoneId, projectId, username);

		removeRelatedFiles(accountId, projectId, milestoneId);
		removeRelatedComments(milestoneId);

	}

	private void removeRelatedFiles(int accountId, int projectId,
			int milestoneId) {
		LOG.debug("Delete files of bug {} in project {}", milestoneId,
				projectId);

		ResourceService resourceService = ApplicationContextUtil
				.getSpringBean(ResourceService.class);
		String attachmentPath = AttachmentUtils
				.getProjectMilestoneAttachmentPath(accountId, projectId,
						milestoneId);
		resourceService.removeResource(attachmentPath, "", accountId);
	}

	private void removeRelatedComments(int milestoneId) {
		LOG.debug("Delete related comments of milestone id {}", milestoneId);
		CommentMapper commentMapper = ApplicationContextUtil
				.getSpringBean(CommentMapper.class);

		CommentExample ex = new CommentExample();
		ex.createCriteria()
				.andTypeEqualTo(CommentType.PRJ_MILESTONE.toString())
				.andExtratypeidEqualTo(milestoneId);
		commentMapper.deleteByExample(ex);
	}

}

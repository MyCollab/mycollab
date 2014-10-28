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
import com.esofthead.mycollab.module.project.esb.DeleteProjectTaskCommand;
import com.esofthead.mycollab.spring.ApplicationContextUtil;

@Component
public class DeleteProjectTaskCommandImpl implements DeleteProjectTaskCommand {

	private static final Logger LOG = LoggerFactory
			.getLogger(DeleteProjectTaskCommandImpl.class);

	@Override
	public void taskRemoved(String username, int accountId, int projectId,
			int taskId) {
		LOG.debug("Remove task id {} of project {} by user {}", new Object[] {
				taskId, projectId, username });

		removeRelatedFiles(accountId, projectId, taskId);
		removeRelatedComments(taskId);

	}

	private void removeRelatedFiles(int accountId, int projectId, int taskId) {
		LOG.debug("Delete files of task id {} in project {}", taskId, projectId);

		ResourceService resourceService = ApplicationContextUtil
				.getSpringBean(ResourceService.class);
		String attachmentPath = AttachmentUtils.getProjectTaskAttachmentPath(
				accountId, projectId, taskId);
		resourceService.removeResource(attachmentPath, "", accountId);
	}

	private void removeRelatedComments(int taskId) {
		LOG.debug("Delete related comments of task id {}", taskId);
		CommentMapper commentMapper = ApplicationContextUtil
				.getSpringBean(CommentMapper.class);

		CommentExample ex = new CommentExample();
		ex.createCriteria().andTypeEqualTo(CommentType.PRJ_TASK.toString())
				.andExtratypeidEqualTo(taskId);
		commentMapper.deleteByExample(ex);
	}

}

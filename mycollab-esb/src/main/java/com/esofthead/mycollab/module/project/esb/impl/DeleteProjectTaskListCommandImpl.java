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
import com.esofthead.mycollab.module.project.esb.DeleteProjectTaskListCommand;
import com.esofthead.mycollab.spring.ApplicationContextUtil;

@Component
public class DeleteProjectTaskListCommandImpl implements
		DeleteProjectTaskListCommand {

	private static final Logger LOG = LoggerFactory
			.getLogger(DeleteProjectTaskListCommandImpl.class);

	@Override
	public void taskListRemoved(String username, int accountId, int projectId,
			int taskListId) {
		LOG.debug("Remove task list id {} of project {} by user {}",
				taskListId, projectId, username);

		removeRelatedFiles(accountId, projectId, taskListId);
		removeRelatedComments(taskListId);

	}

	private void removeRelatedFiles(int accountId, int projectId, int taskListId) {
		LOG.debug("Delete files of task list id {} in project {}", taskListId,
				projectId);

		ResourceService resourceService = ApplicationContextUtil
				.getSpringBean(ResourceService.class);
		String attachmentPath = AttachmentUtils.getProjectTaskAttachmentPath(
				accountId, projectId, taskListId);
		resourceService.removeResource(attachmentPath, "", accountId);
	}

	private void removeRelatedComments(int taskListId) {
		LOG.debug("Delete related comments of task list id {}", taskListId);
		CommentMapper commentMapper = ApplicationContextUtil
				.getSpringBean(CommentMapper.class);

		CommentExample ex = new CommentExample();
		ex.createCriteria()
				.andTypeEqualTo(CommentType.PRJ_TASK_LIST.toString())
				.andExtratypeidEqualTo(taskListId);
		commentMapper.deleteByExample(ex);
	}

}

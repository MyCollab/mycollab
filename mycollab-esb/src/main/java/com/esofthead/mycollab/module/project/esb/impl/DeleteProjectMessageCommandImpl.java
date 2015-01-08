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
import com.esofthead.mycollab.module.project.esb.DeleteProjectMessageCommand;
import com.esofthead.mycollab.spring.ApplicationContextUtil;

@Component
public class DeleteProjectMessageCommandImpl implements
		DeleteProjectMessageCommand {

	private static final Logger LOG = LoggerFactory
			.getLogger(DeleteProjectMessageCommandImpl.class);

	@Override
	public void messageRemoved(String username, int accountId, int projectId,
			int messageId) {
		LOG.debug("Remove message id {} of project {} by user {}",
				messageId, projectId, username);

		removeRelatedFiles(accountId, projectId, messageId);
		removeRelatedComments(messageId);
	}

	private void removeRelatedFiles(int accountId, int projectId, int messageId) {
		LOG.debug("Delete files of bug {} in project {}", messageId, projectId);

		ResourceService resourceService = ApplicationContextUtil
				.getSpringBean(ResourceService.class);
		String attachmentPath = AttachmentUtils
				.getProjectMessageAttachmentPath(accountId, projectId,
						messageId);
		resourceService.removeResource(attachmentPath, "", accountId);
	}

	private void removeRelatedComments(int messageId) {
		LOG.debug("Delete related comments of message id {}", messageId);
		CommentMapper commentMapper = ApplicationContextUtil
				.getSpringBean(CommentMapper.class);

		CommentExample ex = new CommentExample();
		ex.createCriteria().andTypeEqualTo(CommentType.PRJ_MESSAGE.toString())
				.andExtratypeidEqualTo(messageId);
		commentMapper.deleteByExample(ex);
	}

}

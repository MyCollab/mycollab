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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.esofthead.mycollab.common.CommentType;
import com.esofthead.mycollab.common.dao.CommentMapper;
import com.esofthead.mycollab.common.domain.CommentExample;
import com.esofthead.mycollab.core.MyCollabException;
import com.esofthead.mycollab.module.ecm.service.ResourceService;
import com.esofthead.mycollab.module.file.AttachmentUtils;
import com.esofthead.mycollab.module.project.esb.DeleteProjectBugCommand;
import com.esofthead.mycollab.spring.ApplicationContextUtil;

@Component
public class DeleteProjectBugCommandImpl implements DeleteProjectBugCommand {

	private static Logger log = LoggerFactory
			.getLogger(DeleteProjectBugCommandImpl.class);

	@Autowired
	private ResourceService resourceService;

	@Autowired
	private CommentMapper commentMapper;

	@Override
	public void bugRemoved(String username, int accountId, int projectId,
			int bugId) {
		log.debug("Remove bug {} of project {} by user {}", new Object[] {
				bugId, projectId, username });

		removeRelatedFiles(accountId, projectId, bugId);
		removeRelatedComments(bugId);
	}

	private void removeRelatedFiles(int accountId, int projectId, int bugId) {
		log.debug("Delete files of bug {} in project {}", bugId, projectId);

		String attachmentPath = AttachmentUtils.getProjectBugAttachmentPath(
				accountId, projectId, bugId);
		resourceService.removeResource(attachmentPath, "", accountId);
	}

	private void removeRelatedComments(int bugId) {
		log.debug("Delete related comments of bug {}", bugId);

		CommentExample ex = new CommentExample();
		ex.createCriteria().andTypeEqualTo(CommentType.PRJ_BUG.toString())
				.andExtratypeidEqualTo(bugId);
		commentMapper.deleteByExample(ex);
	}

}

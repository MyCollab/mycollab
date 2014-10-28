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

import com.esofthead.mycollab.common.dao.ActivityStreamMapper;
import com.esofthead.mycollab.module.project.esb.DeleteProjectMemberCommand;
import com.esofthead.mycollab.module.user.service.UserService;

@Component
public class DeleteProjectMemberCommandImpl implements
		DeleteProjectMemberCommand {

	private static final Logger LOG = LoggerFactory
			.getLogger(DeleteProjectMemberCommandImpl.class);

	@Autowired
	private ActivityStreamMapper activityStreamMapper;

	@Autowired
	private UserService userService;

	@Override
	public void projectMemberRemoved(String username, Integer projectMemberId,
			Integer projectId, Integer accountId) {
		LOG.debug(
				"Remove project member has username {}, project member id {} and project id {}",
				new Object[] { username, projectMemberId, projectId });

	}

}

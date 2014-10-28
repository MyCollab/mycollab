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
package com.esofthead.mycollab.module.user.esb.impl;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.esofthead.mycollab.cache.CacheUtils;
import com.esofthead.mycollab.module.billing.RegisterStatusConstants;
import com.esofthead.mycollab.module.project.dao.ProjectMemberMapper;
import com.esofthead.mycollab.module.project.domain.ProjectMember;
import com.esofthead.mycollab.module.project.domain.ProjectMemberExample;
import com.esofthead.mycollab.module.project.service.ProjectMemberService;
import com.esofthead.mycollab.module.user.esb.UserRemovedCommand;
import com.esofthead.mycollab.module.user.service.UserService;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
@Component
public class UserRemovedCommandImpl implements UserRemovedCommand {

	private static final Logger LOG = LoggerFactory
			.getLogger(UserRemovedCommandImpl.class);

	@Autowired
	private UserService userService;

	@Autowired
	private ProjectMemberMapper projectMemberMapper;

	@Autowired
	private ProjectMemberService projectMemberService;

	@Override
	public void userRemoved(String username, Integer accountid) {
		LOG.debug("Remove user {} with account id {}", username, accountid);

		LOG.debug(
				"Update status of project member to status 'Delete' if project member associates with deleted user {}",
				username);
		ProjectMemberExample ex = new ProjectMemberExample();
		ex.createCriteria()
				.andStatusIn(
						Arrays.asList(
								RegisterStatusConstants.ACTIVE,
								RegisterStatusConstants.SENT_VERIFICATION_EMAIL,
								RegisterStatusConstants.VERIFICATING))
				.andSaccountidEqualTo(accountid).andUsernameEqualTo(username);
		ProjectMember projectMember = new ProjectMember();
		projectMember.setStatus(RegisterStatusConstants.DELETE);
		projectMemberMapper.updateByExampleSelective(projectMember, ex);

		// Remove cache of project member
		// clean cache of related items
		CacheUtils.cleanCaches(accountid, ProjectMemberService.class);
	}

}

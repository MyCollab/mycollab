/**
 * This file is part of mycollab-services-community.
 *
 * mycollab-services-community is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-services-community is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-services-community.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.module.project.service;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

import com.esofthead.mycollab.configuration.SiteConfiguration;
import com.esofthead.mycollab.module.project.domain.SimpleProjectMember;
import com.esofthead.mycollab.module.user.domain.SimpleUser;
import com.esofthead.mycollab.test.DataSet;
import com.esofthead.mycollab.test.MyCollabClassRunner;
import com.esofthead.mycollab.test.service.ServiceTest;

@RunWith(MyCollabClassRunner.class)
public class ProjectMemberServiceTest extends ServiceTest {
	@Autowired
	private ProjectMemberService projectMemberService;

	@Before
	public void init() {
		SiteConfiguration.loadInstance(8080);
	}

	@DataSet
	@Test
	public void testAcceptProjectMemberAcceptInvitation() {
		projectMemberService.acceptProjectInvitationByNewUser(
				"baohan@esofthead.com", "123", 1, 1, 1);

		SimpleProjectMember member = projectMemberService.findMemberByUsername(
				"baohan@esofthead.com", 1, 1);
		Assert.assertEquals("baohan@esofthead.com", member.getUsername());
	}

	@DataSet
	@Test
	public void testGetActiveMembersInproject() {
		List<SimpleUser> activeUsers = projectMemberService
				.getActiveUsersInProject(1, 1);
		Assert.assertEquals(1, activeUsers.size());

		SimpleUser user = activeUsers.get(0);
		Assert.assertEquals("user1", user.getUsername());
	}

	@DataSet
	@Test
	public void testGetMembersNotInProject() {
		List<SimpleUser> users = projectMemberService
				.getUsersNotInProject(1, 1);
		Assert.assertEquals(2, users.size());
	}

	@DataSet
	@Test
	public void testGetProjectMembersInProjects() {
		List<SimpleUser> users = projectMemberService.getActiveUsersInProjects(
				Arrays.asList(1, 2), 1);
		Assert.assertEquals(3, users.size());
	}
}

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
package com.mycollab.common.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.db.arguments.BasicSearchRequest;
import com.mycollab.module.user.domain.SimpleUser;
import com.mycollab.module.user.domain.criteria.UserSearchCriteria;
import com.mycollab.module.user.service.UserService;
import com.mycollab.test.DataSet;
import com.mycollab.test.service.IntergrationServiceTest;

@RunWith(SpringJUnit4ClassRunner.class)
public class UserServiceTest extends IntergrationServiceTest {
	@Autowired
	protected UserService userService;

	@SuppressWarnings({ "unchecked" })
	@DataSet
	@Test
	public void testGetListUser() {
		UserSearchCriteria criteria = new UserSearchCriteria();
		criteria.setSaccountid(new NumberSearchField(1));
		List<SimpleUser> users = userService
				.findPagableListByCriteria(new BasicSearchRequest<>(criteria, 0, Integer.MAX_VALUE));
		assertThat(users.size()).isEqualTo(4);
		assertThat(users).extracting("username").contains(
				"hainguyen@esofthead.com", "linhduong@esofthead.com",
				"huynguyen@esofthead.com", "test@esofthead.com");
	}

	@DataSet
	@Test
	public void updateUserEmail() {
		SimpleUser user = userService.findUserByUserNameInAccount(
				"hainguyen@esofthead.com", 1);
		assertThat(user.getEmail()).isEqualTo("hainguyen@esofthead.com");

		user.setEmail("hannguyen@esofthead.com");
		userService.updateUserAccount(user, 1);

		SimpleUser anotherUser = userService.findUserByUserNameInAccount(
				"hannguyen@esofthead.com", 1);
		assertThat(anotherUser.getEmail()).isEqualTo("hannguyen@esofthead.com");
		assertThat(anotherUser.getLastname()).isEqualTo("Hai");
	}

	@DataSet
	@Test
	public void testGetTotalActiveUsersInAccount() {
		int totalActiveUsersInAccount = userService.getTotalActiveUsersInAccount(1);
		assertThat(totalActiveUsersInAccount).isEqualTo(3);
	}

	@DataSet
	@Test
	public void testFindUserByUsernameInAccount() {
		SimpleUser user = userService.findUserByUserNameInAccount("hainguyen@esofthead.com", 1);
		assertThat(user.getUsername()).isEqualTo("hainguyen@esofthead.com");
		assertThat(user.getAccountId()).isEqualTo(1);
		assertThat(user.getFirstname()).isEqualTo("Nguyen");
		assertThat(user.getLastname()).isEqualTo("Hai");
	}
}

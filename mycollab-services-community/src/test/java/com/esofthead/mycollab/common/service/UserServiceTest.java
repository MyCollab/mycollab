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
package com.esofthead.mycollab.common.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchRequest;
import com.esofthead.mycollab.module.user.domain.SimpleUser;
import com.esofthead.mycollab.module.user.domain.criteria.UserSearchCriteria;
import com.esofthead.mycollab.module.user.service.UserService;
import com.esofthead.mycollab.test.DataSet;
import com.esofthead.mycollab.test.service.IntergrationServiceTest;

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
				.findPagableListByCriteria(new SearchRequest<UserSearchCriteria>(
						criteria, 0, Integer.MAX_VALUE));
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

	@SuppressWarnings({ "unchecked" })
	@DataSet
	@Test
	public void testGetLoginByDate() throws ParseException {
		GregorianCalendar calendar = new GregorianCalendar(2014, 1, 20);

		Date to = calendar.getTime();
		calendar.add(Calendar.DAY_OF_YEAR, -1);
		Date from = calendar.getTime();
		UserSearchCriteria searchCriteria = new UserSearchCriteria();
		searchCriteria.setLastAccessTimeRange(from, to);
		searchCriteria.setSaccountid(null);
		List<SimpleUser> users = userService
				.findPagableListByCriteria(new SearchRequest<>(
						searchCriteria, 0, Integer.MAX_VALUE));
		assertThat(users.size()).isEqualTo(2);
		assertThat(users).extracting("email").contains(
				"linhduong@esofthead.com", "huynguyen@esofthead.com");
	}

	@DataSet
	@Test
	public void testGetTotalActiveUsersInAccount() {
		int totalActiveUsersInAccount = userService
				.getTotalActiveUsersInAccount(1);
		assertThat(totalActiveUsersInAccount).isEqualTo(3);
	}

	@DataSet
	@Test
	public void testFindUserByUsernameInAccount() {
		SimpleUser user = userService.findUserByUserNameInAccount(
				"hainguyen@esofthead.com", 1);
		assertThat(user.getUsername()).isEqualTo("hainguyen@esofthead.com");
		assertThat(user.getAccountId()).isEqualTo(1);
		assertThat(user.getFirstname()).isEqualTo("Nguyen");
		assertThat(user.getLastname()).isEqualTo("Hai");
	}
}

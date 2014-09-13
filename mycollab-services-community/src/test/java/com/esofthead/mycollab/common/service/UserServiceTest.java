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

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchRequest;
import com.esofthead.mycollab.module.user.domain.SimpleUser;
import com.esofthead.mycollab.module.user.domain.criteria.UserSearchCriteria;
import com.esofthead.mycollab.module.user.service.UserService;
import com.esofthead.mycollab.test.DataSet;
import com.esofthead.mycollab.test.MyCollabClassRunner;
import com.esofthead.mycollab.test.service.ServiceTest;

@RunWith(MyCollabClassRunner.class)
public class UserServiceTest extends ServiceTest {
	@Autowired
	protected UserService userService;

	@SuppressWarnings("rawtypes")
	@DataSet
	@Test
	public void testGetListUser() {
		UserSearchCriteria criteria = new UserSearchCriteria();
		criteria.setSaccountid(new NumberSearchField(1));
		List lstUser = userService
				.findPagableListByCriteria(new SearchRequest<UserSearchCriteria>(
						criteria, 0, Integer.MAX_VALUE));
		Assert.assertEquals(3, lstUser.size());
	}

	@DataSet
	@Test
	public void updateUserEmail() {
		SimpleUser user = userService.findUserByUserNameInAccount(
				"hainguyen@esofthead.com", 1);
		Assert.assertNotNull(user);

		user.setEmail("hannguyen@esofthead.com");
		userService.updateUserAccount(user, 1);

		SimpleUser anotherUser = userService.findUserByUserNameInAccount(
				"hannguyen@esofthead.com", 1);
		Assert.assertNotNull(anotherUser);
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
		List<SimpleUser> lstSimpleUsers = userService
				.findPagableListByCriteria(new SearchRequest<UserSearchCriteria>(
						searchCriteria, 0, Integer.MAX_VALUE));
		Assert.assertEquals(2, lstSimpleUsers.size());
	}

}

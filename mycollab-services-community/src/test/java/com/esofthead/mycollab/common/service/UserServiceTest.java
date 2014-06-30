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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

import com.esofthead.mycollab.core.arguments.DateSearchField;
import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchField;
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

	@DataSet
	@Test
	public void testGetListUser() {
		UserSearchCriteria criteria = new UserSearchCriteria();
		criteria.setSaccountid(new NumberSearchField(1));
		List lstUser = userService
				.findPagableListByCriteria(new SearchRequest<UserSearchCriteria>(
						criteria, 0, Integer.MAX_VALUE));
		Assert.assertEquals(2, lstUser.size());
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

	@DataSet
	@Test
	public void testGetLoginByDate() throws ParseException {
		SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
		Date date = fmt.parse("2014-02-19");
		UserSearchCriteria searchCriteria = new UserSearchCriteria();
		searchCriteria.setSaccountid(null);
		searchCriteria.setLastAccessedTime(new DateSearchField(SearchField.AND,
				date));
		List<SimpleUser> lstSimpleUsers = userService
				.findPagableListByCriteria(new SearchRequest<UserSearchCriteria>(
						searchCriteria, 0, Integer.MAX_VALUE));
		// Assert.assertEquals(2, lstSimpleUsers.size());
		Assert.assertEquals(2, userService.getTotalCount(searchCriteria));
	}

}

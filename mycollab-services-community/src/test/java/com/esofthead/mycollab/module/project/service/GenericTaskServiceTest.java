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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

import com.esofthead.mycollab.core.arguments.DateSearchField;
import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchRequest;
import com.esofthead.mycollab.module.project.domain.ProjectGenericTask;
import com.esofthead.mycollab.module.project.domain.criteria.ProjectGenericTaskSearchCriteria;
import com.esofthead.mycollab.test.DataSet;
import com.esofthead.mycollab.test.MyCollabClassRunner;
import com.esofthead.mycollab.test.service.ServiceTest;

@RunWith(MyCollabClassRunner.class)
public class GenericTaskServiceTest extends ServiceTest {
	@Autowired
	protected ProjectGenericTaskService genericTaskService;

	@DataSet
	@Test
	public void testGenericTaskListFindPageable() {
		List task = genericTaskService
				.findPagableListByCriteria(new SearchRequest<ProjectGenericTaskSearchCriteria>(
						null, 0, Integer.MAX_VALUE));
		Assert.assertEquals(4, task.size());
	}

	@DataSet
	@Test
	public void testCountTaskOverDue() throws ParseException {
		
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			Date d = df.parse("2014-01-23 10:49:49");
			ProjectGenericTaskSearchCriteria criteria = new ProjectGenericTaskSearchCriteria();
			criteria.setDueDate(new DateSearchField(DateSearchField.AND, d));
			criteria.setProjectId(new NumberSearchField(1));
			criteria.setSaccountid(new NumberSearchField(1));
			Assert.assertEquals(2, genericTaskService.getTotalCount(criteria));
	}

	@DataSet
	@Test
	public void testListTaskOverDue() throws ParseException {


			DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			Date d = df.parse("2014-01-23 10:49:49");
			
			ProjectGenericTaskSearchCriteria criteria = new ProjectGenericTaskSearchCriteria();
			criteria.setDueDate(new DateSearchField(DateSearchField.AND, d));
			criteria.setProjectId(new NumberSearchField(1));
			criteria.setSaccountid(new NumberSearchField(1));
			List<ProjectGenericTask> taskList = genericTaskService
					.findPagableListByCriteria(new SearchRequest<ProjectGenericTaskSearchCriteria>(
							criteria, 0, Integer.MAX_VALUE));

			ProjectGenericTask task = taskList.get(0);
			Assert.assertEquals(2, taskList.size());
			
			
			Date d2 = df.parse("2013-01-23 10:49:49");
			Assert.assertEquals(d2, task.getDueDate());

		
	}
}

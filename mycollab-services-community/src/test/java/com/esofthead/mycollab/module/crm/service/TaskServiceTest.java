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
package com.esofthead.mycollab.module.crm.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.BasicSearchRequest;
import com.esofthead.mycollab.module.crm.domain.SimpleTask;
import com.esofthead.mycollab.module.crm.domain.criteria.TodoSearchCriteria;
import com.esofthead.mycollab.test.DataSet;
import com.esofthead.mycollab.test.service.IntergrationServiceTest;

@RunWith(SpringJUnit4ClassRunner.class)
public class TaskServiceTest extends IntergrationServiceTest {

	@Autowired
	protected TaskService taskService;

	@DataSet
	@Test
	public void testSearchByCriteria() {
		List<SimpleTask> tasks = taskService.findPagableListByCriteria(new BasicSearchRequest<>(getCriteria(), 0, Integer.MAX_VALUE));

		assertThat(tasks.size()).isEqualTo(1);
		assertThat(tasks).extracting("id", "status", "subject").contains(tuple(1, "Completed", "aaa"));
	}

	@DataSet
	@Test
	public void testGetTotalCounts() {
		List<SimpleTask> tasks = taskService.findPagableListByCriteria(new BasicSearchRequest<>(getCriteria(), 0, Integer.MAX_VALUE));
		assertThat(tasks.size()).isEqualTo(1);
	}

	private TodoSearchCriteria getCriteria() {
		TodoSearchCriteria criteria = new TodoSearchCriteria();
		criteria.setSaccountid(new NumberSearchField(1));
		return criteria;
	}
}

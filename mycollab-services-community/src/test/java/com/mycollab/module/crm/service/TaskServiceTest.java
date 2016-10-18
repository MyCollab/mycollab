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
package com.mycollab.module.crm.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import java.util.List;

import com.mycollab.module.crm.domain.SimpleCrmTask;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.db.arguments.BasicSearchRequest;
import com.mycollab.module.crm.domain.criteria.CrmTaskSearchCriteria;
import com.mycollab.test.DataSet;
import com.mycollab.test.service.IntegrationServiceTest;

@RunWith(SpringJUnit4ClassRunner.class)
public class TaskServiceTest extends IntegrationServiceTest {

	@Autowired
	private TaskService taskService;

	@DataSet
	@Test
	public void testSearchByCriteria() {
		List<SimpleCrmTask> tasks = taskService.findPageableListByCriteria(new BasicSearchRequest<>(getCriteria()));

		assertThat(tasks.size()).isEqualTo(1);
		assertThat(tasks).extracting("id", "status", "subject").contains(tuple(1, "Completed", "aaa"));
	}

	@DataSet
	@Test
	public void testGetTotalCounts() {
		List<SimpleCrmTask> tasks = taskService.findPageableListByCriteria(new BasicSearchRequest<>(getCriteria()));
		assertThat(tasks.size()).isEqualTo(1);
	}

	private CrmTaskSearchCriteria getCriteria() {
		CrmTaskSearchCriteria criteria = new CrmTaskSearchCriteria();
		criteria.setSaccountid(new NumberSearchField(1));
		return criteria;
	}
}

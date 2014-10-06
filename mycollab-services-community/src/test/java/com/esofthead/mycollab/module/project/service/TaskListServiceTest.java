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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.esofthead.mycollab.common.i18n.OptionI18nEnum.StatusI18nEnum;
import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchRequest;
import com.esofthead.mycollab.core.arguments.StringSearchField;
import com.esofthead.mycollab.module.project.domain.SimpleTask;
import com.esofthead.mycollab.module.project.domain.SimpleTaskList;
import com.esofthead.mycollab.module.project.domain.criteria.TaskListSearchCriteria;
import com.esofthead.mycollab.test.DataSet;
import com.esofthead.mycollab.test.service.IntergrationServiceTest;

@RunWith(SpringJUnit4ClassRunner.class)
public class TaskListServiceTest extends IntergrationServiceTest {

	@Autowired
	private ProjectTaskListService projectTaskListService;

	@SuppressWarnings("unchecked")
	@DataSet
	@Test
	public void testGetArchivedTaskList() {
		TaskListSearchCriteria criteria = new TaskListSearchCriteria();
		criteria.setStatus(new StringSearchField(StatusI18nEnum.Closed.name()));
		criteria.setProjectId(new NumberSearchField(1));
		criteria.setSaccountid(new NumberSearchField(1));

		List<SimpleTaskList> taskLists = (List<SimpleTaskList>) projectTaskListService
				.findPagableListByCriteria(new SearchRequest<TaskListSearchCriteria>(
						criteria));
		assertThat(taskLists.size()).isEqualTo(2);
		assertThat(taskLists).extracting("id", "name").contains(
				tuple(1, "tasklist1"), tuple(3, "tasklist3"));

		List<SimpleTask> subTasks = taskLists.get(0).getSubTasks();
		assertThat(subTasks.size()).isEqualTo(2);
		assertThat(subTasks).extracting("id", "taskname").contains(
				tuple(1, "task1"), tuple(2, "task2"));

		SimpleTaskList taskList3 = taskLists.get(1);
		assertThat(taskList3.getSubTasks().size()).isEqualTo(0);
	}
}

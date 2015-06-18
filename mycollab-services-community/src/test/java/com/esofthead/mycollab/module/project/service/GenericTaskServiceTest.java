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

import com.esofthead.mycollab.core.arguments.*;
import com.esofthead.mycollab.module.project.domain.ProjectGenericTask;
import com.esofthead.mycollab.module.project.domain.criteria.ProjectGenericTaskSearchCriteria;
import com.esofthead.mycollab.test.DataSet;
import com.esofthead.mycollab.test.service.IntergrationServiceTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@RunWith(SpringJUnit4ClassRunner.class)
public class GenericTaskServiceTest extends IntergrationServiceTest {
    @Autowired
    protected ProjectGenericTaskService genericTaskService;

    @SuppressWarnings("unchecked")
    @DataSet
    @Test
    public void testGenericTaskListFindPageable() {
        List<ProjectGenericTask> tasks = genericTaskService
                .findPagableListByCriteria(new SearchRequest<ProjectGenericTaskSearchCriteria>(
                        null, 0, Integer.MAX_VALUE));
        assertThat(tasks.size()).isEqualTo(4);
        assertThat(tasks).extracting("type", "name").contains(
                tuple("Project-Problem", "a"),
                tuple("Project-Problem", "problem a"),
                tuple("Project-Risk", "b"), tuple("Project-Bug", "summary 1"));
    }

    @SuppressWarnings("unchecked")
    @DataSet
    @Test
    public void testCountTaskOverDue() throws ParseException {

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date d = df.parse("2014-01-23 10:49:49");
        ProjectGenericTaskSearchCriteria criteria = new ProjectGenericTaskSearchCriteria();
        criteria.setDueDate(new DateSearchField(d));
        criteria.setProjectIds(new SetSearchField<>(1));
        criteria.setSaccountid(new NumberSearchField(1));
        List<ProjectGenericTask> tasks = genericTaskService
                .findPagableListByCriteria(new SearchRequest<>(
                        criteria, 0, Integer.MAX_VALUE));
        assertThat(tasks.size()).isEqualTo(2);
        assertThat(tasks).extracting("type", "name").contains(
                tuple("Project-Problem", "problem a"),
                tuple("Project-Risk", "b"));
    }

    @SuppressWarnings("unchecked")
    @DataSet
    @Test
    public void testListTaskOverDue() throws ParseException {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date d = df.parse("2014-01-23 10:49:49");

        ProjectGenericTaskSearchCriteria criteria = new ProjectGenericTaskSearchCriteria();
        criteria.setDueDate(new DateSearchField(d));
        criteria.setProjectIds(new SetSearchField<>(1));
        criteria.setSaccountid(new NumberSearchField(1));
        List<ProjectGenericTask> taskList = genericTaskService
                .findPagableListByCriteria(new SearchRequest<>(
                        criteria, 0, Integer.MAX_VALUE));

        assertThat(taskList.size()).isEqualTo(2);
        assertThat(taskList).extracting("type", "name").contains(
                tuple("Project-Problem", "problem a"),
                tuple("Project-Risk", "b"));
    }
}

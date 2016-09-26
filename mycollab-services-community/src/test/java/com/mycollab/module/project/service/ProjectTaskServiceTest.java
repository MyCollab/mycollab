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
package com.mycollab.module.project.service;

import com.mycollab.module.project.domain.SimpleTask;
import com.mycollab.test.DataSet;
import com.mycollab.test.service.IntegrationServiceTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
public class ProjectTaskServiceTest extends IntegrationServiceTest {

    @Autowired
    private ProjectTaskService projectTaskService;

    @DataSet
    @Test
    public void testFindById() {
        SimpleTask task = projectTaskService.findById(1, 1);
        assertThat(task.getName()).isEqualTo("task1");
        assertThat(task.getProjectShortname()).isEqualTo("aaa");
    }

    @DataSet
    @Test
    public void testFindByProjectAndTaskKey() {
        SimpleTask task = projectTaskService.findByProjectAndTaskKey(1, "aaa", 1);
        assertThat(task.getName()).isEqualTo("task1");
        assertThat(task.getProjectShortname()).isEqualTo("aaa");
    }
}

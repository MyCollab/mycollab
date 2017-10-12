/**
 * Copyright © MyCollab
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.project.service

import com.mycollab.module.project.domain.SimpleTask
import com.mycollab.test.DataSet
import com.mycollab.test.spring.IntegrationServiceTest
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner

import org.assertj.core.api.Assertions.assertThat

@RunWith(SpringJUnit4ClassRunner::class)
class ProjectTaskServiceTest : IntegrationServiceTest() {

    @Autowired
    private lateinit var projectTaskService: ProjectTaskService

    @DataSet
    @Test
    fun testFindById() {
        val task = projectTaskService.findById(1, 1)
        assertThat(task.name).isEqualTo("task1")
        assertThat(task.projectShortname).isEqualTo("aaa")
    }

    @DataSet
    @Test
    fun testFindByProjectAndTaskKey() {
        val task = projectTaskService.findByProjectAndTaskKey(1, "aaa", 1)
        assertThat(task.name).isEqualTo("task1")
        assertThat(task.projectShortname).isEqualTo("aaa")
    }
}

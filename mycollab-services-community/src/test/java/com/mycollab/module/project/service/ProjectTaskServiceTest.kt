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

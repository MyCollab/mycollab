package com.mycollab.module.project.service

import com.mycollab.test.DataSet
import com.mycollab.test.spring.IntegrationServiceTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner

@RunWith(SpringJUnit4ClassRunner::class)
class ProjectNotificationServiceTest : IntegrationServiceTest() {
    @Autowired
    private lateinit var projectService: ProjectService

    @DataSet
    @Test
    fun testGetNotification() {
        val projectRelayEmailNotifications = projectService.findProjectRelayEmailNotifications()
        assertThat(projectRelayEmailNotifications.size).isEqualTo(1)
        val projectRelayEmailNotification = projectRelayEmailNotifications[0]
        assertThat(projectRelayEmailNotification.notifyUsers.size).isEqualTo(2)
        assertThat(projectRelayEmailNotification.notifyUsers).extracting("username").contains("admin").contains("user2")
    }
}

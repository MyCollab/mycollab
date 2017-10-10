/**
 * mycollab-services-community - Parent pom providing dependency and plugin management for applications
		built with Maven
 * Copyright © ${project.inceptionYear} MyCollab (support@mycollab.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
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

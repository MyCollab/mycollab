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

import com.esofthead.mycollab.module.project.domain.ProjectRelayEmailNotification;
import com.esofthead.mycollab.test.DataSet;
import com.esofthead.mycollab.test.service.IntergrationServiceTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
public class ProjectNotificationServiceTest extends IntergrationServiceTest {
    @Autowired
    private ProjectService projectService;

    @DataSet
    @Test
    public void testGetNotification() {
        List<ProjectRelayEmailNotification> projectRelayEmailNotifications = projectService.findProjectRelayEmailNotifications();
        assertThat(projectRelayEmailNotifications.size()).isEqualTo(1);
        ProjectRelayEmailNotification projectRelayEmailNotification = projectRelayEmailNotifications.get(0);
        assertThat(projectRelayEmailNotification.getNotifyUsers().size()).isEqualTo(2);
        assertThat(projectRelayEmailNotification.getNotifyUsers()).extracting("username").
                contains("admin").contains("user2");
    }
}

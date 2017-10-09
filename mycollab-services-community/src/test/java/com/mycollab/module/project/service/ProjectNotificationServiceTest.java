package com.mycollab.module.project.service;

import com.mycollab.module.project.domain.ProjectRelayEmailNotification;
import com.mycollab.test.DataSet;
import com.mycollab.test.spring.IntegrationServiceTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
public class ProjectNotificationServiceTest extends IntegrationServiceTest {
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

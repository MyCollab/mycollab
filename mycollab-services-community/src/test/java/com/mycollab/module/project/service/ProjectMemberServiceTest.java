package com.mycollab.module.project.service;

import com.mycollab.module.project.domain.SimpleProjectMember;
import com.mycollab.module.user.domain.SimpleUser;
import com.mycollab.test.DataSet;
import com.mycollab.test.spring.IntegrationServiceTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
public class ProjectMemberServiceTest extends IntegrationServiceTest {
    @Autowired
    private ProjectMemberService projectMemberService;

    @DataSet
    @Test
    public void testGetActiveMembersInproject() {
        List<SimpleUser> activeUsers = projectMemberService.getActiveUsersInProject(1, 1);
        assertThat(activeUsers.size()).isEqualTo(1);
        assertThat(activeUsers).extracting("username").contains("user1");
    }

    @DataSet
    @Test
    public void testGetMembersNotInProject() {
        List<SimpleUser> users = projectMemberService.getUsersNotInProject(1, 1);

        assertThat(users.size()).isEqualTo(2);
        assertThat(users).extracting("username").contains("user2", "user3");
    }

    @DataSet
    @Test
    public void testGetProjectMembersInProjects() {
        List<SimpleUser> users = projectMemberService.getActiveUsersInProjects(Arrays.asList(1, 2), 1);

        assertThat(users.size()).isEqualTo(3);
        assertThat(users).extracting("username").contains("user1", "user2", "user3");
    }

    @DataSet
    @Test
    public void testGetUsersNotInProjects() {
        List<SimpleUser> users = projectMemberService.getUsersNotInProject(1, 1);
        assertThat(users.size()).isEqualTo(2);
        assertThat(users).extracting("username").contains("user2", "user3");
    }

    @DataSet
    @Test
    public void testFindMemberByUsername() {
        SimpleProjectMember member = projectMemberService.findMemberByUsername("user1", 1, 1);
        assertThat(member.getProjectid()).isEqualTo(1);
        assertThat(member.getStatus()).isEqualTo("Active");
        assertThat(member.getUsername()).isEqualTo("user1");
    }
}

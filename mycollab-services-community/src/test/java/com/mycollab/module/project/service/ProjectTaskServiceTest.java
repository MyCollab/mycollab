package com.mycollab.module.project.service;

import com.mycollab.module.project.domain.SimpleTask;
import com.mycollab.test.DataSet;
import com.mycollab.test.spring.IntegrationServiceTest;
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

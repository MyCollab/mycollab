package com.mycollab.module.tracker.service;

import com.mycollab.module.tracker.domain.SimpleRelatedBug;
import com.mycollab.test.DataSet;
import com.mycollab.test.spring.IntegrationServiceTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author MyCollab Ltd
 * @since 5.2.12
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class BugRelationServiceTest extends IntegrationServiceTest {

    @Autowired
    private BugRelationService bugRelationService;

    @DataSet
    @Test
    public void testGetRelatedBugs() {
        List<SimpleRelatedBug> relatedBugs = bugRelationService.findRelatedBugs(1);
        assertThat(relatedBugs.size()).isEqualTo(2);
    }
}

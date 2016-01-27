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

import static org.assertj.core.api.Assertions.*;

import com.esofthead.mycollab.core.arguments.RangeDateSearchField;
import com.esofthead.mycollab.module.project.domain.criteria.ProjectGenericTaskSearchCriteria;
import com.esofthead.mycollab.test.DataSet;
import com.esofthead.mycollab.test.service.IntergrationServiceTest;
import org.joda.time.LocalDate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
public class ProjectGenericTaskServiceTest extends IntergrationServiceTest {
    @Autowired
    protected ProjectGenericTaskService projectGenericTaskService;

    @DataSet
    @Test
    public void testGetAccountsHasOverdueAssignments() {
        ProjectGenericTaskSearchCriteria criteria = new ProjectGenericTaskSearchCriteria();
        criteria.setSaccountid(null);
        LocalDate now = new LocalDate();
        RangeDateSearchField rangeDateSearchField = new RangeDateSearchField(now.minusDays(10000).toDate(), now.toDate());
        criteria.setDateInRange(rangeDateSearchField);
        List<Map> accounts = projectGenericTaskService.getAccountsHasOverdueAssignments(criteria);
        assertThat(accounts).isNotEmpty().hasSize(2);
        assertThat(accounts.get(0)).containsExactly(entry("subdomain", "a"), entry("id", 1));
        assertThat(accounts.get(1)).containsExactly(entry("subdomain", "b"), entry("id", 2));
    }
}

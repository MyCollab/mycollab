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
package com.mycollab.module.project.service;

import com.mycollab.common.domain.GroupItem;
import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.db.arguments.RangeDateSearchField;
import com.mycollab.db.arguments.SetSearchField;
import com.mycollab.module.project.domain.criteria.ProjectTicketSearchCriteria;
import com.mycollab.module.user.domain.BillingAccount;
import com.mycollab.test.DataSet;
import com.mycollab.test.service.IntegrationServiceTest;
import org.joda.time.LocalDate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@RunWith(SpringJUnit4ClassRunner.class)
public class ProjectTicketServiceTest extends IntegrationServiceTest {
    @Autowired
    private ProjectTicketService projectTicketService;

    @DataSet
    @Test
    public void testGetAccountsHasOverdueAssignments() {
        ProjectTicketSearchCriteria criteria = new ProjectTicketSearchCriteria();
        criteria.setSaccountid(null);
        LocalDate now = new LocalDate();
        RangeDateSearchField rangeDateSearchField = new RangeDateSearchField(now.minusDays(10000).toDate(), now.toDate());
        criteria.setDateInRange(rangeDateSearchField);
        List<BillingAccount> accounts = projectTicketService.getAccountsHasOverdueAssignments(criteria);
        assertThat(accounts).isNotEmpty().hasSize(2);
        assertThat(accounts).extracting("subdomain", "id").contains(tuple("a", 1), tuple("b", 2));
    }

    @DataSet
    @Test
    public void testGetAssigneeSummary() {
        ProjectTicketSearchCriteria criteria = new ProjectTicketSearchCriteria();
        criteria.setSaccountid(NumberSearchField.equal(1));
        criteria.setProjectIds(new SetSearchField<>(3));
        List<GroupItem> groupItems = projectTicketService.getAssigneeSummary(criteria);
        assertThat(groupItems).isNotEmpty();
        assertThat(groupItems).extracting("groupid", "value").contains(tuple("hai79", 2), tuple("linhduong", 1));
    }
}

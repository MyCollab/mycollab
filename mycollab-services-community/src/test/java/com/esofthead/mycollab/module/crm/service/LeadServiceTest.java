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
package com.esofthead.mycollab.module.crm.service;

import com.esofthead.mycollab.core.arguments.*;
import com.esofthead.mycollab.module.crm.domain.SimpleLead;
import com.esofthead.mycollab.module.crm.domain.criteria.LeadSearchCriteria;
import com.esofthead.mycollab.test.DataSet;
import com.esofthead.mycollab.test.service.IntergrationServiceTest;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@RunWith(SpringJUnit4ClassRunner.class)
public class LeadServiceTest extends IntergrationServiceTest {

    @Autowired
    protected LeadService leadService;

    @SuppressWarnings("unchecked")
    @DataSet
    @Test
    public void testSearchByCriteria() {
        List<SimpleLead> leads = leadService.findPagableListByCriteria(
                new SearchRequest<>(getCriteria(), 0, 2));
        assertThat(leads.size()).isEqualTo(2);
        assertThat(leads).extracting("id", "source").contains(
                tuple(1, "Cold Call"), tuple(2, "Employee"));
    }

    @DataSet
    @Test
    public void testGetTotalCounts() {
        Assert.assertEquals(2, leadService.getTotalCount(getCriteria()));
    }

    private LeadSearchCriteria getCriteria() {
        LeadSearchCriteria criteria = new LeadSearchCriteria();
        criteria.setLeadName(new StringSearchField("Nguyen"));
        criteria.setSaccountid(new NumberSearchField(1));
        return criteria;
    }

    @SuppressWarnings("unchecked")
    @Test
    @DataSet
    public void testSearchLeadName() {
        LeadSearchCriteria criteria = new LeadSearchCriteria();
        criteria.setLeadName(new StringSearchField(SearchField.AND,
                "Nguyen Hai"));
        criteria.setSaccountid(new NumberSearchField(1));

        List<SimpleLead> leads = leadService.findPagableListByCriteria(
                new SearchRequest<>(criteria, 0, 2));
        assertThat(leads.size()).isEqualTo(1);
        assertThat(leads).extracting("id", "source").contains(
                tuple(1, "Cold Call"));
    }

    @SuppressWarnings("unchecked")
    @Test
    @DataSet
    public void testSearchAssignUser() {
        LeadSearchCriteria criteria = new LeadSearchCriteria();
        criteria.setAssignUsers(new SetSearchField<>("linh", "hai"));
        criteria.setSaccountid(new NumberSearchField(1));

        List<SimpleLead> leads = leadService
                .findPagableListByCriteria(new SearchRequest<>(criteria, 0, 2));
        assertThat(leads.size()).isEqualTo(2);
        assertThat(leads).extracting("id", "source").contains(
                tuple(1, "Cold Call"), tuple(2, "Employee"));
    }
}

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
package com.mycollab.module.crm.service;

import com.mycollab.module.crm.domain.SimpleLead;
import com.mycollab.module.crm.domain.criteria.LeadSearchCriteria;
import com.mycollab.test.DataSet;
import com.mycollab.test.service.IntergrationServiceTest;
import com.mycollab.db.arguments.BasicSearchRequest;
import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.db.arguments.SetSearchField;
import com.mycollab.db.arguments.StringSearchField;
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

    @DataSet
    @Test
    public void testSearchByCriteria() {
        List<SimpleLead> leads = leadService.findPagableListByCriteria(new BasicSearchRequest<>(getCriteria(), 0, 2));
        assertThat(leads.size()).isEqualTo(2);
        assertThat(leads).extracting("id", "source").contains(tuple(1, "Cold Call"), tuple(2, "Employee"));
    }

    @DataSet
    @Test
    public void testGetTotalCounts() {
        Assert.assertEquals(new Integer(2), leadService.getTotalCount(getCriteria()));
    }

    private LeadSearchCriteria getCriteria() {
        LeadSearchCriteria criteria = new LeadSearchCriteria();
        criteria.setLeadName(StringSearchField.and("Nguyen"));
        criteria.setSaccountid(new NumberSearchField(1));
        return criteria;
    }

    @Test
    @DataSet
    public void testSearchLeadName() {
        LeadSearchCriteria criteria = new LeadSearchCriteria();
        criteria.setLeadName(StringSearchField.and("Nguyen Hai"));
        criteria.setSaccountid(new NumberSearchField(1));

        List<SimpleLead> leads = leadService.findPagableListByCriteria(new BasicSearchRequest<>(criteria, 0, 2));
        assertThat(leads.size()).isEqualTo(1);
        assertThat(leads).extracting("id", "source").contains(tuple(1, "Cold Call"));
    }

    @Test
    @DataSet
    public void testSearchAssignUser() {
        LeadSearchCriteria criteria = new LeadSearchCriteria();
        criteria.setAssignUsers(new SetSearchField<>("linh", "hai"));
        criteria.setSaccountid(new NumberSearchField(1));

        List<SimpleLead> leads = leadService.findPagableListByCriteria(new BasicSearchRequest<>(criteria, 0, 2));
        assertThat(leads.size()).isEqualTo(2);
        assertThat(leads).extracting("id", "source").contains(tuple(1, "Cold Call"), tuple(2, "Employee"));
    }
}

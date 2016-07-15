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

import com.mycollab.db.arguments.BasicSearchRequest;
import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.db.arguments.SetSearchField;
import com.mycollab.db.arguments.StringSearchField;
import com.mycollab.module.crm.domain.SimpleOpportunity;
import com.mycollab.module.crm.domain.criteria.OpportunitySearchCriteria;
import com.mycollab.test.DataSet;
import com.mycollab.test.service.IntegrationServiceTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@RunWith(SpringJUnit4ClassRunner.class)
public class OpportunityServiceTest extends IntegrationServiceTest {

    @Autowired
    protected OpportunityService opportunityService;

    @DataSet
    @Test
    public void testSearchByCriteria() {
        List<SimpleOpportunity> opportunities = opportunityService.findPagableListByCriteria(new BasicSearchRequest<>(getCriteria(), 0, Integer.MAX_VALUE));

        assertThat(opportunities.size()).isEqualTo(2);
        assertThat(opportunities).extracting("id", "salesstage", "source").contains(
                tuple(1, "1", "Cold Call"), tuple(2, "2", "Employee"));
    }

    @DataSet
    @Test
    public void testGetTotalCount() {
        List<SimpleOpportunity> opportunities = opportunityService.findPagableListByCriteria(new BasicSearchRequest<>(getCriteria(),
                0, Integer.MAX_VALUE));
        assertThat(opportunities.size()).isEqualTo(2);
    }

    private OpportunitySearchCriteria getCriteria() {
        OpportunitySearchCriteria criteria = new OpportunitySearchCriteria();
        criteria.setAccountId(new NumberSearchField(1));
        criteria.setCampaignId(new NumberSearchField(1));
        criteria.setOpportunityName(StringSearchField.and("aa"));
        criteria.setSaccountid(new NumberSearchField(1));
        return criteria;
    }

    @Test
    @DataSet
    public void testSearchAssignUsers() {
        OpportunitySearchCriteria criteria = new OpportunitySearchCriteria();
        criteria.setAssignUsers(new SetSearchField<>("hai", "linh"));
        criteria.setSaccountid(new NumberSearchField(1));

        List<SimpleOpportunity> opportunities = opportunityService.findPagableListByCriteria(new BasicSearchRequest<>(criteria,
                0, Integer.MAX_VALUE));

        assertThat(opportunities.size()).isEqualTo(2);
        assertThat(opportunities).extracting("id", "salesstage", "source").contains(
                tuple(1, "1", "Cold Call"), tuple(2, "2", "Employee"));
    }
}

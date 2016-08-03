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

import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.db.arguments.BasicSearchRequest;
import com.mycollab.db.arguments.SetSearchField;
import com.mycollab.db.arguments.StringSearchField;
import com.mycollab.module.crm.domain.SimpleCampaign;
import com.mycollab.module.crm.domain.criteria.CampaignSearchCriteria;
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
public class CampaignServiceTest extends IntegrationServiceTest {

    @Autowired
    protected CampaignService campaignService;

    @DataSet
    @Test
    public void testSearchByCriteria() {
        List<SimpleCampaign> campaigns = campaignService.findPageableListByCriteria(new BasicSearchRequest<>(getCriteria()));

        assertThat(campaigns.size()).isEqualTo(2);
        assertThat(campaigns).extracting("id", "campaignname", "status").contains(tuple(1, "AAA", "a"), tuple(2, "ABB", "b"));
    }

    @DataSet
    @Test
    public void testGetTotalCounts() {
        List<SimpleCampaign> campaigns = campaignService.findPageableListByCriteria(new BasicSearchRequest<>(getCriteria()));

        assertThat(campaigns.size()).isEqualTo(2);
    }

    private CampaignSearchCriteria getCriteria() {
        CampaignSearchCriteria criteria = new CampaignSearchCriteria();
        criteria.setAssignUser(StringSearchField.and("hai79"));
        criteria.setCampaignName(StringSearchField.and("A"));
        criteria.setSaccountid(new NumberSearchField(1));
        criteria.setAssignUsers(new SetSearchField<>("hai79", "linh"));
        criteria.setStatuses(new SetSearchField<>("a", "b"));
        criteria.setTypes(new SetSearchField<>("a", "b"));
        return criteria;
    }
}

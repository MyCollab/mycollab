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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchField;
import com.esofthead.mycollab.core.arguments.SearchRequest;
import com.esofthead.mycollab.core.arguments.SetSearchField;
import com.esofthead.mycollab.core.arguments.StringSearchField;
import com.esofthead.mycollab.module.crm.domain.SimpleOpportunity;
import com.esofthead.mycollab.module.crm.domain.criteria.OpportunitySearchCriteria;
import com.esofthead.mycollab.test.DataSet;
import com.esofthead.mycollab.test.service.IntergrationServiceTest;

@RunWith(SpringJUnit4ClassRunner.class)
public class OpportunityServiceTest extends IntergrationServiceTest {

	@Autowired
	protected OpportunityService opportunityService;

	@SuppressWarnings("unchecked")
	@DataSet
	@Test
	public void testSearchByCriteria() {
		List<SimpleOpportunity> opportunities = opportunityService
				.findPagableListByCriteria(new SearchRequest<OpportunitySearchCriteria>(
						getCriteria(), 0, Integer.MAX_VALUE));

		assertThat(opportunities.size()).isEqualTo(2);
		assertThat(opportunities).extracting("id", "salesstage", "source").contains(
				tuple(1, "1", "Cold Call"), tuple(2, "2", "Employee"));
	}

	@SuppressWarnings("unchecked")
	@DataSet
	@Test
	public void testGetTotalCount() {
		List<SimpleOpportunity> opportunities = opportunityService
				.findPagableListByCriteria(new SearchRequest<OpportunitySearchCriteria>(
						getCriteria(), 0, Integer.MAX_VALUE));

		assertThat(opportunities.size()).isEqualTo(2);
	}

	private OpportunitySearchCriteria getCriteria() {
		OpportunitySearchCriteria criteria = new OpportunitySearchCriteria();
		criteria.setAccountId(new NumberSearchField(SearchField.AND, 1));
		criteria.setCampaignId(new NumberSearchField(SearchField.AND, 1));
		criteria.setOpportunityName(new StringSearchField(SearchField.AND, "aa"));
		criteria.setSaccountid(new NumberSearchField(SearchField.AND, 1));
		return criteria;
	}

	@SuppressWarnings("unchecked")
	@Test
	@DataSet
	public void testSearchAssignUsers() {
		OpportunitySearchCriteria criteria = new OpportunitySearchCriteria();
		criteria.setAssignUsers(new SetSearchField<String>(SearchField.AND,
				new String[]{"hai", "linh"}));
		criteria.setSaccountid(new NumberSearchField(1));

		List<SimpleOpportunity> opportunities = opportunityService
				.findPagableListByCriteria(new SearchRequest<OpportunitySearchCriteria>(
						criteria, 0, Integer.MAX_VALUE));

		assertThat(opportunities.size()).isEqualTo(2);
		assertThat(opportunities).extracting("id", "salesstage", "source").contains(
				tuple(1, "1", "Cold Call"), tuple(2, "2", "Employee"));
	}
}

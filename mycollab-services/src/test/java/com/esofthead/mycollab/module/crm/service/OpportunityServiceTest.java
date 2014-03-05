/**
 * This file is part of mycollab-services.
 *
 * mycollab-services is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-services is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-services.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.module.crm.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchField;
import com.esofthead.mycollab.core.arguments.SearchRequest;
import com.esofthead.mycollab.core.arguments.SetSearchField;
import com.esofthead.mycollab.core.arguments.StringSearchField;
import com.esofthead.mycollab.module.crm.domain.criteria.OpportunitySearchCriteria;
import com.esofthead.mycollab.test.DataSet;
import com.esofthead.mycollab.test.MyCollabClassRunner;
import com.esofthead.mycollab.test.service.ServiceTest;

@RunWith(MyCollabClassRunner.class)
public class OpportunityServiceTest extends ServiceTest {

	@Autowired
	protected OpportunityService opportunityService;

	@DataSet
	@Test
	public void testSearchByCriteria() {
		Assert.assertEquals(
				2,
				opportunityService.findPagableListByCriteria(
						new SearchRequest<OpportunitySearchCriteria>(
								getCriteria(), 0, 2)).size());
	}

	@DataSet
	@Test
	public void testGetTotalCount() {
		Assert.assertEquals(2, opportunityService.getTotalCount(getCriteria()));
	}

	private OpportunitySearchCriteria getCriteria() {
		OpportunitySearchCriteria criteria = new OpportunitySearchCriteria();
		criteria.setAccountId(new NumberSearchField(SearchField.AND, 1));
		criteria.setCampaignId(new NumberSearchField(SearchField.AND, 1));
		criteria.setOpportunityName(new StringSearchField(SearchField.AND, "aa"));
		criteria.setSaccountid(new NumberSearchField(SearchField.AND, 1));
		return criteria;
	}

	@Test
	@DataSet
	public void testSearchAssignUsers() {
		OpportunitySearchCriteria criteria = new OpportunitySearchCriteria();
		criteria.setAssignUsers(new SetSearchField<String>(SearchField.AND,
				new String[] { "hai", "linh" }));
		criteria.setSaccountid(new NumberSearchField(1));

		Assert.assertEquals(2, opportunityService.getTotalCount(criteria));
		Assert.assertEquals(
				2,
				opportunityService.findPagableListByCriteria(
						new SearchRequest<OpportunitySearchCriteria>(criteria,
								0, Integer.MAX_VALUE)).size());
	}
}

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
import com.esofthead.mycollab.module.crm.domain.criteria.LeadSearchCriteria;
import com.esofthead.mycollab.test.DataSet;
import com.esofthead.mycollab.test.MyCollabClassRunner;
import com.esofthead.mycollab.test.service.ServiceTest;

@RunWith(MyCollabClassRunner.class)
public class LeadServiceTest extends ServiceTest {

	@Autowired
	protected LeadService leadService;

	@DataSet
	@Test
	public void testSearchByCriteria() {
		Assert.assertEquals(
				2,
				leadService.findPagableListByCriteria(
						new SearchRequest<LeadSearchCriteria>(getCriteria(), 0,
								2)).size());
	}

	@DataSet
	@Test
	public void testGetTotalCounts() {
		Assert.assertEquals(2, leadService.getTotalCount(getCriteria()));
	}

	private LeadSearchCriteria getCriteria() {
		LeadSearchCriteria criteria = new LeadSearchCriteria();
		criteria.setLeadName(new StringSearchField(SearchField.AND, "Nguyen"));
		criteria.setSaccountid(new NumberSearchField(SearchField.AND, 1));
		return criteria;
	}

	@Test
	@DataSet
	public void testSearchLeadName() {
		LeadSearchCriteria criteria = new LeadSearchCriteria();
		criteria.setLeadName(new StringSearchField(SearchField.AND,
				"Nguyen Hai"));
		criteria.setSaccountid(new NumberSearchField(1));

		Assert.assertEquals(1, leadService.getTotalCount(criteria));
		Assert.assertEquals(
				1,
				leadService.findPagableListByCriteria(
						new SearchRequest<LeadSearchCriteria>(criteria, 0,
								Integer.MAX_VALUE)).size());
	}

	@Test
	@DataSet
	public void testSearchAssignUser() {
		LeadSearchCriteria criteria = new LeadSearchCriteria();
		criteria.setAssignUsers(new SetSearchField<String>(SetSearchField.AND,
				new String[] { "linh", "hai" }));
		criteria.setSaccountid(new NumberSearchField(1));

		Assert.assertEquals(2, leadService.getTotalCount(criteria));
		Assert.assertEquals(
				2,
				leadService.findPagableListByCriteria(
						new SearchRequest<LeadSearchCriteria>(criteria, 0,
								Integer.MAX_VALUE)).size());
	}
}

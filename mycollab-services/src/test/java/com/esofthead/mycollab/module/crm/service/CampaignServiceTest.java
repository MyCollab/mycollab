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

import java.util.Date;
import java.util.GregorianCalendar;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

import com.esofthead.mycollab.core.arguments.DateSearchField;
import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.RangeDateSearchField;
import com.esofthead.mycollab.core.arguments.SearchField;
import com.esofthead.mycollab.core.arguments.SearchRequest;
import com.esofthead.mycollab.core.arguments.SetSearchField;
import com.esofthead.mycollab.core.arguments.StringSearchField;
import com.esofthead.mycollab.module.crm.domain.criteria.CampaignSearchCriteria;
import com.esofthead.mycollab.test.DataSet;
import com.esofthead.mycollab.test.MyCollabClassRunner;
import com.esofthead.mycollab.test.service.ServiceTest;

@RunWith(MyCollabClassRunner.class)
public class CampaignServiceTest extends ServiceTest {

	@Autowired
	protected CampaignService campaignService;

	@DataSet
	@Test
	public void testSearchByCriteria() {
		Assert.assertEquals(
				10,
				campaignService.findPagableListByCriteria(
						new SearchRequest<CampaignSearchCriteria>(
								getCriteria(), 0, Integer.MAX_VALUE)).size());
	}

	@DataSet
	@Test
	public void testGetTotalCounts() {
		Assert.assertEquals(10, campaignService.getTotalCount(getCriteria()));
	}

	private CampaignSearchCriteria getCriteria() {
		CampaignSearchCriteria criteria = new CampaignSearchCriteria();
		criteria.setAssignUserName(new StringSearchField(SearchField.AND, "Hai"));
		criteria.setCampaignName(new StringSearchField(SearchField.AND, "A"));
		criteria.setSaccountid(new NumberSearchField(SearchField.AND, 1));
		criteria.setAssignUsers(new SetSearchField<String>(SearchField.AND,
				new String[] { "hai79", "linh" }));
		criteria.setStatuses(new SetSearchField<String>(SearchField.AND,
				new String[] { "a", "b" }));
		criteria.setTypes(new SetSearchField<String>(SearchField.AND,
				new String[] { "a", "b" }));
		return criteria;
	}

	@Test
	@DataSet
	public void testSearchStartDateRangeNext7Days() {
		CampaignSearchCriteria criteria = new CampaignSearchCriteria();
		Date startDate = new GregorianCalendar(2012, 11, 20).getTime();
		Date endDate = new GregorianCalendar(2012, 11, 28).getTime();
		criteria.setStartDateRange(new RangeDateSearchField(startDate, endDate));
		criteria.setSaccountid(new NumberSearchField(1));

		Assert.assertEquals(1, campaignService.getTotalCount(criteria));
		Assert.assertEquals(
				1,
				campaignService.findPagableListByCriteria(
						new SearchRequest<CampaignSearchCriteria>(criteria, 0,
								Integer.MAX_VALUE)).size());
	}

	@Test
	@DataSet
	public void testSearchStartDateRangeLast7Days() {
		CampaignSearchCriteria criteria = new CampaignSearchCriteria();
		Date startDate = new GregorianCalendar(2012, 11, 14).getTime();
		Date endDate = new GregorianCalendar(2012, 11, 21).getTime();
		criteria.setStartDateRange(new RangeDateSearchField(startDate, endDate));
		criteria.setSaccountid(new NumberSearchField(1));

		Assert.assertEquals(4, campaignService.getTotalCount(criteria));
		Assert.assertEquals(
				4,
				campaignService.findPagableListByCriteria(
						new SearchRequest<CampaignSearchCriteria>(criteria, 0,
								Integer.MAX_VALUE)).size());
	}

	@Test
	@DataSet
	public void testSearchEndDateLessThanEqual() {
		CampaignSearchCriteria criteria = new CampaignSearchCriteria();
		Date endDate = new GregorianCalendar(2012, 11, 21).getTime();
		criteria.setEndDate(new DateSearchField(SearchField.AND,
				DateSearchField.LESSTHANEQUAL, endDate));
		criteria.setSaccountid(new NumberSearchField(1));
		Assert.assertEquals(5, campaignService.getTotalCount(criteria));
		Assert.assertEquals(
				5,
				campaignService.findPagableListByCriteria(
						new SearchRequest<CampaignSearchCriteria>(criteria, 0,
								Integer.MAX_VALUE)).size());
	}

	@Test
	@DataSet
	public void testSearchEndDateLessThan() {
		CampaignSearchCriteria criteria = new CampaignSearchCriteria();
		Date startDate = new GregorianCalendar(2012, 11, 21).getTime();
		criteria.setEndDate(new DateSearchField(SearchField.AND,
				DateSearchField.LESSTHAN, startDate));
		criteria.setSaccountid(new NumberSearchField(1));
		Assert.assertEquals(4, campaignService.getTotalCount(criteria));
		Assert.assertEquals(
				4,
				campaignService.findPagableListByCriteria(
						new SearchRequest<CampaignSearchCriteria>(criteria, 0,
								Integer.MAX_VALUE)).size());
	}

	@Test
	@DataSet
	public void testSearchEndDateGreaterThanEqual() {
		CampaignSearchCriteria criteria = new CampaignSearchCriteria();
		Date startDate = new GregorianCalendar(2012, 11, 20).getTime();
		criteria.setEndDate(new DateSearchField(SearchField.AND,
				DateSearchField.GREATERTHANEQUAL, startDate));
		criteria.setSaccountid(new NumberSearchField(1));

		Assert.assertEquals(7, campaignService.getTotalCount(criteria));
		Assert.assertEquals(
				7,
				campaignService.findPagableListByCriteria(
						new SearchRequest<CampaignSearchCriteria>(criteria, 0,
								Integer.MAX_VALUE)).size());
	}

	@Test
	@DataSet
	public void testSearchEndDateGreaterThan() {
		CampaignSearchCriteria criteria = new CampaignSearchCriteria();
		Date startDate = new GregorianCalendar(2012, 11, 20).getTime();
		criteria.setEndDate(new DateSearchField(SearchField.AND,
				DateSearchField.GREATERTHAN, startDate));
		criteria.setSaccountid(new NumberSearchField(1));

		Assert.assertEquals(6, campaignService.getTotalCount(criteria));
		Assert.assertEquals(
				6,
				campaignService.findPagableListByCriteria(
						new SearchRequest<CampaignSearchCriteria>(criteria, 0,
								Integer.MAX_VALUE)).size());
	}

	@Test
	@DataSet
	public void testSearchEndDateNotEqual() {
		CampaignSearchCriteria criteria = new CampaignSearchCriteria();
		Date startDate = new GregorianCalendar(2012, 11, 20).getTime();
		criteria.setEndDate(new DateSearchField(SearchField.AND,
				DateSearchField.NOTEQUAL, startDate));
		criteria.setSaccountid(new NumberSearchField(1));

		Assert.assertEquals(9, campaignService.getTotalCount(criteria));
		Assert.assertEquals(
				9,
				campaignService.findPagableListByCriteria(
						new SearchRequest<CampaignSearchCriteria>(criteria, 0,
								Integer.MAX_VALUE)).size());
	}

	@Test
	@DataSet
	public void testSearchEndDateEqual() {
		CampaignSearchCriteria criteria = new CampaignSearchCriteria();
		Date startDate = new GregorianCalendar(2012, 11, 20).getTime();
		criteria.setEndDate(new DateSearchField(SearchField.AND,
				DateSearchField.EQUAL, startDate));
		criteria.setSaccountid(new NumberSearchField(1));

		Assert.assertEquals(1, campaignService.getTotalCount(criteria));
		Assert.assertEquals(
				1,
				campaignService.findPagableListByCriteria(
						new SearchRequest<CampaignSearchCriteria>(criteria, 0,
								Integer.MAX_VALUE)).size());
	}

	@Test
	@DataSet
	public void testSearchEndDateBetween() {
		CampaignSearchCriteria criteria = new CampaignSearchCriteria();
		Date from = new GregorianCalendar(2012, 11, 20).getTime();
		Date to = new GregorianCalendar(2012, 11, 21).getTime();
		criteria.setEndDateRange(new RangeDateSearchField(from, to));
		criteria.setSaccountid(new NumberSearchField(1));

		Assert.assertEquals(2, campaignService.getTotalCount(criteria));
		Assert.assertEquals(
				2,
				campaignService.findPagableListByCriteria(
						new SearchRequest<CampaignSearchCriteria>(criteria, 0,
								Integer.MAX_VALUE)).size());
	}

	@Test
	@DataSet
	public void testSearchStartDateLessThanEqual() {
		CampaignSearchCriteria criteria = new CampaignSearchCriteria();
		Date startDate = new GregorianCalendar(2012, 11, 16).getTime();
		criteria.setStartDate(new DateSearchField(SearchField.AND,
				DateSearchField.LESSTHANEQUAL, startDate));
		criteria.setSaccountid(new NumberSearchField(1));

		Assert.assertEquals(5, campaignService.getTotalCount(criteria));
		Assert.assertEquals(
				5,
				campaignService.findPagableListByCriteria(
						new SearchRequest<CampaignSearchCriteria>(criteria, 0,
								Integer.MAX_VALUE)).size());
	}

	@Test
	@DataSet
	public void testSearchStartDateLessThan() {
		CampaignSearchCriteria criteria = new CampaignSearchCriteria();
		Date startDate = new GregorianCalendar(2012, 11, 16).getTime();
		criteria.setStartDate(new DateSearchField(SearchField.AND,
				DateSearchField.LESSTHAN, startDate));
		criteria.setSaccountid(new NumberSearchField(1));

		Assert.assertEquals(4, campaignService.getTotalCount(criteria));
		Assert.assertEquals(
				4,
				campaignService.findPagableListByCriteria(
						new SearchRequest<CampaignSearchCriteria>(criteria, 0,
								Integer.MAX_VALUE)).size());
	}

	@Test
	@DataSet
	public void testSearchStartDateGreaterThanEqual() {
		CampaignSearchCriteria criteria = new CampaignSearchCriteria();
		Date startDate = new GregorianCalendar(2012, 11, 15).getTime();
		criteria.setStartDate(new DateSearchField(SearchField.AND,
				DateSearchField.GREATERTHANEQUAL, startDate));
		criteria.setSaccountid(new NumberSearchField(1));

		Assert.assertEquals(7, campaignService.getTotalCount(criteria));
		Assert.assertEquals(
				7,
				campaignService.findPagableListByCriteria(
						new SearchRequest<CampaignSearchCriteria>(criteria, 0,
								Integer.MAX_VALUE)).size());
	}

	@Test
	@DataSet
	public void testSearchStartDateGreaterThan() {
		CampaignSearchCriteria criteria = new CampaignSearchCriteria();
		Date startDate = new GregorianCalendar(2012, 11, 15).getTime();
		criteria.setStartDate(new DateSearchField(SearchField.AND,
				DateSearchField.GREATERTHAN, startDate));
		criteria.setSaccountid(new NumberSearchField(1));

		Assert.assertEquals(6, campaignService.getTotalCount(criteria));
		Assert.assertEquals(
				6,
				campaignService.findPagableListByCriteria(
						new SearchRequest<CampaignSearchCriteria>(criteria, 0,
								Integer.MAX_VALUE)).size());
	}

	@Test
	@DataSet
	public void testSearchStartDateNotEqual() {
		CampaignSearchCriteria criteria = new CampaignSearchCriteria();
		Date startDate = new GregorianCalendar(2012, 11, 15).getTime();
		criteria.setStartDate(new DateSearchField(SearchField.AND,
				DateSearchField.NOTEQUAL, startDate));
		criteria.setSaccountid(new NumberSearchField(1));

		Assert.assertEquals(9, campaignService.getTotalCount(criteria));
		Assert.assertEquals(
				9,
				campaignService.findPagableListByCriteria(
						new SearchRequest<CampaignSearchCriteria>(criteria, 0,
								Integer.MAX_VALUE)).size());
	}

	@Test
	@DataSet
	public void testSearchStartDateEqual() {
		CampaignSearchCriteria criteria = new CampaignSearchCriteria();
		Date startDate = new GregorianCalendar(2012, 11, 15).getTime();
		criteria.setStartDate(new DateSearchField(SearchField.AND,
				DateSearchField.EQUAL, startDate));
		criteria.setSaccountid(new NumberSearchField(1));

		Assert.assertEquals(1, campaignService.getTotalCount(criteria));
		Assert.assertEquals(
				1,
				campaignService.findPagableListByCriteria(
						new SearchRequest<CampaignSearchCriteria>(criteria, 0,
								Integer.MAX_VALUE)).size());
	}

	@Test
	@DataSet
	public void testSearchStartDateBetween() {
		CampaignSearchCriteria criteria = new CampaignSearchCriteria();
		Date from = new GregorianCalendar(2012, 11, 15).getTime();
		Date to = new GregorianCalendar(2012, 11, 17).getTime();
		criteria.setStartDateRange(new RangeDateSearchField(from, to));
		criteria.setSaccountid(new NumberSearchField(1));

		Assert.assertEquals(3, campaignService.getTotalCount(criteria));
		Assert.assertEquals(
				3,
				campaignService.findPagableListByCriteria(
						new SearchRequest<CampaignSearchCriteria>(criteria, 0,
								Integer.MAX_VALUE)).size());
	}
}

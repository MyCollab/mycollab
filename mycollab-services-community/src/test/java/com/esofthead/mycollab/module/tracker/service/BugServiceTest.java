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
package com.esofthead.mycollab.module.tracker.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

import com.esofthead.mycollab.common.domain.GroupItem;
import com.esofthead.mycollab.core.arguments.DateSearchField;
import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchField;
import com.esofthead.mycollab.core.arguments.SearchRequest;
import com.esofthead.mycollab.core.arguments.SetSearchField;
import com.esofthead.mycollab.core.arguments.StringSearchField;
import com.esofthead.mycollab.module.tracker.domain.BugWithBLOBs;
import com.esofthead.mycollab.module.tracker.domain.SimpleBug;
import com.esofthead.mycollab.module.tracker.domain.criteria.BugSearchCriteria;
import com.esofthead.mycollab.test.DataSet;
import com.esofthead.mycollab.test.MyCollabClassRunner;
import com.esofthead.mycollab.test.service.ServiceTest;

@RunWith(MyCollabClassRunner.class)
public class BugServiceTest extends ServiceTest {

	@Autowired
	protected BugService bugService;

	@SuppressWarnings("unchecked")
	@DataSet
	@Test
	public void testGetListBugs() {
		List<SimpleBug> bugs = bugService
				.findPagableListByCriteria(new SearchRequest<BugSearchCriteria>(
						null, 0, Integer.MAX_VALUE));

		assertThat(bugs.size()).isEqualTo(3);
		assertThat(bugs).extracting("id", "detail", "summary").contains(
				tuple(1, "detail 1", "summary 1"),
				tuple(2, "detail 2", "summary 2"),
				tuple(3, "detail 3", "summary 3"));
	}

	@DataSet
	@Test
	public void testSearchDefectsByUserCriteria() {
		BugSearchCriteria criteria = new BugSearchCriteria();
		criteria.setAssignuser(new StringSearchField("user1"));
		criteria.setLoguser(new StringSearchField("admin"));
		criteria.setSummary(new StringSearchField("summary"));
		criteria.setDetail(new StringSearchField("detail"));

		Assert.assertEquals(1, bugService.getTotalCount(criteria));
		Assert.assertEquals(
				1,
				bugService.findPagableListByCriteria(
						new SearchRequest<BugSearchCriteria>(criteria, 0,
								Integer.MAX_VALUE)).size());
	}

	@DataSet
	@Test
	public void testGetExtBug() {
		SimpleBug bug = bugService.findById(1, 1);
		Assert.assertEquals("Nguyen Hai", bug.getLoguserFullName());
		Assert.assertEquals("Nguyen Hai", bug.getAssignuserFullName());
		Assert.assertEquals(1, bug.getAffectedVersions().size());
		Assert.assertEquals(2, bug.getFixedVersions().size());
		Assert.assertEquals(1, bug.getComponents().size());
	}

	@DataSet
	@Test
	public void testSearchByComponents() {
		BugSearchCriteria criteria = new BugSearchCriteria();
		criteria.setComponentids(new SetSearchField<Integer>(1, 2));

		Assert.assertEquals(1, bugService.getTotalCount(criteria));
		Assert.assertEquals(
				1,
				bugService.findPagableListByCriteria(
						new SearchRequest<BugSearchCriteria>(criteria, 0,
								Integer.MAX_VALUE)).size());
	}

	@DataSet
	@Test
	public void testGetComponentDefectsSummary() {
		BugSearchCriteria criteria = new BugSearchCriteria();
		criteria.setProjectId(new NumberSearchField(1));
		bugService.getComponentDefectsSummary(criteria);
	}

	@DataSet
	@Test
	public void testSearchByVersions() {
		BugSearchCriteria criteria = new BugSearchCriteria();
		criteria.setFixedversionids(new SetSearchField<Integer>(1, 2, 3));
		criteria.setAffectedversionids(new SetSearchField<Integer>(1, 2, 3));

		Assert.assertEquals(1, bugService.getTotalCount(criteria));
		Assert.assertEquals(
				1,
				bugService.findPagableListByCriteria(
						new SearchRequest<BugSearchCriteria>(criteria, 0,
								Integer.MAX_VALUE)).size());
	}

	@DataSet
	@Test
	public void testSearchByVersions2() {
		// BugSearchCriteria criteria = new BugSearchCriteria();
		// criteria.setVersionids(new SetSearchField<Integer>(1, 2));
		//
		// Assert.assertEquals(1, bugService.getTotalCount(criteria));
		//
		// List<SimpleBug> bugList = (List<SimpleBug>) bugService
		// .findPagableListByCriteria(new SearchRequest<BugSearchCriteria>(
		// criteria, 0, Integer.MAX_VALUE));
		// Assert.assertEquals(1, bugList.size());
		// SimpleBug bug = bugList.get(0);
		// Assert.assertEquals(1, bug.getAffectedVersions().size());
		// Assert.assertEquals(2, bug.getFixedVersions().size());
	}

	@DataSet
	@Test
	public void testSearchByAssignedUser() {
		BugSearchCriteria criteria = new BugSearchCriteria();
		List<GroupItem> assignedDefectsSummary = bugService
				.getAssignedDefectsSummary(criteria);
		Assert.assertEquals(2, assignedDefectsSummary.size());
	}

	@DataSet
	@Test
	public void testSearchByDateCriteria2() {
		BugSearchCriteria criteria = new BugSearchCriteria();
		Calendar date = new GregorianCalendar();
		date.set(Calendar.YEAR, 2009);
		date.set(Calendar.MONTH, 0);
		date.set(Calendar.DAY_OF_MONTH, 2);

		criteria.setUpdatedDate(new DateSearchField(SearchField.AND, date
				.getTime()));
		Assert.assertEquals(0, bugService.getTotalCount(criteria));
		Assert.assertEquals(
				0,
				bugService.findPagableListByCriteria(
						new SearchRequest<BugSearchCriteria>(criteria, 0,
								Integer.MAX_VALUE)).size());
	}

	@DataSet
	@Test
	public void testBugStatus() {
		BugSearchCriteria criteria = new BugSearchCriteria();
		List<GroupItem> groupitems = bugService.getStatusSummary(criteria);
		Assert.assertEquals(1, groupitems.size());
	}

	@Test
	@DataSet
	public void testSaveBug() {
		BugWithBLOBs bug = new BugWithBLOBs();
		bug.setSummary("summary4");
		bug.setStatus("aaa");
		bug.setProjectid(1);
		bug.setSaccountid(1);
		int bugId = bugService.saveWithSession(bug, "admin");
		Assert.assertTrue((bugId > 0));

		System.out.println("bugid: " + bugId);

		bug = bugService.findById(bugId, 1);
		Assert.assertEquals("summary4", bug.getSummary());
	}
}

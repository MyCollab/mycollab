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
package com.esofthead.mycollab.module.project.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

import com.esofthead.mycollab.common.domain.GroupItem;
import com.esofthead.mycollab.core.arguments.DateSearchField;
import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.RangeDateSearchField;
import com.esofthead.mycollab.core.arguments.SearchField;
import com.esofthead.mycollab.core.arguments.SearchRequest;
import com.esofthead.mycollab.core.arguments.StringSearchField;
import com.esofthead.mycollab.module.project.domain.SimpleStandupReport;
import com.esofthead.mycollab.module.project.domain.criteria.StandupReportSearchCriteria;
import com.esofthead.mycollab.module.user.domain.SimpleUser;
import com.esofthead.mycollab.test.DataSet;
import com.esofthead.mycollab.test.MyCollabClassRunner;
import com.esofthead.mycollab.test.service.ServiceTest;

@RunWith(MyCollabClassRunner.class)
public class StandupReportServiceTest extends ServiceTest {
	@Autowired
	protected StandupReportService reportService;

	@SuppressWarnings("unchecked")
	@Test
	@DataSet
	public void gatherStandupList() {
		StandupReportSearchCriteria criteria = new StandupReportSearchCriteria();
		criteria.setProjectId(new NumberSearchField(1));
		criteria.setLogBy(new StringSearchField(SearchField.AND, "hainguyen"));
		Date d = new GregorianCalendar(2013, 2, 13).getTime();
		criteria.setOnDate(new DateSearchField(SearchField.AND, d));
		criteria.setSaccountid(new NumberSearchField(1));
		List<SimpleStandupReport> reports = reportService
				.findPagableListByCriteria(new SearchRequest<StandupReportSearchCriteria>(
						criteria, 0, Integer.MAX_VALUE));
		assertThat(reports.size()).isEqualTo(1);
		assertThat(reports).extracting("id", "logby", "whattoday").contains(
				tuple(1, "hainguyen", "a"));
	}

	@Test
	@DataSet
	public void testGetListCount() {
		StandupReportSearchCriteria criteria = new StandupReportSearchCriteria();
		criteria.setProjectId(new NumberSearchField(1));
		criteria.setSaccountid(new NumberSearchField(1));
		Date from = new GregorianCalendar(2013, 2, 1).getTime();
		Date to = new GregorianCalendar(2013, 2, 31).getTime();
		criteria.setReportDateRange(new RangeDateSearchField(from, to));
		List<GroupItem> reportsCount = reportService.getReportsCount(criteria);

		assertThat(reportsCount.size()).isEqualTo(2);
	}

	@Test
	@DataSet
	public void testFindUsersNotDoReportYet() {
		Date d = new GregorianCalendar(2013, 2, 13).getTime();
		List<SimpleUser> users = reportService.findUsersNotDoReportYet(1, d, 1);
		assertThat(users.size()).isEqualTo(1);
		assertThat(users.get(0).getUsername()).isEqualTo("linhduong");
	}
}

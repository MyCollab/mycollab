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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

import com.esofthead.mycollab.core.arguments.DateTimeSearchField;
import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchField;
import com.esofthead.mycollab.core.arguments.SearchRequest;
import com.esofthead.mycollab.module.crm.domain.SimpleActivity;
import com.esofthead.mycollab.module.crm.domain.criteria.ActivitySearchCriteria;
import com.esofthead.mycollab.test.DataSet;
import com.esofthead.mycollab.test.MyCollabClassRunner;
import com.esofthead.mycollab.test.service.ServiceTest;

@RunWith(MyCollabClassRunner.class)
public class EventServiceTest extends ServiceTest {

	@Autowired
	protected EventService eventService;

	@SuppressWarnings("unchecked")
	@DataSet
	@Test
	public void testSearchByCriteria() throws ParseException {
		ActivitySearchCriteria criteria = new ActivitySearchCriteria();
		criteria.setSaccountid(new NumberSearchField(1));

		List<SimpleActivity> list = eventService
				.findPagableListByCriteria(new SearchRequest<ActivitySearchCriteria>(
						criteria, 0, Integer.MAX_VALUE));

		assertThat(list.size()).isEqualTo(1);
		assertThat(list).extracting("id", "subject").contains(tuple(1, "aaa"));
	}

	@SuppressWarnings("unchecked")
	@DataSet
	@Test
	public void testSearchByTimeRange() throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date startDate = format.parse("2012-11-11 00:00:00");
		Date endDate = format.parse("2012-11-15 00:00:00");
		ActivitySearchCriteria criteria = new ActivitySearchCriteria();
		criteria.setStartDate(new DateTimeSearchField(SearchField.AND,
				DateTimeSearchField.GREATERTHANEQUAL, startDate));
		criteria.setEndDate(new DateTimeSearchField(SearchField.AND,
				DateTimeSearchField.LESSTHANEQUAL, endDate));
		criteria.setSaccountid(new NumberSearchField(1));

		List<SimpleActivity> list = eventService
				.findPagableListByCriteria(new SearchRequest<ActivitySearchCriteria>(
						criteria, 0, Integer.MAX_VALUE));
		assertThat(list.size()).isEqualTo(1);
		assertThat(list).extracting("id", "subject").contains(tuple(1, "aaa"));
	}
}

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
import com.mycollab.db.arguments.DateTimeSearchField;
import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.db.arguments.SearchField;
import com.mycollab.module.crm.domain.SimpleActivity;
import com.mycollab.module.crm.domain.criteria.ActivitySearchCriteria;
import com.mycollab.test.DataSet;
import com.mycollab.test.service.IntegrationServiceTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@RunWith(SpringJUnit4ClassRunner.class)
public class EventServiceTest extends IntegrationServiceTest {

    @Autowired
    protected EventService eventService;

    @DataSet
    @Test
    public void testSearchByCriteria() throws ParseException {
        ActivitySearchCriteria criteria = new ActivitySearchCriteria();
        criteria.setSaccountid(new NumberSearchField(1));

        List<SimpleActivity> list = eventService.findPageableListByCriteria(new BasicSearchRequest<>(criteria));
        assertThat(list.size()).isEqualTo(1);
        assertThat(list).extracting("id", "subject").contains(tuple(1, "aaa"));
    }

    @DataSet
    @Test
    public void testSearchByTimeRange() throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date startDate = format.parse("2012-11-11 00:00:00");
        Date endDate = format.parse("2012-11-15 00:00:00");
        ActivitySearchCriteria criteria = new ActivitySearchCriteria();
        criteria.setStartDate(new DateTimeSearchField(SearchField.AND, DateTimeSearchField.GREATER_THAN_EQUAL(), startDate));
        criteria.setEndDate(new DateTimeSearchField(SearchField.AND, DateTimeSearchField.LESS_THAN_EQUAL(), endDate));
        criteria.setSaccountid(new NumberSearchField(1));

        List<SimpleActivity> list = eventService.findPageableListByCriteria(new BasicSearchRequest<>(criteria, 0, Integer.MAX_VALUE));
        assertThat(list.size()).isEqualTo(1);
        assertThat(list).extracting("id", "subject").contains(tuple(1, "aaa"));
    }
}

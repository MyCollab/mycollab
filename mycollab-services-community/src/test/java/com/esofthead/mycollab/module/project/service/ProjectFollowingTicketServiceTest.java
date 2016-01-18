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

import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchRequest;
import com.esofthead.mycollab.core.arguments.SetSearchField;
import com.esofthead.mycollab.core.arguments.StringSearchField;
import com.esofthead.mycollab.module.project.domain.FollowingTicket;
import com.esofthead.mycollab.module.project.domain.criteria.FollowingTicketSearchCriteria;
import com.esofthead.mycollab.test.DataSet;
import com.esofthead.mycollab.test.service.IntergrationServiceTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@RunWith(SpringJUnit4ClassRunner.class)
public class ProjectFollowingTicketServiceTest extends IntergrationServiceTest {

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat(
            "yyyy-MM-dd hh:mm:ss");

    @Autowired
    private ProjectFollowingTicketService projectFollowingTicketService;

    private FollowingTicketSearchCriteria getCriteria() {
        FollowingTicketSearchCriteria criteria = new FollowingTicketSearchCriteria();
        criteria.setExtraTypeIds(new SetSearchField<>(1, 2));
        criteria.setSaccountid(new NumberSearchField(1));
        criteria.setUser(StringSearchField.and("hainguyen@esofthead.com"));
        return criteria;
    }

    @SuppressWarnings("unchecked")
    @DataSet
    @Test
    public void testGetListProjectFollowingTicket() throws ParseException {
        List<FollowingTicket> projectFollowingTickets = projectFollowingTicketService
                .findPagableListByCriteria(new SearchRequest<>(
                        getCriteria(), 0, Integer.MAX_VALUE));
        assertThat(projectFollowingTickets).extracting("type", "summary",
                "monitorDate").contains(
                tuple("Project-Task", "task 1", DATE_FORMAT.parse("2014-10-21 00:00:00")),
                tuple("Project-Task", "task 2", DATE_FORMAT.parse("2014-10-22 00:00:00")),
                tuple("Project-Bug", "bug 1", DATE_FORMAT.parse("2014-10-23 00:00:00")),
                tuple("Project-Bug", "bug 2", DATE_FORMAT.parse("2014-10-24 00:00:00")),
                tuple("Project-Task", "task 3", DATE_FORMAT.parse("2014-09-21 00:00:00")),
                tuple("Project-Task", "task 4", DATE_FORMAT.parse("2014-09-22 00:00:00")),
                tuple("Project-Bug", "bug 3", DATE_FORMAT.parse("2014-09-23 00:00:00")),
                tuple("Project-Bug", "bug 4", DATE_FORMAT.parse("2014-09-24 00:00:00")),
                tuple("Project-Problem", "problem 1", DATE_FORMAT.parse("2014-10-21 00:00:00")),
                tuple("Project-Problem", "problem 2", DATE_FORMAT.parse("2014-10-22 00:00:00")),
                tuple("Project-Risk", "risk 1", DATE_FORMAT.parse("2014-10-23 00:00:00")),
                tuple("Project-Risk", "risk 2", DATE_FORMAT.parse("2014-10-24 00:00:00")),
                tuple("Project-Problem", "problem 3", DATE_FORMAT.parse("2014-09-21 00:00:00")),
                tuple("Project-Problem", "problem 4", DATE_FORMAT.parse("2014-09-22 00:00:00")),
                tuple("Project-Risk", "risk 3", DATE_FORMAT.parse("2014-09-23 00:00:00")),
                tuple("Project-Risk", "risk 4", DATE_FORMAT.parse("2014-09-24 00:00:00")));
        assertThat(projectFollowingTickets.size()).isEqualTo(16);
    }

    @SuppressWarnings("unchecked")
    @DataSet
    @Test
    public void testGetListProjectFollowingTicketBySummary()
            throws ParseException {
        FollowingTicketSearchCriteria criteria = getCriteria();
        criteria.setSummary(StringSearchField.and("1"));
        List<FollowingTicket> projectFollowingTickets = projectFollowingTicketService
                .findPagableListByCriteria(new SearchRequest<>(criteria, 0, Integer.MAX_VALUE));
        assertThat(projectFollowingTickets).extracting("type", "summary", "monitorDate").contains(
                tuple("Project-Task", "task 1", DATE_FORMAT.parse("2014-10-21 00:00:00")),
                tuple("Project-Bug", "bug 1", DATE_FORMAT.parse("2014-10-23 00:00:00")),
                tuple("Project-Problem", "problem 1", DATE_FORMAT.parse("2014-10-21 00:00:00")),
                tuple("Project-Risk", "risk 1", DATE_FORMAT.parse("2014-10-23 00:00:00")));
        assertThat(projectFollowingTickets.size()).isEqualTo(4);
    }

    @SuppressWarnings("unchecked")
    @DataSet
    @Test
    public void testGetListProjectFollowingTicketOfTaskAndBug()
            throws ParseException {
        FollowingTicketSearchCriteria criteria = getCriteria();
        criteria.setTypes(new SetSearchField<>("Project-Task", "Project-Bug"));
        List<FollowingTicket> projectFollowingTickets = projectFollowingTicketService
                .findPagableListByCriteria(new SearchRequest<>(criteria, 0, Integer.MAX_VALUE));
        assertThat(projectFollowingTickets).extracting("type", "summary",
                "monitorDate").contains(
                tuple("Project-Task", "task 1", DATE_FORMAT.parse("2014-10-21 00:00:00")),
                tuple("Project-Task", "task 2", DATE_FORMAT.parse("2014-10-22 00:00:00")),
                tuple("Project-Task", "task 3", DATE_FORMAT.parse("2014-09-21 00:00:00")),
                tuple("Project-Task", "task 4", DATE_FORMAT.parse("2014-09-22 00:00:00")),
                tuple("Project-Bug", "bug 1", DATE_FORMAT.parse("2014-10-23 00:00:00")),
                tuple("Project-Bug", "bug 2", DATE_FORMAT.parse("2014-10-24 00:00:00")),
                tuple("Project-Bug", "bug 3", DATE_FORMAT.parse("2014-09-23 00:00:00")),
                tuple("Project-Bug", "bug 4", DATE_FORMAT.parse("2014-09-24 00:00:00")));
        assertThat(projectFollowingTickets.size()).isEqualTo(8);
    }

    @SuppressWarnings("unchecked")
    @DataSet
    @Test
    public void testGetListProjectFollowingTicketOfTask() throws ParseException {
        FollowingTicketSearchCriteria criteria = getCriteria();
        criteria.setType(StringSearchField.and("Project-Task"));
        List<FollowingTicket> projectFollowingTickets = projectFollowingTicketService
                .findPagableListByCriteria(new SearchRequest<>(criteria, 0, Integer.MAX_VALUE));
        assertThat(projectFollowingTickets).extracting("type", "summary",
                "monitorDate").contains(
                tuple("Project-Task", "task 1", DATE_FORMAT.parse("2014-10-21 00:00:00")),
                tuple("Project-Task", "task 2", DATE_FORMAT.parse("2014-10-22 00:00:00")),
                tuple("Project-Task", "task 3", DATE_FORMAT.parse("2014-09-21 00:00:00")),
                tuple("Project-Task", "task 4", DATE_FORMAT.parse("2014-09-22 00:00:00")));
        assertThat(projectFollowingTickets.size()).isEqualTo(4);
    }

    @SuppressWarnings("unchecked")
    @DataSet
    @Test
    public void testGetListProjectFollowingTicketOfRisk() throws ParseException {
        FollowingTicketSearchCriteria criteria = getCriteria();
        criteria.setType(StringSearchField.and("Project-Risk"));
        List<FollowingTicket> projectFollowingTickets = projectFollowingTicketService
                .findPagableListByCriteria(new SearchRequest<>(criteria, 0, Integer.MAX_VALUE));

        assertThat(projectFollowingTickets).extracting("type", "summary",
                "monitorDate").contains(
                tuple("Project-Risk", "risk 1", DATE_FORMAT.parse("2014-10-23 00:00:00")),
                tuple("Project-Risk", "risk 2", DATE_FORMAT.parse("2014-10-24 00:00:00")),
                tuple("Project-Risk", "risk 3", DATE_FORMAT.parse("2014-09-23 00:00:00")),
                tuple("Project-Risk", "risk 4", DATE_FORMAT.parse("2014-09-24 00:00:00")));
        assertThat(projectFollowingTickets.size()).isEqualTo(4);
    }

    @SuppressWarnings("unchecked")
    @DataSet
    @Test
    public void testGetListProjectFollowingTicketOfProblem()
            throws ParseException {
        FollowingTicketSearchCriteria criteria = getCriteria();
        criteria.setType(StringSearchField.and("Project-Problem"));
        List<FollowingTicket> projectFollowingTickets = projectFollowingTicketService
                .findPagableListByCriteria(new SearchRequest<>(criteria, 0, Integer.MAX_VALUE));

        assertThat(projectFollowingTickets).extracting("type", "summary",
                "monitorDate").contains(
                tuple("Project-Problem", "problem 1", DATE_FORMAT.parse("2014-10-21 00:00:00")),
                tuple("Project-Problem", "problem 2", DATE_FORMAT.parse("2014-10-22 00:00:00")),
                tuple("Project-Problem", "problem 3", DATE_FORMAT.parse("2014-09-21 00:00:00")),
                tuple("Project-Problem", "problem 4", DATE_FORMAT.parse("2014-09-22 00:00:00")));
        assertThat(projectFollowingTickets.size()).isEqualTo(4);
    }

    @SuppressWarnings("unchecked")
    @DataSet
    @Test
    public void testGetListProjectFollowingTicketOfBug() throws ParseException {
        FollowingTicketSearchCriteria criteria = getCriteria();
        criteria.setType(StringSearchField.and("Project-Bug"));
        List<FollowingTicket> projectFollowingTickets = projectFollowingTicketService
                .findPagableListByCriteria(new SearchRequest<>(criteria, 0, Integer.MAX_VALUE));

        assertThat(projectFollowingTickets).extracting("type", "summary",
                "monitorDate").contains(
                tuple("Project-Bug", "bug 1", DATE_FORMAT.parse("2014-10-23 00:00:00")),
                tuple("Project-Bug", "bug 2", DATE_FORMAT.parse("2014-10-24 00:00:00")),
                tuple("Project-Bug", "bug 3", DATE_FORMAT.parse("2014-09-23 00:00:00")),
                tuple("Project-Bug", "bug 4", DATE_FORMAT.parse("2014-09-24 00:00:00")));
        assertThat(projectFollowingTickets.size()).isEqualTo(4);
    }

    @DataSet
    @Test
    public void testGetTotalCount() throws ParseException {
        assertThat(projectFollowingTicketService.getTotalCount(getCriteria())).isEqualTo(16);
    }
}

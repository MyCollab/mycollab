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
import com.esofthead.mycollab.core.arguments.BasicSearchRequest;
import com.esofthead.mycollab.core.arguments.SetSearchField;
import com.esofthead.mycollab.core.arguments.StringSearchField;
import com.esofthead.mycollab.module.project.domain.SimpleMilestone;
import com.esofthead.mycollab.module.project.domain.criteria.MilestoneSearchCriteria;
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
public class MilestoneServiceTest extends IntergrationServiceTest {

    private static final DateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    @Autowired
    protected MilestoneService itemTimeLoggingService;

    private MilestoneSearchCriteria getCriteria() {
        MilestoneSearchCriteria criteria = new MilestoneSearchCriteria();
        criteria.setSaccountid(new NumberSearchField(1));
        criteria.setProjectIds(new SetSearchField<>(1));
        return criteria;
    }

    @SuppressWarnings("unchecked")
    @DataSet
    @Test
    public void testGetListMilestones() throws ParseException {
        MilestoneSearchCriteria criteria = new MilestoneSearchCriteria();
        criteria.setSaccountid(new NumberSearchField(1));
        criteria.setProjectIds(new SetSearchField<>(1));
        criteria.setStatuses(new SetSearchField<>("Open"));
        criteria.setMilestoneName(StringSearchField.and("milestone 1"));

        List<SimpleMilestone> milestones = itemTimeLoggingService.findPagableListByCriteria(new BasicSearchRequest<>(criteria, 0, Integer.MAX_VALUE));

        assertThat(milestones.size()).isEqualTo(1);
        assertThat(milestones).extracting("id", "description", "createdUserFullName", "createdtime", "ownerFullName",
                "numTasks", "numOpenTasks", "numBugs", "numOpenBugs").contains(
                tuple(1, "milestone no1", "Hai Nguyen", dateformat.parse("2014-10-01 00:00:00"), "Hai Nguyen", 1, 0,
                        2, 2));
    }

    @SuppressWarnings("unchecked")
    @DataSet
    @Test
    public void testGetListMilestonesByCriteria() throws ParseException {
        List<SimpleMilestone> milestones = itemTimeLoggingService
                .findPagableListByCriteria(new BasicSearchRequest<>(getCriteria(), 0, Integer.MAX_VALUE));

        assertThat(milestones.size()).isEqualTo(4);
        assertThat(milestones).extracting("id", "description",
                "createdUserFullName", "createdtime", "ownerFullName",
                "numTasks", "numOpenTasks", "numBugs", "numOpenBugs").contains(
                tuple(4, "milestone no4", "Nghiem Le", dateformat.parse("2014-10-04 00:00:00"), "Nghiem Le", 0, 0, 3, 3),
                tuple(3, "milestone no3", "Nghiem Le", dateformat.parse("2014-10-03 00:00:00"), "Nghiem Le", 0, 0, 1, 1),
                tuple(2, "milestone no2", "Hai Nguyen", dateformat.parse("2014-10-02 00:00:00"), "Hai Nguyen", 2, 0, 0, 0),
                tuple(1, "milestone no1", "Hai Nguyen", dateformat.parse("2014-10-01 00:00:00"), "Hai Nguyen", 1, 0, 2, 2));
    }

    @DataSet
    @Test
    public void testFindMilestoneById() throws ParseException {
        SimpleMilestone milestone = itemTimeLoggingService.findById(1, 1);
        assertThat(milestone.getCreatedUserFullName()).isEqualTo("Hai Nguyen");
        assertThat(milestone.getNumOpenBugs()).isEqualTo(2);
    }

    @DataSet
    @Test
    public void testgetTotalCount() {
        int milestoneSize = itemTimeLoggingService.getTotalCount(getCriteria());
        assertThat(milestoneSize).isEqualTo(4);
    }
}

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
package com.mycollab.common.service;

import com.mycollab.common.domain.SimpleActivityStream;
import com.mycollab.common.domain.criteria.ActivityStreamSearchCriteria;
import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.db.arguments.BasicSearchRequest;
import com.mycollab.db.arguments.SetSearchField;
import com.mycollab.test.DataSet;
import com.mycollab.test.service.IntegrationServiceTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@RunWith(SpringJUnit4ClassRunner.class)
public class ActivityStreamServiceTest extends IntegrationServiceTest {

    @Autowired
    protected ActivityStreamService activityStreamService;

    @Test
    @DataSet
    public void testSearchActivityStreams() {
        ActivityStreamSearchCriteria searchCriteria = new ActivityStreamSearchCriteria();
        searchCriteria.setModuleSet(new SetSearchField<>("aa", "bb"));
        searchCriteria.setSaccountid(new NumberSearchField(1));

        List<SimpleActivityStream> activities = activityStreamService.findPageableListByCriteria(new BasicSearchRequest<>(searchCriteria, 0,
                Integer.MAX_VALUE));
        assertThat(activities.size()).isEqualTo(3);
    }

    @Test
    @DataSet
    public void testQueryActivityWithComments() {
        ActivityStreamSearchCriteria searchCriteria = new ActivityStreamSearchCriteria();
        searchCriteria.setModuleSet(new SetSearchField<>("bb"));
        searchCriteria.setSaccountid(new NumberSearchField(1));

        List<SimpleActivityStream> activities = activityStreamService
                .findPageableListByCriteria(new BasicSearchRequest<>(searchCriteria, 0, Integer.MAX_VALUE));

        assertThat(activities.size()).isEqualTo(1);
        assertThat(activities).extracting("saccountid", "module", "action").contains(tuple(1, "bb", "update"));
    }
}

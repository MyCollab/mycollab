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

import com.mycollab.module.crm.domain.SimpleCase;
import com.mycollab.module.crm.domain.criteria.CaseSearchCriteria;
import com.mycollab.test.DataSet;
import com.mycollab.test.service.IntergrationServiceTest;
import com.mycollab.db.arguments.BasicSearchRequest;
import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.db.arguments.SetSearchField;
import com.mycollab.db.arguments.StringSearchField;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@RunWith(SpringJUnit4ClassRunner.class)
public class CaseServiceTest extends IntergrationServiceTest {

    @Autowired
    protected CaseService caseService;

    @DataSet
    @Test
    public void testGetAll() {
        CaseSearchCriteria criteria = new CaseSearchCriteria();
        criteria.setSaccountid(new NumberSearchField(1));

        List<SimpleCase> cases = caseService.findPagableListByCriteria(new BasicSearchRequest<>(null, 0, Integer.MAX_VALUE));

        assertThat(cases.size()).isEqualTo(2);
        assertThat(cases).extracting("id", "subject", "status").contains(tuple(1, "abc", "New"), tuple(2, "a", "Test Status"));
    }

    @DataSet
    @Test
    public void testGetSearchCriteria() {
        CaseSearchCriteria criteria = new CaseSearchCriteria();
        criteria.setAccountId(new NumberSearchField(1));
        criteria.setAssignUser(StringSearchField.and("admin"));
        criteria.setSubject(StringSearchField.and("a"));
        criteria.setSaccountid(new NumberSearchField(1));

        List<SimpleCase> cases = caseService.findPagableListByCriteria(new BasicSearchRequest<>(null, 0, Integer.MAX_VALUE));

        assertThat(cases.size()).isEqualTo(2);
        assertThat(cases).extracting("id", "subject", "status").contains(tuple(1, "abc", "New"), tuple(2, "a", "Test Status"));
    }

    @Test
    @DataSet
    public void testSearchAssignUsers() {
        CaseSearchCriteria criteria = new CaseSearchCriteria();
        criteria.setAssignUsers(new SetSearchField<>("linh", "admin"));
        criteria.setSaccountid(new NumberSearchField(1));

        List<SimpleCase> cases = caseService.findPagableListByCriteria(new BasicSearchRequest<>(null, 0, Integer.MAX_VALUE));

        assertThat(cases.size()).isEqualTo(2);
        assertThat(cases).extracting("id", "subject", "status").contains(tuple(1, "abc", "New"), tuple(2, "a", "Test Status"));
    }

    @Test
    @DataSet
    public void testSearchPriorities() {
        CaseSearchCriteria criteria = new CaseSearchCriteria();
        criteria.setPriorities(new SetSearchField<>("High", "Medium"));
        criteria.setSaccountid(new NumberSearchField(1));

        List<SimpleCase> cases = caseService.findPagableListByCriteria(new BasicSearchRequest<>(null, 0, Integer.MAX_VALUE));

        assertThat(cases.size()).isEqualTo(2);
        assertThat(cases).extracting("id", "subject", "status").contains(tuple(1, "abc", "New"), tuple(2, "a", "Test Status"));
    }

    @Test
    @DataSet
    public void testSearchStatuses() {
        CaseSearchCriteria criteria = new CaseSearchCriteria();
        criteria.setStatuses(new SetSearchField<>("New", "Test Status"));
        criteria.setSaccountid(new NumberSearchField(1));

        List<SimpleCase> cases = caseService.findPagableListByCriteria(new BasicSearchRequest<>(null, 0, Integer.MAX_VALUE));

        assertThat(cases.size()).isEqualTo(2);
        assertThat(cases).extracting("id", "subject", "status").contains(tuple(1, "abc", "New"), tuple(2, "a", "Test Status"));
    }
}

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

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchRequest;
import com.esofthead.mycollab.core.arguments.StringSearchField;
import com.esofthead.mycollab.module.project.domain.SimpleProblem;
import com.esofthead.mycollab.module.project.domain.criteria.ProblemSearchCriteria;
import com.esofthead.mycollab.test.DataSet;
import com.esofthead.mycollab.test.service.IntergrationServiceTest;

@RunWith(SpringJUnit4ClassRunner.class)
public class ProblemServiceTest extends IntergrationServiceTest {

	@Autowired
	protected ProblemService problemService;

	@SuppressWarnings("unchecked")
	@DataSet
	@Test
	public void testGetListIssues() {
		List<SimpleProblem> problems = problemService
				.findPagableListByCriteria(new SearchRequest<ProblemSearchCriteria>(
						null, 0, Integer.MAX_VALUE));
		assertThat(problems.size()).isEqualTo(3);
		assertThat(problems).extracting("id", "issuename").contains(
				tuple(1, "a"), tuple(2, "b"), tuple(3, "c"));
	}

	@SuppressWarnings("unchecked")
	@DataSet
	@Test
	public void testSearchIssuesByName() {
		ProblemSearchCriteria criteria = new ProblemSearchCriteria();
		criteria.setProblemname(new StringSearchField(StringSearchField.AND,
				"a"));
		criteria.setSaccountid(new NumberSearchField(1));
		List<SimpleProblem> problems = problemService
				.findPagableListByCriteria(new SearchRequest<>(
						criteria, 0, Integer.MAX_VALUE));
		assertThat(problems.size()).isEqualTo(1);
		assertThat(problems).extracting("id", "issuename").contains(
				tuple(1, "a"));
	}

	@SuppressWarnings("unchecked")
	@DataSet
	@Test
	public void testgetTotalCount() {
		ProblemSearchCriteria criteria = new ProblemSearchCriteria();
		criteria.setSaccountid(null);
		criteria.setProblemname(new StringSearchField(StringSearchField.AND,
				"a"));
		List<SimpleProblem> problems = problemService
				.findPagableListByCriteria(new SearchRequest<>(
						criteria, 0, Integer.MAX_VALUE));
		assertThat(problems.size()).isEqualTo(1);
		assertThat(problems).extracting("id", "issuename").contains(
				tuple(1, "a"));
	}
}

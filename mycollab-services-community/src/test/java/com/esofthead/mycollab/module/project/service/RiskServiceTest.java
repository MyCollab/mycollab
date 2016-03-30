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
import com.esofthead.mycollab.module.project.domain.Risk;
import com.esofthead.mycollab.module.project.domain.SimpleRisk;
import com.esofthead.mycollab.module.project.domain.criteria.RiskSearchCriteria;
import com.esofthead.mycollab.test.DataSet;
import com.esofthead.mycollab.test.service.IntergrationServiceTest;

@RunWith(SpringJUnit4ClassRunner.class)
public class RiskServiceTest extends IntergrationServiceTest {

	@Autowired
	protected RiskService riskService;

	@SuppressWarnings("unchecked")
	@DataSet
	@Test
	public void testGetListRisks() {
		List<SimpleRisk> risks = riskService
				.findPagableListByCriteria(new SearchRequest<RiskSearchCriteria>(
						null, 0, Integer.MAX_VALUE));

		assertThat(risks.size()).isEqualTo(3);
		assertThat(risks).extracting("id", "riskname").contains(tuple(1, "a"),
				tuple(2, "ab"), tuple(3, "c"));
	}

	@SuppressWarnings("unchecked")
	@DataSet
	@Test
	public void testSearchRisksByName() {
		RiskSearchCriteria criteria = new RiskSearchCriteria();
		criteria.setRiskname(StringSearchField.and("a"));
		criteria.setSaccountid(new NumberSearchField(1));
		List<SimpleRisk> risks = riskService.findPagableListByCriteria(new SearchRequest<>(
						criteria, 0, Integer.MAX_VALUE));

		assertThat(risks.size()).isEqualTo(2);
		assertThat(risks).extracting("id", "riskname").contains(tuple(1, "a"),
				tuple(2, "ab"));
	}

	@DataSet
	@Test
	public void testInsertAndReturnKey() {
		Risk record = new Risk();
		record.setProjectid(1);
		record.setRiskname("New projectMember");
		record.setDescription("aaa");
		record.setSaccountid(1);
		int newId = riskService.saveWithSession(record, "hainguyen");

		Risk risk = riskService.findByPrimaryKey(newId, 1);
		assertThat(risk.getRiskname()).isEqualTo("New projectMember");
	}
}

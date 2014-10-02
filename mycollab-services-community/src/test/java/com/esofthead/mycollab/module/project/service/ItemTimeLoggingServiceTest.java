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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

import com.esofthead.mycollab.core.arguments.BooleanSearchField;
import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchField;
import com.esofthead.mycollab.core.arguments.SearchRequest;
import com.esofthead.mycollab.core.arguments.SetSearchField;
import com.esofthead.mycollab.module.project.domain.ItemTimeLogging;
import com.esofthead.mycollab.module.project.domain.criteria.ItemTimeLoggingSearchCriteria;
import com.esofthead.mycollab.test.DataSet;
import com.esofthead.mycollab.test.MyCollabClassRunner;
import com.esofthead.mycollab.test.service.ServiceTest;

@RunWith(MyCollabClassRunner.class)
public class ItemTimeLoggingServiceTest extends ServiceTest {

	private static final DateFormat DF = new SimpleDateFormat(
			"yyyy-MM-dd hh:mm:ss");

	@Autowired
	protected ItemTimeLoggingService itemTimeLoggingService;

	private ItemTimeLoggingSearchCriteria getCriteria() {
		ItemTimeLoggingSearchCriteria criteria = new ItemTimeLoggingSearchCriteria();
		criteria.setSaccountid(new NumberSearchField(1));
		criteria.setIsBillable(new BooleanSearchField(false));
		criteria.setLogUsers(new SetSearchField<String>(SearchField.AND,
				new String[]{"hai79", "nghiemle"}));
		return criteria;
	}

	@SuppressWarnings("unchecked")
	@DataSet
	@Test
	public void testGetListItemTimeLoggings() throws ParseException {
		List<ItemTimeLogging> itemTimeLoggings = itemTimeLoggingService
				.findPagableListByCriteria(new SearchRequest<ItemTimeLoggingSearchCriteria>(
						getCriteria(), 0, Integer.MAX_VALUE));

		assertThat(itemTimeLoggings.size()).isEqualTo(2);
		assertThat(itemTimeLoggings).extracting("id", "type", "logforday",
				"loguser", "summary").contains(
				tuple(4, null, DF.parse("2014-04-19 13:29:23"),
						"hai79", ""),
				tuple(2, "Project-Bug", DF.parse("2014-06-10 13:29:23"),
						"nghiemle", "summary 2"));
	}

	@SuppressWarnings("unchecked")
	@DataSet
	@Test
	public void testgetTotalCount() {
		List<ItemTimeLogging> itemTimeLoggings = itemTimeLoggingService
				.findPagableListByCriteria(new SearchRequest<ItemTimeLoggingSearchCriteria>(
						getCriteria(), 0, Integer.MAX_VALUE));
		assertThat(itemTimeLoggings.size()).isEqualTo(2);
	}
}

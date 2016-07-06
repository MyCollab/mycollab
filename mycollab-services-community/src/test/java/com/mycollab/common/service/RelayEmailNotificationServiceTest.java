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

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.mycollab.common.domain.SimpleRelayEmailNotification;
import com.mycollab.common.domain.criteria.RelayEmailNotificationSearchCriteria;
import com.mycollab.db.arguments.BasicSearchRequest;

//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = {"classpath:META-INF/spring/service-context-test.xml"})
public class RelayEmailNotificationServiceTest {
	@Autowired
	protected RelayEmailNotificationService relayEmailNotificationService;

	//@Test
	//@DataSet
	@SuppressWarnings("unchecked")
	public void testRemoveItems() {
		RelayEmailNotificationSearchCriteria criteria = new RelayEmailNotificationSearchCriteria();
		List<SimpleRelayEmailNotification> items = relayEmailNotificationService
				.findPagableListByCriteria(new BasicSearchRequest<RelayEmailNotificationSearchCriteria>(
						criteria, 0, Integer.MAX_VALUE));
		assertThat(items.size()).isEqualTo(1);
	}
}

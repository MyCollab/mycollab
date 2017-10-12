/**
 * Copyright © MyCollab
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.form.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.mycollab.test.spring.IntegrationServiceTest;

@RunWith(SpringJUnit4ClassRunner.class)
public class MasterFormServiceTest extends IntegrationServiceTest {

	@Autowired
	private MasterFormService masterFormService;

//	@DataSet
	@Test
	public void testGetForm() {
//		DynaForm form = masterFormService.findCustomForm(1, "Account");
//		Assert.assertNotNull(form);
	}
}

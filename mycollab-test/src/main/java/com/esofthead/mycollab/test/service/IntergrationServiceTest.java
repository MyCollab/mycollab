/**
 * This file is part of mycollab-test.
 *
 * mycollab-test is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-test is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-test.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.esofthead.mycollab.test.service;

import com.esofthead.mycollab.spring.test.RootConfigurationTest;
import org.junit.ClassRule;
import org.junit.Rule;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import com.esofthead.mycollab.test.rule.DbUnitInitializerRule;
import com.esofthead.mycollab.test.rule.EssentialInitRule;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 */

@ContextConfiguration(classes = {RootConfigurationTest.class})
@ActiveProfiles(profiles = "test")
public class IntergrationServiceTest {
	@ClassRule
	public static final EssentialInitRule essentialRule = new EssentialInitRule();

	@Rule
	public DbUnitInitializerRule dbRule = new DbUnitInitializerRule();
}

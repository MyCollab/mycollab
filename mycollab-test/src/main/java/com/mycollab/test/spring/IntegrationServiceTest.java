/**
 * Copyright © MyCollab
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.test.spring;

import com.mycollab.configuration.ApplicationConfiguration;
import com.mycollab.configuration.ServerConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@ContextConfiguration(classes = RootConfigurationTest.class)
@ActiveProfiles(profiles = "test")
public class IntegrationServiceTest {

    @MockBean
    private ApplicationConfiguration applicationConfiguration;

    @MockBean
    private ServerConfiguration serverConfiguration;

}

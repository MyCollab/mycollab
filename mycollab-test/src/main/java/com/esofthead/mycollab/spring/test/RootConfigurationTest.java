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
package com.esofthead.mycollab.spring.test;

import org.springframework.context.annotation.*;
import org.springframework.context.annotation.aspectj.EnableSpringConfigured;

/**
 * @author MyCollab Ltd.
 * @since 4.6.0
 */
@Configuration
@EnableSpringConfigured
@EnableAspectJAutoProxy
@ComponentScan(basePackages = {"com.esofthead.mycollab"}, includeFilters = {@ComponentScan.Filter(type = FilterType
        .ASPECTJ, pattern = "com.esofthead.mycollab..service.*"), @ComponentScan.Filter(type = FilterType.ASPECTJ,
        pattern = "com.esofthead.mycollab..dao.*"), @ComponentScan.Filter(type = FilterType.ASPECTJ,
        pattern = "com.esofthead.mycollab..service.*Test"), @ComponentScan.Filter(type = FilterType.ASPECTJ,
        pattern = "com.esofthead.mycollab.schedule.email.*"), @ComponentScan.Filter(type = FilterType.REGEX, pattern = "test.com.esofthead.mycollab.service.*")})
@ImportResource(value = {"classpath:META-INF/spring-test/ecm-context-test.xml",
        "classpath:META-INF/spring-test/page-context-test.xml"})
@Profile("test")
public class RootConfigurationTest {

}

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
package com.esofthead.mycollab.spring.test.service;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.aspectj.EnableSpringConfigured;
import org.springframework.stereotype.Controller;

/**
 * @author MyCollab Ltd.
 * @since 4.6.0
 */
@Configuration
@EnableSpringConfigured
@EnableAspectJAutoProxy
@ComponentScan(basePackages = {"com.esofthead.mycollab.**.service", "com.esofthead.mycollab.**.spring",
        "com.esofthead.mycollab.**.jobs", "com.esofthead.mycollab.**.aspect", "com.esofthead.mycollab.**.esb"},
        excludeFilters = {@ComponentScan.Filter(classes = {Controller.class})})
@Profile("test")
public class RootConfigurationTest {

}

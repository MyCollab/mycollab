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
package com.esofthead.mycollab.test.rule;

import java.net.URL;
import java.util.TimeZone;

import org.apache.log4j.PropertyConfigurator;
import org.joda.time.DateTimeZone;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import com.esofthead.mycollab.test.service.IntergrationServiceTest;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.5.2
 *
 */
public class EssentialInitRule implements TestRule {

	@Override
	public Statement apply(Statement base, Description description) {
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
		DateTimeZone.setDefault(DateTimeZone.UTC);

		URL resourceUrl = IntergrationServiceTest.class.getClassLoader().getResource(
				"log4j-test.properties");
		if (resourceUrl != null) {
			PropertyConfigurator.configure(resourceUrl);
		}
		return base;
	}

}

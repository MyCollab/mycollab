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
package com.esofthead.mycollab.test;

/**
 * MyCollab test class can be tested against external entities like database,
 * ldap, email server etc or combine several of them. MyCollabTestModule provide
 * the plugin module for MyCollab test class that they can add several supported
 * module that helps the process of set up and destroy external entities without
 * impacting to test code.
 * 
 * The following code depicts how to make the integration test against database:
 * <code>
 * public class ATest {
 *     public ATest () {
 *         addTestModule(new DbUnitModule());
 *     }
 * 
 *     public void testXyz() {
 *         //do the test against database
 *     }
 * }
 * </code>
 * 
 * @author Hai Nguyen (hainguyen@esofthead.com)
 * @since 1.0
 */
public interface MyCollabTestModule {
	/**
	 * Assign the test host to this module. Depend on the functionality of this
	 * module, it get some variables from this host like data source, test
	 * configurations etc and serves for setUp and tearDown actions.
	 * 
	 * @param host
	 *            The test class attach to this module
	 */
	void setHost(Class<?> host);

	/**
	 * Init the module. Note that this method is invoked after the test host
	 * init the testing.
	 */
	void setUp();

	/**
	 * Clean up the testing includes resources or extra configuration settings.
	 * This method is invoked after the test host clean up testing.
	 */
	void tearDown();
}

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

import java.util.ArrayList;
import java.util.List;

import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.esofthead.mycollab.test.module.DbUnitModule;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public class MyCollabClassRunner extends SpringJUnit4ClassRunner {

	private List<MyCollabTestModule> testModules;

	public MyCollabClassRunner(Class<?> clazz) throws InitializationError {
		super(clazz);
	}

	@Override
	protected Statement methodInvoker(FrameworkMethod method, Object test) {
		preInvokeMethod(method);
		Statement methodInvoker = super.methodInvoker(method, test);
		postInvokeMethod(method);
		return methodInvoker;
	}

	private void preInvokeMethod(FrameworkMethod method) {
		testModules = new ArrayList<MyCollabTestModule>();
		DataSet dataSetAnno = method.getAnnotation(DataSet.class);
		if (dataSetAnno != null) {
			DbUnitModule dbModule = new DbUnitModule();
			dbModule.setHost(this.getTestClass().getJavaClass());
			testModules.add(dbModule);
		}

		for (MyCollabTestModule module : testModules) {
			module.setUp();
		}
	}

	private void postInvokeMethod(FrameworkMethod method) {
		for (MyCollabTestModule module : testModules) {
			module.tearDown();
		}
	}
}

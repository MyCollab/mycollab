/**
 * This file is part of mycollab-mobile.
 *
 * mycollab-mobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-mobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-mobile.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.mobile.module.project.view.parameters;

import com.esofthead.mycollab.common.i18n.OptionI18nEnum.StatusI18nEnum;
import com.esofthead.mycollab.mobile.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.domain.SimpleTaskList;
import com.esofthead.mycollab.module.project.domain.criteria.TaskListSearchCriteria;
import com.esofthead.mycollab.vaadin.mvp.ScreenData;

/**
 * @author MyCollab Ltd.
 *
 * @since 4.5.0
 *
 */
public class TaskGroupScreenData {

	public static class List extends ScreenData<TaskListSearchCriteria> {
		public List() {
			super(null);
		}

		public List(TaskListSearchCriteria param) {
			super(param);
		}

	}

	public static class Add extends ScreenData<SimpleTaskList> {
		public Add() {
			super(null);
			SimpleTaskList taskList = new SimpleTaskList();
			taskList.setProjectid(CurrentProjectVariables.getProjectId());
			taskList.setStatus(StatusI18nEnum.Open.name());
			this.setParams(taskList);
		}
	}

	public static class Read extends ScreenData<Integer> {
		public Read(Integer param) {
			super(param);
		}
	}

	public static class Edit extends ScreenData<SimpleTaskList> {

		public Edit(SimpleTaskList params) {
			super(params);
		}

	}
}

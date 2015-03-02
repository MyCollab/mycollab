/**
 * This file is part of mycollab-web.
 *
 * mycollab-web is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-web is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-web.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.module.project.view.task;

import com.esofthead.mycollab.module.project.domain.SimpleTask;
import com.esofthead.mycollab.module.project.domain.criteria.TaskSearchCriteria;
import com.esofthead.mycollab.vaadin.events.HasSearchHandlers;
import com.esofthead.mycollab.vaadin.mvp.PageView;
import com.esofthead.mycollab.vaadin.ui.table.IPagedBeanTable;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.0.0
 *
 */
public interface TaskSearchView extends PageView {
	public static final String VIEW_DEF_ID = "project-task-list";

	HasSearchHandlers<TaskSearchCriteria> getSearchHandlers();

	IPagedBeanTable<TaskSearchCriteria, SimpleTask> getPagedBeanTable();

	void moveToAdvanceSearch();

	void moveToBasicSearch();

	void setSearchInputValue(String value);
}

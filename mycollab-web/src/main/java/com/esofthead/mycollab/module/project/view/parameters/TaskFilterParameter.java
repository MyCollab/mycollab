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
package com.esofthead.mycollab.module.project.view.parameters;

import com.esofthead.mycollab.module.project.domain.criteria.TaskSearchCriteria;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.0
 * 
 */
public class TaskFilterParameter {

	private TaskSearchCriteria searchCriteria;
	
	private String screenTitle;
	
	private boolean advanceSearch;
	
	public boolean getAdvanceSearch() {
		return advanceSearch;
	}

	public void setAdvanceSearch(boolean advanceSearch) {
		this.advanceSearch = advanceSearch;
	}

	public TaskFilterParameter(TaskSearchCriteria searchCriteria, String screenTitle) {
		this.searchCriteria = searchCriteria;
		this.screenTitle = screenTitle;
		this.advanceSearch = false;
	}

	public TaskSearchCriteria getSearchCriteria() {
		return searchCriteria;
	}

	public void setSearchCriteria(TaskSearchCriteria searchCriteria) {
		this.searchCriteria = searchCriteria;
	}

	public String getScreenTitle() {
		return screenTitle;
	}

	public void setScreenTitle(String screenTitle) {
		this.screenTitle = screenTitle;
	}

}

/**
 * This file is part of mycollab-services.
 *
 * mycollab-services is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-services is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-services.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.module.project.domain.criteria;

import com.esofthead.mycollab.core.arguments.BooleanSearchField;
import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchCriteria;
import com.esofthead.mycollab.core.arguments.StringSearchField;

public class RiskSearchCriteria extends SearchCriteria {
	private StringSearchField riskname;

	private StringSearchField raisedByUser;

	private StringSearchField assignToUser;

	private BooleanSearchField isCompleted;

	private NumberSearchField projectId;
	
	 private NumberSearchField id;

	public StringSearchField getRiskname() {
		return riskname;
	}

	public void setRiskname(StringSearchField riskname) {
		this.riskname = riskname;
	}

	public NumberSearchField getProjectId() {
		return projectId;
	}

	public void setProjectId(NumberSearchField projectId) {
		this.projectId = projectId;
	}

	public StringSearchField getRaisedByUser() {
		return raisedByUser;
	}

	public void setRaisedByUser(StringSearchField raisedByUser) {
		this.raisedByUser = raisedByUser;
	}

	public StringSearchField getAssignToUser() {
		return assignToUser;
	}

	public void setAssignToUser(StringSearchField assignToUser) {
		this.assignToUser = assignToUser;
	}

	public BooleanSearchField getIsCompleted() {
		return isCompleted;
	}

	public void setIsCompleted(BooleanSearchField isCompleted) {
		this.isCompleted = isCompleted;
	}

	public void setId(NumberSearchField id) {
		this.id = id;
	}

	public NumberSearchField getId() {
		return id;
	}
}

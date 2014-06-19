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

import java.util.Arrays;

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchCriteria;
import com.esofthead.mycollab.core.arguments.StringSearchField;
import com.esofthead.mycollab.core.db.query.DateParam;
import com.esofthead.mycollab.core.db.query.Param;
import com.esofthead.mycollab.core.db.query.PropertyListParam;
import com.esofthead.mycollab.core.db.query.StringListParam;
import com.esofthead.mycollab.module.project.i18n.ProblemI18nEnum;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public class ProblemSearchCriteria extends SearchCriteria {
	private static final long serialVersionUID = 1L;

	public static Param p_assignee = new PropertyListParam(
			"problem-assignuser", GenericI18Enum.FORM_ASSIGNEE,
			"m_prj_problem", "assigntouser");

	public static Param p_raisedUser = new PropertyListParam(
			"problem-raiseduser", ProblemI18nEnum.FORM_RAISED_BY,
			"m_prj_problem", "raisedbyuser");

	public static Param p_status = new StringListParam("problem-status",
			ProblemI18nEnum.FORM_STATUS, "m_prj_problem", "status",
			Arrays.asList("Open", "Closed"));

	public static Param p_priority = new StringListParam("problem-priority",
			ProblemI18nEnum.FORM_PRIORITY, "m_prj_problem", "priority",
			Arrays.asList("High", "Medium", "Low"));

	public static Param p_duedate = new DateParam("problem-duedate",
			ProblemI18nEnum.FORM_DATE_DUE, "m_prj_problem", "datedue");

	public static Param p_raiseddate = new DateParam("problem-raiseddate",
			GenericI18Enum.FORM_CREATED_TIME, "m_prj_problem", "dateraised");

	private StringSearchField problemname;
	private NumberSearchField projectId;
	private StringSearchField raisedByUser;
	private StringSearchField assignToUser;
	private NumberSearchField id;

	public StringSearchField getProblemname() {
		return problemname;
	}

	public void setProblemname(StringSearchField problemname) {
		this.problemname = problemname;
	}

	public NumberSearchField getProjectId() {
		return projectId;
	}

	public void setProjectId(NumberSearchField projectId) {
		this.projectId = projectId;
	}

	public StringSearchField getAssignToUser() {
		return assignToUser;
	}

	public void setAssignToUser(StringSearchField assignToUser) {
		this.assignToUser = assignToUser;
	}

	public StringSearchField getRaisedByUser() {
		return raisedByUser;
	}

	public void setRaisedByUser(StringSearchField raisedByUser) {
		this.raisedByUser = raisedByUser;
	}

	public void setId(NumberSearchField id) {
		this.id = id;
	}

	public NumberSearchField getId() {
		return id;
	}
}

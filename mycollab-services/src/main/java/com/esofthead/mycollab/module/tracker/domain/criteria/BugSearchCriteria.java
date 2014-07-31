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
package com.esofthead.mycollab.module.tracker.domain.criteria;

import java.util.Arrays;

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.core.arguments.DateSearchField;
import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.RangeDateTimeSearchField;
import com.esofthead.mycollab.core.arguments.SearchCriteria;
import com.esofthead.mycollab.core.arguments.SetSearchField;
import com.esofthead.mycollab.core.arguments.StringSearchField;
import com.esofthead.mycollab.core.db.query.CompositionStringParam;
import com.esofthead.mycollab.core.db.query.DateParam;
import com.esofthead.mycollab.core.db.query.Param;
import com.esofthead.mycollab.core.db.query.PropertyListParam;
import com.esofthead.mycollab.core.db.query.StringListParam;
import com.esofthead.mycollab.core.db.query.StringParam;
import com.esofthead.mycollab.module.project.ProjectDataTypeFactory;
import com.esofthead.mycollab.module.project.i18n.BugI18nEnum;
import com.esofthead.mycollab.module.project.i18n.OptionI18nEnum.BugPriority;
import com.esofthead.mycollab.module.project.i18n.OptionI18nEnum.BugStatus;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public class BugSearchCriteria extends SearchCriteria {
	private static final long serialVersionUID = 1L;

	public static Param p_textDesc = new CompositionStringParam("bug-textDesc",
			BugI18nEnum.FORM_ANY_TEXT, new StringParam[] {
					new StringParam("", null, "m_tracker_bug", "summary"),
					new StringParam("", null, "m_tracker_bug", "detail") });

	public static Param p_createdtime = new DateParam("bug-createdtime",
			GenericI18Enum.FORM_CREATED_TIME, "m_tracker_bug", "createdTime");

	public static Param p_lastupdatedtime = new DateParam(
			"bug-lastupdatedtime", GenericI18Enum.FORM_LAST_UPDATED_TIME,
			"m_tracker_bug", "lastUpdatedTime");

	public static Param p_resolveddate = new DateParam("bug-resolveddate",
			BugI18nEnum.FORM_RESOLVED_DATE, "m_tracker_bug", "resolveddate");

	public static Param p_duedate = new DateParam("bug-duedate",
			BugI18nEnum.FORM_DUE_DATE, "m_tracker_bug", "duedate");

	public static Param p_priority = new StringListParam("bug-priority",
			BugI18nEnum.FORM_PRIORITY, "m_tracker_bug", "priority",
			Arrays.asList(BugPriority.Blocker.name(),
					BugPriority.Critical.name(), BugPriority.Major.name(),
					BugPriority.Minor.name(), BugPriority.Trivial.name()));

	public static Param p_severity = new StringListParam("bug-severity",
			BugI18nEnum.FORM_SEVERITY, "m_tracker_bug", "severity",
			Arrays.asList(ProjectDataTypeFactory.getBugSeverityList()));

	public static Param p_status = new StringListParam("bug-status",
			BugI18nEnum.FORM_STATUS, "m_tracker_bug", "status", Arrays.asList(
					BugStatus.Verified.name(), BugStatus.InProgress.name(),
					BugStatus.Open.name(), BugStatus.ReOpened.name(),
					BugStatus.Resolved.name()));

	public static Param p_assignee = new PropertyListParam("bug-assignuser",
			GenericI18Enum.FORM_ASSIGNEE, "m_tracker_bug", "assignuser");

	private StringSearchField assignuser;

	private StringSearchField loguser;

	private DateSearchField updatedDate;

	private RangeDateTimeSearchField updatedDateRange;

	private DateSearchField dueDate;

	private RangeDateTimeSearchField dueDateRange;

	private DateSearchField resolvedDate;

	private RangeDateTimeSearchField resolvedDateRange;

	private StringSearchField summary;

	private StringSearchField description;

	private StringSearchField detail;

	private StringSearchField environment;

	private SetSearchField<String> resolutions;

	private SetSearchField<Integer> componentids;

	private SetSearchField<Integer> affectedversionids;

	private SetSearchField<Integer> fixedversionids;

	private SetSearchField<Integer> versionids;

	private SetSearchField<Integer> milestoneIds;

	private SetSearchField<String> priorities;

	private SetSearchField<String> severities;

	private SetSearchField<String> statuses;

	private NumberSearchField projectId;

	public static String AFFVERSION = "AffVersion";
	public static String FIXVERSION = "FixVersion";
	public static String COMPONENT = "Component";

	public StringSearchField getAssignuser() {
		return assignuser;
	}

	public void setAssignuser(StringSearchField assignuser) {
		this.assignuser = assignuser;
	}

	public StringSearchField getLoguser() {
		return loguser;
	}

	public void setLoguser(StringSearchField loguser) {
		this.loguser = loguser;
	}

	public StringSearchField getSummary() {
		return summary;
	}

	public void setSummary(StringSearchField summary) {
		this.summary = summary;
	}

	public StringSearchField getDetail() {
		return detail;
	}

	public void setDetail(StringSearchField detail) {
		this.detail = detail;
	}

	public StringSearchField getEnvironment() {
		return environment;
	}

	public void setEnvironment(StringSearchField environment) {
		this.environment = environment;
	}

	public SetSearchField<String> getResolutions() {
		return resolutions;
	}

	public void setResolutions(SetSearchField<String> resolutions) {
		this.resolutions = resolutions;
	}

	public SetSearchField<Integer> getComponentids() {
		return componentids;
	}

	public void setComponentids(SetSearchField<Integer> componentids) {
		this.componentids = componentids;
	}

	public SetSearchField<Integer> getAffectedversionids() {
		return affectedversionids;
	}

	public void setAffectedversionids(SetSearchField<Integer> affectedversionids) {
		this.affectedversionids = affectedversionids;
	}

	public SetSearchField<Integer> getFixedversionids() {
		return fixedversionids;
	}

	public void setFixedversionids(SetSearchField<Integer> fixedversionids) {
		this.fixedversionids = fixedversionids;
	}

	public SetSearchField<Integer> getVersionids() {
		return versionids;
	}

	public void setVersionids(SetSearchField<Integer> versionids) {
		this.versionids = versionids;
	}

	public DateSearchField getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(DateSearchField updatedDate) {
		this.updatedDate = updatedDate;
	}

	public DateSearchField getResolvedDate() {
		return resolvedDate;
	}

	public void setResolvedDate(DateSearchField resolvedDate) {
		this.resolvedDate = resolvedDate;
	}

	public RangeDateTimeSearchField getUpdatedDateRange() {
		return updatedDateRange;
	}

	public void setUpdatedDateRange(RangeDateTimeSearchField updatedDateRange) {
		this.updatedDateRange = updatedDateRange;
	}

	public DateSearchField getDueDate() {
		return dueDate;
	}

	public void setDueDate(DateSearchField dueDate) {
		this.dueDate = dueDate;
	}

	public RangeDateTimeSearchField getDueDateRange() {
		return dueDateRange;
	}

	public void setDueDateRange(RangeDateTimeSearchField dueDateRange) {
		this.dueDateRange = dueDateRange;
	}

	public RangeDateTimeSearchField getResolvedDateRange() {
		return resolvedDateRange;
	}

	public void setResolvedDateRange(RangeDateTimeSearchField resolvedDateRange) {
		this.resolvedDateRange = resolvedDateRange;
	}

	public SetSearchField<String> getPriorities() {
		return priorities;
	}

	public void setPriorities(SetSearchField<String> priorities) {
		this.priorities = priorities;
	}

	public SetSearchField<String> getSeverities() {
		return severities;
	}

	public void setSeverities(SetSearchField<String> severities) {
		this.severities = severities;
	}

	public SetSearchField<String> getStatuses() {
		return statuses;
	}

	public void setStatuses(SetSearchField<String> statuses) {
		this.statuses = statuses;
	}

	public void setProjectId(NumberSearchField projectId) {
		this.projectId = projectId;
	}

	public NumberSearchField getProjectId() {
		return projectId;
	}

	public void setDescription(StringSearchField description) {
		this.description = description;
	}

	public StringSearchField getDescription() {
		return description;
	}

	public SetSearchField<Integer> getMilestoneIds() {
		return milestoneIds;
	}

	public void setMilestoneIds(SetSearchField<Integer> milestoneIds) {
		this.milestoneIds = milestoneIds;
	}
}

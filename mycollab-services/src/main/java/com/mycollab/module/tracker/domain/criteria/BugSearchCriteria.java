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
package com.mycollab.module.tracker.domain.criteria;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.db.arguments.*;
import com.mycollab.db.query.*;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.i18n.BugI18nEnum;
import com.mycollab.module.project.i18n.MilestoneI18nEnum;
import com.mycollab.module.project.i18n.OptionI18nEnum.BugSeverity;
import com.mycollab.module.project.i18n.OptionI18nEnum.BugStatus;
import com.mycollab.module.project.i18n.OptionI18nEnum.Priority;
import com.mycollab.module.project.i18n.ProjectCommonI18nEnum;
import org.apache.ibatis.jdbc.SQL;

import java.util.Arrays;
import java.util.Collection;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class BugSearchCriteria extends SearchCriteria {
    private static final long serialVersionUID = 1L;

    public static final CompositionStringParam p_textDesc = CacheParamMapper.register(ProjectTypeConstants.BUG, BugI18nEnum.FORM_ANY_TEXT,
            new CompositionStringParam("textDesc",
                    new StringParam("", "m_tracker_bug", "name"),
                    new StringParam("", "m_tracker_bug", "detail"),
                    new StringParam("", "m_tracker_bug", "environment")));

    public static final Param p_createdtime = CacheParamMapper.register(ProjectTypeConstants.BUG, GenericI18Enum.FORM_CREATED_TIME,
            new DateParam("createdtime", "m_tracker_bug", "createdTime"));

    public static final DateParam p_lastupdatedtime = CacheParamMapper.register(ProjectTypeConstants.BUG, GenericI18Enum.FORM_LAST_UPDATED_TIME,
            new DateParam("lastupdatedtime", "m_tracker_bug", "lastUpdatedTime"));

    public static final DateParam p_resolveddate = CacheParamMapper.register(ProjectTypeConstants.BUG, BugI18nEnum.FORM_RESOLVED_DATE,
            new DateParam("resolveddate", "m_tracker_bug", "resolveddate"));

    public static final DateParam p_createddate = CacheParamMapper.register(ProjectTypeConstants.BUG, GenericI18Enum.FORM_CREATED_TIME,
            new DateParam("createdTime", "m_tracker_bug", "createdTime"));

    public static final DateParam p_duedate = CacheParamMapper.register(ProjectTypeConstants.BUG, GenericI18Enum.FORM_DUE_DATE,
            new DateParam("dueDate", "m_tracker_bug", "dueDate"));

    public static final NumberParam p_bugkey = CacheParamMapper.register(ProjectTypeConstants.BUG, BugI18nEnum.FORM_BUG_KEY,
            new NumberParam("key", "m_tracker_bug", "bugkey"));

    public static final PropertyListParam<Integer> p_milestones = CacheParamMapper.register(ProjectTypeConstants.BUG, MilestoneI18nEnum.SINGLE,
            new PropertyListParam<Integer>("milestones", "m_tracker_bug", "milestoneId"));

    public static final I18nStringListParam p_priority = CacheParamMapper.register(ProjectTypeConstants.BUG, GenericI18Enum.FORM_PRIORITY,
            new I18nStringListParam("priority", "m_tracker_bug", "priority",
                    Arrays.asList(Priority.Urgent, Priority.High, Priority.Medium,
                            Priority.Low, Priority.None)));

    public static final I18nStringListParam p_severity = CacheParamMapper.register(ProjectTypeConstants.BUG, BugI18nEnum.FORM_SEVERITY,
            new I18nStringListParam("severity", "m_tracker_bug", "severity",
                    Arrays.asList(BugSeverity.Critical, BugSeverity.Major, BugSeverity.Minor, BugSeverity.Trivial)));

    public static final I18nStringListParam p_status = CacheParamMapper.register(ProjectTypeConstants.BUG, GenericI18Enum.FORM_STATUS,
            new I18nStringListParam("status", "m_tracker_bug", "status",
                    Arrays.asList(BugStatus.Verified, BugStatus.Open, BugStatus.ReOpen, BugStatus.Resolved)));

    public static final BugTypeCustomSqlParam p_affectedVersions = CacheParamMapper.register(ProjectTypeConstants.BUG, BugI18nEnum.FORM_AFFECTED_VERSIONS,
            new BugTypeCustomSqlParam("affected_versions", "AffVersion"));

    public static final BugTypeCustomSqlParam p_fixedVersions = CacheParamMapper.register(ProjectTypeConstants.BUG, BugI18nEnum.FORM_FIXED_VERSIONS,
            new BugTypeCustomSqlParam("fixed_versions", "FixVersion"));

    public static final BugTypeCustomSqlParam p_components = CacheParamMapper.register(ProjectTypeConstants.BUG, BugI18nEnum.FORM_COMPONENTS,
            new BugTypeCustomSqlParam("components", "Component"));

    public static final PropertyListParam<String> p_assignee = CacheParamMapper.register(ProjectTypeConstants.BUG, GenericI18Enum.FORM_ASSIGNEE,
            new PropertyListParam<String>("assignUser", "m_tracker_bug", "assignUser"));

    public static final PropertyListParam<String> p_createdUser = CacheParamMapper.register(ProjectTypeConstants.BUG, ProjectCommonI18nEnum.ITEM_CREATED_PEOPLE,
            new PropertyListParam<String>("createdUser", "m_tracker_bug", "createdUser"));

    public static final PropertyListParam p_projectIds = CacheParamMapper.register(ProjectTypeConstants.BUG, null,
            new PropertyListParam("projectId", "m_tracker_bug", "projectId"));

    private static class BugTypeCustomSqlParam extends CustomSqlParam {
        private String type;

        BugTypeCustomSqlParam(String id, String type) {
            super(id);
            this.type = type;
        }

        @Override
        public NoValueSearchField buildPropertyParamInList(String oper, Collection<?> values) {
            if (values == null || values.size() == 0) {
                return null;
            }
            StringBuilder sqlResult = new StringBuilder();
            Object[] array = values.toArray();
            for (int i = 0; i < array.length; i++) {
                final Integer affectedVersion = Integer.parseInt(array[i].toString());
                String result = new SQL() {
                    {
                        SELECT("COUNT(*)");
                        FROM("m_tracker_bug_related_item");
                        WHERE(String.format("m_tracker_bug_related_item.type='%s'", type));
                        AND();
                        WHERE(String.format("m_tracker_bug_related_item.typeid=%d", affectedVersion));
                        AND();
                        WHERE("m_tracker_bug_related_item.bugid=m_tracker_bug.id");
                    }
                }.toString();
                sqlResult.append("(").append(result).append(") > 0");
                if (i < array.length - 1) {
                    sqlResult.append(" OR ");
                }
            }

            if (array.length > 1) {
                sqlResult.insert(0, '(');
                sqlResult.append(')');
            }

            return new NoValueSearchField(oper, sqlResult.toString());
        }

        @Override
        public NoValueSearchField buildPropertyParamNotInList(String oper, Collection<?> values) {
            if (values == null || values.size() == 0) {
                return null;
            }
            StringBuilder sqlResult = new StringBuilder();
            Object[] array = values.toArray();
            for (int i = 0; i < array.length; i++) {
                final Object affectedVersion = Integer.parseInt(array[i].toString());
                String result = new SQL() {
                    {
                        SELECT("COUNT(*)");
                        FROM("m_tracker_bug_related_item");
                        WHERE(String.format("m_tracker_bug_related_item.type='%s'", type));
                        AND();
                        WHERE(String.format("m_tracker_bug_related_item.typeid=%d", affectedVersion));
                        AND();
                        WHERE("m_tracker_bug_related_item.bugid=m_tracker_bug.id");
                    }
                }.toString();
                sqlResult.append("(").append(result).append(") = 0");
                if (i < array.length - 1) {
                    sqlResult.append(" AND ");
                }
            }

            if (array.length > 1) {
                sqlResult.insert(0, '(');
                sqlResult.append(')');
            }
            return new NoValueSearchField(oper, sqlResult.toString());
        }

    }

    private StringSearchField assignuser;
    private StringSearchField loguser;
    private DateSearchField updatedDate;

    private StringSearchField name;
    private StringSearchField description;
    private StringSearchField detail;
    private StringSearchField environment;
    private SetSearchField<String> resolutions;

    private SetSearchField<Integer> componentids;
    private SetSearchField<Integer> affectedversionids;
    private SetSearchField<Integer> fixedversionids;
    private SetSearchField<Integer> versionids;
    private SetSearchField<String> priorities;
    private SetSearchField<String> statuses;
    private NumberSearchField projectId;

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

    public StringSearchField getName() {
        return name;
    }

    public void setName(StringSearchField name) {
        this.name = name;
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

    public SetSearchField<String> getPriorities() {
        return priorities;
    }

    public void setPriorities(SetSearchField<String> priorities) {
        this.priorities = priorities;
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
}

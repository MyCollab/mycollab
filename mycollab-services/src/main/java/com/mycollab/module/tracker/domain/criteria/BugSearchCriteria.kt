/**
 * Copyright © MyCollab
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http:></http:>//www.gnu.org/licenses/>.
 */
package com.mycollab.module.tracker.domain.criteria

import com.mycollab.common.i18n.GenericI18Enum
import com.mycollab.common.i18n.OptionI18nEnum.StatusI18nEnum
import com.mycollab.db.arguments.*
import com.mycollab.db.query.*
import com.mycollab.module.project.ProjectTypeConstants
import com.mycollab.module.project.i18n.BugI18nEnum
import com.mycollab.module.project.i18n.MilestoneI18nEnum
import com.mycollab.module.project.i18n.OptionI18nEnum.BugSeverity
import com.mycollab.module.project.i18n.OptionI18nEnum.Priority
import com.mycollab.module.project.i18n.ProjectCommonI18nEnum
import org.apache.ibatis.jdbc.SQL
import java.util.*

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
class BugSearchCriteria : SearchCriteria() {

    var assignuser: StringSearchField? = null
    var loguser: StringSearchField? = null
    var updatedDate: DateSearchField? = null

    var name: StringSearchField? = null
    var description: StringSearchField? = null
    var detail: StringSearchField? = null
    var environment: StringSearchField? = null
    var resolutions: SetSearchField<String>? = null

    var componentids: SetSearchField<Int>? = null
    var affectedversionids: SetSearchField<Int>? = null
    var fixedversionids: SetSearchField<Int>? = null
    var versionids: SetSearchField<Int>? = null
    var priorities: SetSearchField<String>? = null
    var statuses: SetSearchField<String>? = null
    var projectId: NumberSearchField? = null

    class BugTypeCustomSqlParam internal constructor(id: String, private val type: String) : CustomSqlParam(id) {

        override fun buildPropertyParamInList(oper: String, values: Collection<String>?): NoValueSearchField? {
            if (values == null || values.isEmpty()) {
                return null
            }
            val sqlResult = StringBuilder()
            val array = values.toTypedArray()
            for (i in array.indices) {
                val affectedVersion = Integer.parseInt(array[i])
                val result = object : SQL() {
                    init {
                        SELECT("COUNT(*)")
                        FROM("m_tracker_bug_related_item")
                        WHERE("m_tracker_bug_related_item.type='$type'")
                        AND()
                        WHERE("m_tracker_bug_related_item.typeid=$affectedVersion")
                        AND()
                        WHERE("m_tracker_bug_related_item.bugid=m_tracker_bug.id")
                    }
                }.toString()
                sqlResult.append("(").append(result).append(") > 0")
                if (i < array.size - 1) {
                    sqlResult.append(" OR ")
                }
            }

            if (array.size > 1) {
                sqlResult.insert(0, '(')
                sqlResult.append(')')
            }

            return NoValueSearchField(oper, sqlResult.toString())
        }

        override fun buildPropertyParamNotInList(oper: String, values: Collection<String>?): NoValueSearchField? {
            if (values == null || values.isEmpty()) {
                return null
            }
            val sqlResult = StringBuilder()
            val array = values.toTypedArray()
            for (i in array.indices) {
                val affectedVersion = Integer.parseInt(array[i])
                val result = object : SQL() {
                    init {
                        SELECT("COUNT(*)")
                        FROM("m_tracker_bug_related_item")
                        WHERE("m_tracker_bug_related_item.type='$type'")
                        AND()
                        WHERE("m_tracker_bug_related_item.typeid=$affectedVersion")
                        AND()
                        WHERE("m_tracker_bug_related_item.bugid=m_tracker_bug.id")
                    }
                }.toString()
                sqlResult.append("(").append(result).append(") = 0")
                if (i < array.size - 1) {
                    sqlResult.append(" AND ")
                }
            }

            if (array.size > 1) {
                sqlResult.insert(0, '(')
                sqlResult.append(')')
            }
            return NoValueSearchField(oper, sqlResult.toString())
        }

    }

    companion object {

        @JvmField
        val p_textDesc = CacheParamMapper.register(ProjectTypeConstants.BUG, BugI18nEnum.FORM_ANY_TEXT,
                CompositionStringParam("textDesc",
                        StringParam("", "m_tracker_bug", "name"),
                        StringParam("", "m_tracker_bug", "detail"),
                        StringParam("", "m_tracker_bug", "environment")))

        @JvmField
        val p_createdtime = CacheParamMapper.register(ProjectTypeConstants.BUG, GenericI18Enum.FORM_CREATED_TIME,
                DateParam("createdtime", "m_tracker_bug", "createdTime"))

        @JvmField
        val p_lastupdatedtime = CacheParamMapper.register(ProjectTypeConstants.BUG, GenericI18Enum.FORM_LAST_UPDATED_TIME,
                DateParam("lastupdatedtime", "m_tracker_bug", "lastUpdatedTime"))

        @JvmField
        val p_resolveddate = CacheParamMapper.register(ProjectTypeConstants.BUG, BugI18nEnum.FORM_RESOLVED_DATE,
                DateParam("resolveddate", "m_tracker_bug", "resolveddate"))

        @JvmField
        val p_createddate = CacheParamMapper.register(ProjectTypeConstants.BUG, GenericI18Enum.FORM_CREATED_TIME,
                DateParam("createdTime", "m_tracker_bug", "createdTime"))

        @JvmField
        val p_duedate = CacheParamMapper.register(ProjectTypeConstants.BUG, GenericI18Enum.FORM_DUE_DATE,
                DateParam("dueDate", "m_tracker_bug", "dueDate"))

        @JvmField
        val p_bugkey = CacheParamMapper.register(ProjectTypeConstants.BUG, BugI18nEnum.FORM_BUG_KEY,
                NumberParam("key", "m_tracker_bug", "bugkey"))

        @JvmField
        val p_milestones = CacheParamMapper.register(ProjectTypeConstants.BUG, MilestoneI18nEnum.SINGLE,
                PropertyListParam<Int>("milestones", "m_tracker_bug", "milestoneId"))

        @JvmField
        val p_priority = CacheParamMapper.register(ProjectTypeConstants.BUG, GenericI18Enum.FORM_PRIORITY,
                I18nStringListParam("priority", "m_tracker_bug", "priority",
                        Arrays.asList(Priority.Urgent, Priority.High, Priority.Medium,
                                Priority.Low, Priority.None)))

        @JvmField
        val p_severity = CacheParamMapper.register(ProjectTypeConstants.BUG, BugI18nEnum.FORM_SEVERITY,
                I18nStringListParam("severity", "m_tracker_bug", "severity",
                        Arrays.asList(BugSeverity.Critical, BugSeverity.Major, BugSeverity.Minor, BugSeverity.Trivial)))
        @JvmField
        val p_status = CacheParamMapper.register(ProjectTypeConstants.BUG, GenericI18Enum.FORM_STATUS,
                I18nStringListParam("status", "m_tracker_bug", "status",
                        Arrays.asList(StatusI18nEnum.Verified, StatusI18nEnum.Open, StatusI18nEnum.ReOpen, StatusI18nEnum.Resolved)))

        @JvmField
        val p_affectedVersions = CacheParamMapper.register(ProjectTypeConstants.BUG, BugI18nEnum.FORM_AFFECTED_VERSIONS,
                BugTypeCustomSqlParam("affected_versions", "AffVersion"))

        @JvmField
        val p_fixedVersions = CacheParamMapper.register(ProjectTypeConstants.BUG, BugI18nEnum.FORM_FIXED_VERSIONS,
                BugTypeCustomSqlParam("fixed_versions", "FixVersion"))

        @JvmField
        val p_components = CacheParamMapper.register(ProjectTypeConstants.BUG, BugI18nEnum.FORM_COMPONENTS,
                BugTypeCustomSqlParam("components", "Component"))

        @JvmField
        val p_assignee = CacheParamMapper.register(ProjectTypeConstants.BUG, GenericI18Enum.FORM_ASSIGNEE,
                PropertyListParam<String>("assignUser", "m_tracker_bug", "assignUser"))

        @JvmField
        val p_createdUser = CacheParamMapper.register(ProjectTypeConstants.BUG, ProjectCommonI18nEnum.ITEM_CREATED_PEOPLE,
                PropertyListParam<String>("createdUser", "m_tracker_bug", "createdUser"))

        @JvmField
        val p_projectIds = CacheParamMapper.register(ProjectTypeConstants.BUG, null,
                PropertyListParam<Int>("projectId", "m_tracker_bug", "projectId"))
    }
}

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
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.project.ui.format

import com.mycollab.common.i18n.GenericI18Enum
import com.mycollab.common.i18n.OptionI18nEnum.StatusI18nEnum
import com.mycollab.core.utils.HumanTime
import com.mycollab.core.utils.StringUtils
import com.mycollab.module.project.domain.Task
import com.mycollab.module.project.i18n.MilestoneI18nEnum
import com.mycollab.module.project.i18n.OptionI18nEnum.Priority
import com.mycollab.module.project.i18n.TaskI18nEnum
import com.mycollab.vaadin.UserUIContext
import com.mycollab.vaadin.ui.formatter.FieldGroupFormatter
import com.mycollab.vaadin.ui.formatter.HistoryFieldFormat
import com.mycollab.vaadin.ui.formatter.I18nHistoryFieldFormat
import org.slf4j.LoggerFactory

/**
 * @author MyCollab Ltd
 * @since 5.1.4
 */
class TaskFieldFormatter private constructor() : FieldGroupFormatter() {

    init {
        generateFieldDisplayHandler(Task.Field.name.name, GenericI18Enum.FORM_NAME)
        generateFieldDisplayHandler(Task.Field.startdate.name, GenericI18Enum.FORM_START_DATE, FieldGroupFormatter.DATETIME_FIELD)
        generateFieldDisplayHandler(Task.Field.enddate.name, GenericI18Enum.FORM_END_DATE, FieldGroupFormatter.DATETIME_FIELD)
        generateFieldDisplayHandler(Task.Field.duedate.name, GenericI18Enum.FORM_DUE_DATE, FieldGroupFormatter.DATETIME_FIELD)
        generateFieldDisplayHandler(Task.Field.priority.name, GenericI18Enum.FORM_PRIORITY,
                I18nHistoryFieldFormat(Priority::class.java))
        generateFieldDisplayHandler(Task.Field.status.name, GenericI18Enum.FORM_STATUS,
                I18nHistoryFieldFormat(StatusI18nEnum::class.java))
        generateFieldDisplayHandler(Task.Field.isestimated.name, TaskI18nEnum.FORM_IS_ESTIMATED)
        generateFieldDisplayHandler(Task.Field.remainestimate.name, TaskI18nEnum.FORM_REMAIN_ESTIMATE)
        generateFieldDisplayHandler(Task.Field.originalestimate.name, TaskI18nEnum.FORM_ORIGINAL_ESTIMATE)
        generateFieldDisplayHandler(Task.Field.assignuser.name, GenericI18Enum.FORM_ASSIGNEE, ProjectMemberHistoryFieldFormat())
        generateFieldDisplayHandler(Task.Field.milestoneid.name, MilestoneI18nEnum.SINGLE, MilestoneHistoryFieldFormat())
        generateFieldDisplayHandler(Task.Field.percentagecomplete.name, TaskI18nEnum.FORM_PERCENTAGE_COMPLETE)
        generateFieldDisplayHandler(Task.Field.parenttaskid.name, TaskI18nEnum.FORM_PARENT_TASK, TaskHistoryFieldFormat())
        generateFieldDisplayHandler(Task.Field.description.name, GenericI18Enum.FORM_DESCRIPTION, FieldGroupFormatter.TRIM_HTMLS)
        generateFieldDisplayHandler(Task.Field.duration.name, GenericI18Enum.FORM_DURATION, DurationFieldFormat())
    }

    private class DurationFieldFormat : HistoryFieldFormat {

        override fun toString(value: String): String =
                toString(value, true, UserUIContext.getMessage(GenericI18Enum.FORM_EMPTY))

        override fun toString(value: String, displayAsHtml: Boolean, msgIfBlank: String): String =
                when {
                    StringUtils.isNotBlank(value) -> try {
                        val duration = java.lang.Long.parseLong(value)
                        val humanTime = HumanTime(duration)
                        humanTime.exactly
                    } catch (e: Exception) {
                        LOG.error("Parse value failed $value", e)
                        msgIfBlank
                    }
                    else -> msgIfBlank
                }
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(TaskFieldFormatter::class.java)
        private val _instance = TaskFieldFormatter()

        fun instance(): TaskFieldFormatter = _instance
    }
}

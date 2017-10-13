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
import com.mycollab.module.project.CurrentProjectVariables
import com.mycollab.module.project.ProjectLinkBuilder
import com.mycollab.module.project.ProjectTypeConstants
import com.mycollab.module.project.domain.SimpleTask
import com.mycollab.module.project.service.ProjectTaskService
import com.mycollab.spring.AppContextUtil
import com.mycollab.vaadin.AppUI
import com.mycollab.vaadin.UserUIContext
import com.mycollab.vaadin.ui.formatter.HistoryFieldFormat
import org.apache.commons.lang3.StringUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * @author MyCollab Ltd
 * @since 5.2.9
 */
class TaskHistoryFieldFormat : HistoryFieldFormat {

    override fun toString(value: String): String {
        return toString(value, true, UserUIContext.getMessage(GenericI18Enum.FORM_EMPTY))
    }

    override fun toString(value: String, displayAsHtml: Boolean, msgIfBlank: String): String {
        if (StringUtils.isBlank(value)) {
            return msgIfBlank
        }

        try {
            val taskId = Integer.parseInt(value)
            val taskService = AppContextUtil.getSpringBean(ProjectTaskService::class.java)
            val task = taskService.findById(taskId, AppUI.accountId)

            return if (task != null) {
                if (displayAsHtml) {
                    ProjectLinkBuilder.generateProjectItemHtmlLinkAndTooltip(CurrentProjectVariables.shortName,
                            task.projectid!!, task.name, ProjectTypeConstants.TASK, task.id!!.toString() + "")
                } else {
                    task.name
                }
            } else {
                "Deleted task"
            }
        } catch (e: Exception) {
            LOG.error("Error", e)
        }

        return value
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(TaskHistoryFieldFormat::class.java)
    }
}

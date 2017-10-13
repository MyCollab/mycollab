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
import com.mycollab.module.project.domain.Risk
import com.mycollab.module.project.i18n.MilestoneI18nEnum
import com.mycollab.module.project.i18n.OptionI18nEnum.RiskConsequence
import com.mycollab.module.project.i18n.OptionI18nEnum.RiskProbability
import com.mycollab.module.project.i18n.RiskI18nEnum
import com.mycollab.vaadin.ui.formatter.FieldGroupFormatter
import com.mycollab.vaadin.ui.formatter.I18nHistoryFieldFormat

/**
 * @author MyCollab Ltd.
 * @since 4.3.3
 */
class RiskFieldFormatter : FieldGroupFormatter() {
    init {
        this.generateFieldDisplayHandler(Risk.Field.name.name, GenericI18Enum.FORM_NAME)
        this.generateFieldDisplayHandler(Risk.Field.description.name, GenericI18Enum.FORM_DESCRIPTION, FieldGroupFormatter.TRIM_HTMLS)
        this.generateFieldDisplayHandler(Risk.Field.createduser.name, RiskI18nEnum.FORM_RAISED_BY, ProjectMemberHistoryFieldFormat())
        this.generateFieldDisplayHandler(Risk.Field.assignuser.name, GenericI18Enum.FORM_ASSIGNEE, ProjectMemberHistoryFieldFormat())
        this.generateFieldDisplayHandler(Risk.Field.consequence.name, RiskI18nEnum.FORM_CONSEQUENCE,
                I18nHistoryFieldFormat(RiskConsequence::class.java))
        this.generateFieldDisplayHandler(Risk.Field.duedate.name, GenericI18Enum.FORM_DUE_DATE, FieldGroupFormatter.DATE_FIELD)
        this.generateFieldDisplayHandler(Risk.Field.startdate.name, GenericI18Enum.FORM_START_DATE, FieldGroupFormatter.DATE_FIELD)
        this.generateFieldDisplayHandler(Risk.Field.enddate.name, GenericI18Enum.FORM_END_DATE, FieldGroupFormatter.DATE_FIELD)
        this.generateFieldDisplayHandler(Risk.Field.milestoneid.name, MilestoneI18nEnum.SINGLE, MilestoneHistoryFieldFormat())
        this.generateFieldDisplayHandler(Risk.Field.probalitity.name, RiskI18nEnum.FORM_PROBABILITY,
                I18nHistoryFieldFormat(RiskProbability::class.java))
        this.generateFieldDisplayHandler(Risk.Field.status.name, GenericI18Enum.FORM_STATUS,
                I18nHistoryFieldFormat(StatusI18nEnum::class.java))
        this.generateFieldDisplayHandler(Risk.Field.priority.name, GenericI18Enum.FORM_PRIORITY)
        this.generateFieldDisplayHandler(Risk.Field.response.name, RiskI18nEnum.FORM_RESPONSE, FieldGroupFormatter.TRIM_HTMLS)
    }

    companion object {
        private val _instance = RiskFieldFormatter()

        fun instance(): RiskFieldFormatter {
            return _instance
        }
    }
}

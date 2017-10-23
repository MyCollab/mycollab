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
package com.mycollab.module.crm.ui.format

import com.mycollab.common.i18n.GenericI18Enum
import com.mycollab.module.crm.i18n.CaseI18nEnum
import com.mycollab.module.crm.i18n.OptionI18nEnum.*
import com.mycollab.module.user.ui.format.UserHistoryFieldFormat
import com.mycollab.vaadin.ui.formatter.FieldGroupFormatter
import com.mycollab.vaadin.ui.formatter.I18nHistoryFieldFormat

/**
 * @author MyCollab LTd
 * @since 5.1.4
 */
class CaseFieldFormatter private constructor() : FieldGroupFormatter() {

    init {
        generateFieldDisplayHandler("priority", CaseI18nEnum.FORM_PRIORITY, I18nHistoryFieldFormat(CasePriority::class.java))
        generateFieldDisplayHandler("status", GenericI18Enum.FORM_STATUS, I18nHistoryFieldFormat(CaseStatus::class.java))
        generateFieldDisplayHandler("accountid", CaseI18nEnum.FORM_ACCOUNT, AccountHistoryFieldFormat())
        generateFieldDisplayHandler("phonenumber", GenericI18Enum.FORM_PHONE)
        generateFieldDisplayHandler("origin", CaseI18nEnum.FORM_ORIGIN, I18nHistoryFieldFormat(CaseOrigin::class.java))
        generateFieldDisplayHandler("type", GenericI18Enum.FORM_TYPE, I18nHistoryFieldFormat(CaseType::class.java))
        generateFieldDisplayHandler("reason", CaseI18nEnum.FORM_REASON, I18nHistoryFieldFormat(CaseReason::class.java))
        generateFieldDisplayHandler("subject", CaseI18nEnum.FORM_SUBJECT)
        generateFieldDisplayHandler("email", GenericI18Enum.FORM_EMAIL)
        generateFieldDisplayHandler("assignuser", GenericI18Enum.FORM_ASSIGNEE, UserHistoryFieldFormat())
        generateFieldDisplayHandler("description", GenericI18Enum.FORM_DESCRIPTION, FieldGroupFormatter.TRIM_HTMLS)
        generateFieldDisplayHandler("resolution", CaseI18nEnum.FORM_RESOLUTION, FieldGroupFormatter.TRIM_HTMLS)
    }

    companion object {
        private val _instance = CaseFieldFormatter()

        fun instance(): CaseFieldFormatter = _instance
    }
}

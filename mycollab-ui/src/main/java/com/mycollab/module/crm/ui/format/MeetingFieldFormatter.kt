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
import com.mycollab.module.crm.i18n.MeetingI18nEnum
import com.mycollab.vaadin.ui.formatter.FieldGroupFormatter

/**
 * @author MyCollab Ltd
 * @since 5.1.4
 */
class MeetingFieldFormatter private constructor() : FieldGroupFormatter() {

    init {
        generateFieldDisplayHandler("subject", MeetingI18nEnum.FORM_SUBJECT)
        generateFieldDisplayHandler("status", GenericI18Enum.FORM_STATUS)
        generateFieldDisplayHandler("startdate", MeetingI18nEnum.FORM_START_DATE_TIME, FieldGroupFormatter.DATETIME_FIELD)
        generateFieldDisplayHandler("enddate", MeetingI18nEnum.FORM_END_DATE_TIME, FieldGroupFormatter.DATETIME_FIELD)
        generateFieldDisplayHandler("location", MeetingI18nEnum.FORM_LOCATION)
    }

    companion object {
        private val _instance = MeetingFieldFormatter()

        fun instance(): MeetingFieldFormatter = _instance
    }
}

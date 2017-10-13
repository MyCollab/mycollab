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
import com.mycollab.module.project.i18n.ComponentI18nEnum
import com.mycollab.module.tracker.domain.Component
import com.mycollab.vaadin.ui.formatter.FieldGroupFormatter
import com.mycollab.vaadin.ui.formatter.I18nHistoryFieldFormat

/**
 * @author MyCollab Ltd
 * @since 5.1.4
 */
class ComponentFieldFormatter private constructor() : FieldGroupFormatter() {

    init {
        generateFieldDisplayHandler(Component.Field.name.name, GenericI18Enum.FORM_NAME)
        generateFieldDisplayHandler(Component.Field.description.name, GenericI18Enum.FORM_DESCRIPTION)
        generateFieldDisplayHandler(Component.Field.userlead.name, ComponentI18nEnum.FORM_LEAD,
                ProjectMemberHistoryFieldFormat())
        generateFieldDisplayHandler(Component.Field.status.name, GenericI18Enum.FORM_STATUS,
                I18nHistoryFieldFormat(StatusI18nEnum::class.java))
    }

    companion object {
        private val _instance = ComponentFieldFormatter()

        fun instance(): ComponentFieldFormatter {
            return _instance
        }
    }
}

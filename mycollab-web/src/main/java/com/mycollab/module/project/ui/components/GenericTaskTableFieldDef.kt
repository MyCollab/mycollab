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
package com.mycollab.module.project.ui.components

import com.mycollab.common.TableViewField
import com.mycollab.common.i18n.GenericI18Enum
import com.mycollab.vaadin.web.ui.WebUIConstants

/**
 * @author MyCollab Ltd.
 * @since 4.0
 */
object GenericTaskTableFieldDef {
    @JvmField val name = TableViewField(GenericI18Enum.FORM_DESCRIPTION, "name",
            WebUIConstants.TABLE_EX_LABEL_WIDTH)

    @JvmField val assignUser = TableViewField(GenericI18Enum.FORM_ASSIGNEE,
            "assignUser", WebUIConstants.TABLE_EX_LABEL_WIDTH)
}

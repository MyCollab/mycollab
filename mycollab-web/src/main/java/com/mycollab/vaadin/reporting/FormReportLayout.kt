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
package com.mycollab.vaadin.reporting

import com.mycollab.form.view.builder.type.DynaForm

import java.util.Arrays
import java.util.HashSet

/**
 * @author MyCollab Ltd
 * @since 5.2.11
 */
class FormReportLayout(val moduleName: String, val titleField: String, val dynaForm: DynaForm, vararg excludeFieldArr: String) {
    var excludeFields: Set<String>? = null
        private set

    init {
        if (excludeFieldArr.isNotEmpty()) {
            this.excludeFields = HashSet(Arrays.asList(*excludeFieldArr))
        } else {
            this.excludeFields = HashSet()
        }
    }
}

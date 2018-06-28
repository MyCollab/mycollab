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
package com.mycollab.module.project.domain.criteria

import com.mycollab.db.arguments.SearchCriteria
import com.mycollab.db.query.CacheParamMapper
import com.mycollab.db.query.PropertyListParam
import com.mycollab.module.project.ProjectTypeConstants

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
class InvoiceSearchCriteria : SearchCriteria() {

    companion object {
        @JvmField val p_status = CacheParamMapper.register(ProjectTypeConstants.INVOICE, null,
                 PropertyListParam<String>("status", "m_prj_invoice", "status"))
        @JvmField val p_projectIds = CacheParamMapper.register(ProjectTypeConstants.INVOICE, null,
                 PropertyListParam<Int>("projectid", "m_prj_invoice", "projectId"))
    }
}
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
package com.mycollab.module.crm.domain.criteria

import com.mycollab.db.arguments.NumberSearchField
import com.mycollab.db.arguments.SearchCriteria
import com.mycollab.db.arguments.StringSearchField

class TargetGroupSearchCriteria : SearchCriteria() {
    var assignUserName: StringSearchField? = null

    var listName: StringSearchField? = null

    var assignUser: StringSearchField? = null

    var targetId: NumberSearchField? = null

    var contactId: NumberSearchField? = null

    var leadId: NumberSearchField? = null

    var campaignId: NumberSearchField? = null

}

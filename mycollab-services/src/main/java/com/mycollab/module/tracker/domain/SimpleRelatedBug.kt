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
package com.mycollab.module.tracker.domain

import com.mycollab.module.project.domain.SimpleBug

/**
 * @author MyCollab Ltd
 * @since 5.1.0
 */
class SimpleRelatedBug {

    var id: Int? = null
    var relatedBug: SimpleBug? = null
    var relatedType: String? = null
    var related: Boolean? = null

    companion object {
        val AFF_VERSION = "AffVersion"
        val FIX_VERSION = "FixVersion"
        val COMPONENT = "Component"
    }
}

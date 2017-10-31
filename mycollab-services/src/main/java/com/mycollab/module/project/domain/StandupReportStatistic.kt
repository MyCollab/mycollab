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
package com.mycollab.module.project.domain

import com.mycollab.core.arguments.ValuedBean

/**
 * @author MyCollab Ltd
 * @since 5.3.0
 */
class StandupReportStatistic : ValuedBean() {
    var projectId: Int? = null

    var projectKey: String? = null

    var projectName: String? = null

    var projectAvatarId: String? = null

    var totalWrittenReports: Int? = null

    var totalReports: Int? = null
}

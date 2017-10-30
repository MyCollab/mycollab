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

import com.mycollab.common.i18n.OptionI18nEnum.StatusI18nEnum
import com.mycollab.core.arguments.NotBindable

class SimpleProject : Project() {

    var createUserFullName: String? = null

    var numOpenBugs: Int? = null

    var numBugs: Int? = null

    var numOpenTasks: Int? = null

    var numTasks: Int? = null

    var numOpenRisks: Int? = null

    var numRisks: Int? = null

    var numActiveMembers: Int? = null

    var numClosedPhase: Int? = null

    var numInProgressPhase: Int? = null

    var numFuturePhase: Int? = null

    var totalBillableHours: Double? = null

    var totalNonBillableHours: Double? = null

    var leadFullName: String? = null

    var leadAvatarId: String? = null

    var clientName: String? = null

    var clientAvatarId: String? = null

    @NotBindable
    var customizeView: ProjectCustomizeView? = null

    val isProjectArchived: Boolean
        get() = StatusI18nEnum.Archived.name == this.projectstatus

    val isArchived: Boolean
        get() = StatusI18nEnum.Archived.name == projectstatus

    enum class Field {
        leadFullName, totalBillableHours, totalNonBillableHours, clientName;

        fun equalTo(value: Any): Boolean = name == value
    }
}

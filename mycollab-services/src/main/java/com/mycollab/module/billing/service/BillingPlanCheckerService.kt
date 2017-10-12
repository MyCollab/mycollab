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
package com.mycollab.module.billing.service

import com.mycollab.cache.IgnoreCacheClass
import com.mycollab.db.persistence.service.IService
import com.mycollab.module.billing.UsageExceedBillingPlanException

/**
 * @author MyCollab Ltd.
 * @since 4.1
 */
@IgnoreCacheClass
interface BillingPlanCheckerService : IService {
    @Throws(UsageExceedBillingPlanException::class)
    fun validateAccountCanCreateMoreProject(sAccountId: Int?)

    @Throws(UsageExceedBillingPlanException::class)
    fun validateAccountCanCreateNewUser(sAccountId: Int?)

    @Throws(UsageExceedBillingPlanException::class)
    fun validateAccountCanUploadMoreFiles(sAccountId: Int?, extraBytes: Long)
}

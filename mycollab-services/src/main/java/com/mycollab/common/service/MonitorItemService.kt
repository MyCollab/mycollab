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
package com.mycollab.common.service

import com.mycollab.cache.IgnoreCacheClass
import com.mycollab.common.domain.MonitorItem
import com.mycollab.common.domain.criteria.MonitorSearchCriteria
import com.mycollab.db.persistence.service.IDefaultService
import com.mycollab.module.user.domain.SimpleUser

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@IgnoreCacheClass
interface MonitorItemService : IDefaultService<Int, MonitorItem, MonitorSearchCriteria> {

    fun isUserWatchingItem(username: String, type: String, typeId: Int): Boolean

    fun getWatchers(type: String, typeId: Int): List<SimpleUser>

    fun saveMonitorItems(monitorItems: Collection<MonitorItem>)
}

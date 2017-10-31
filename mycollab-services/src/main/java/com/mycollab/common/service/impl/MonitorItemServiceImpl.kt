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
package com.mycollab.common.service.impl

import com.mycollab.common.dao.MonitorItemMapper
import com.mycollab.common.dao.MonitorItemMapperExt
import com.mycollab.common.domain.MonitorItem
import com.mycollab.common.domain.MonitorItemExample
import com.mycollab.common.domain.criteria.MonitorSearchCriteria
import com.mycollab.common.service.MonitorItemService
import com.mycollab.db.persistence.ICrudGenericDAO
import com.mycollab.db.persistence.ISearchableDAO
import com.mycollab.db.persistence.service.DefaultService
import com.mycollab.module.user.domain.SimpleUser
import org.springframework.stereotype.Service

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@Service
class MonitorItemServiceImpl(private val monitorItemMapper: MonitorItemMapper,
                             private val monitorItemMapperExt: MonitorItemMapperExt) : DefaultService<Int, MonitorItem, MonitorSearchCriteria>(), MonitorItemService {

    override val crudMapper: ICrudGenericDAO<Int, MonitorItem>
        get() = monitorItemMapper as ICrudGenericDAO<Int, MonitorItem>

    override val searchMapper: ISearchableDAO<MonitorSearchCriteria>
        get() = monitorItemMapperExt

    override fun isUserWatchingItem(username: String, type: String, typeId: Int): Boolean {
        val ex = MonitorItemExample()
        ex.createCriteria().andUserEqualTo(username).andTypeEqualTo(type).andTypeidEqualTo(typeId)
        return monitorItemMapper.countByExample(ex) > 0
    }

    override fun saveWithSession(record: MonitorItem, username: String?): Int {
        val ex = MonitorItemExample()
        ex.createCriteria().andTypeEqualTo(record.type).andTypeidEqualTo(record.typeid).andUserEqualTo(record.user)
        val count = monitorItemMapper.countByExample(ex)
        return if (count > 0) 1 else super.saveWithSession(record, username)
    }

    override fun saveMonitorItems(monitorItems: Collection<MonitorItem>) {
        if (monitorItems.isNotEmpty()) {
            monitorItemMapperExt.saveMonitorItems(monitorItems)
        }
    }

    override fun getWatchers(type: String, typeId: Int): List<SimpleUser> =
            monitorItemMapperExt.getWatchers(type, typeId)

    override fun getNextItemKey(criteria: MonitorSearchCriteria): Int? = null

    override fun getPreviousItemKey(criteria: MonitorSearchCriteria): Int? = null
}

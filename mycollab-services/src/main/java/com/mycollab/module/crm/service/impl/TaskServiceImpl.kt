/**
 * mycollab-services - Parent pom providing dependency and plugin management for applications
		built with Maven
 * Copyright © ${project.inceptionYear} MyCollab (support@mycollab.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.crm.service.impl

import com.google.common.eventbus.AsyncEventBus
import com.mycollab.aspect.ClassInfo
import com.mycollab.aspect.ClassInfoMap
import com.mycollab.aspect.Traceable
import com.mycollab.aspect.Watchable
import com.mycollab.cache.CleanCacheEvent
import com.mycollab.common.ModuleNameConstants
import com.mycollab.db.persistence.ICrudGenericDAO
import com.mycollab.db.persistence.ISearchableDAO
import com.mycollab.db.persistence.service.DefaultService
import com.mycollab.module.crm.CrmTypeConstants
import com.mycollab.module.crm.dao.CrmTaskMapper
import com.mycollab.module.crm.dao.CrmTaskMapperExt
import com.mycollab.module.crm.domain.CrmTask
import com.mycollab.module.crm.domain.SimpleCrmTask
import com.mycollab.module.crm.domain.criteria.CrmTaskSearchCriteria
import com.mycollab.module.crm.service.EventService
import com.mycollab.module.crm.service.TaskService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@Service
@Transactional
@Traceable(nameField = "subject")
@Watchable(userFieldName = "assignuser")
class TaskServiceImpl(private val taskMapper: CrmTaskMapper,
                      private val taskMapperExt: CrmTaskMapperExt,
                      private val asyncEventBus: AsyncEventBus) : DefaultService<Int, CrmTask, CrmTaskSearchCriteria>(), TaskService {

    override val crudMapper: ICrudGenericDAO<Int, CrmTask>
        get() = taskMapper as ICrudGenericDAO<Int, CrmTask>

    override val searchMapper: ISearchableDAO<CrmTaskSearchCriteria>
        get() = taskMapperExt

    override fun findById(taskId: Int?, sAccountId: Int?): SimpleCrmTask {
        return taskMapperExt.findById(taskId)
    }

    override fun saveWithSession(record: CrmTask, username: String?): Int {
        val result = super.saveWithSession(record, username)
        asyncEventBus.post(CleanCacheEvent(record.saccountid, arrayOf<Class<*>>(EventService::class.java)))
        return result
    }

    override fun updateWithSession(record: CrmTask, username: String?): Int {
        val result = super.updateWithSession(record, username)
        asyncEventBus.post(CleanCacheEvent(record.saccountid, arrayOf<Class<*>>(EventService::class.java)))
        return result
    }

    override fun removeByCriteria(criteria: CrmTaskSearchCriteria, sAccountId: Int) {
        super.removeByCriteria(criteria, sAccountId)
        asyncEventBus.post(CleanCacheEvent(sAccountId, arrayOf<Class<*>>(EventService::class.java)))
    }

    override fun massRemoveWithSession(tasks: List<CrmTask>, username: String?, sAccountId: Int) {
        super.massRemoveWithSession(tasks, username, sAccountId)
        asyncEventBus.post(CleanCacheEvent(sAccountId, arrayOf<Class<*>>(EventService::class.java)))
    }

    override fun massUpdateWithSession(record: CrmTask, primaryKeys: List<Int>, accountId: Int?) {
        super.massUpdateWithSession(record, primaryKeys, accountId)
        asyncEventBus.post(CleanCacheEvent(accountId, arrayOf<Class<*>>(EventService::class.java)))
    }

    override fun updateBySearchCriteria(record: CrmTask, searchCriteria: CrmTaskSearchCriteria) {
        super.updateBySearchCriteria(record, searchCriteria)
        asyncEventBus.post(CleanCacheEvent(searchCriteria.accountId.value as Int,
                arrayOf<Class<*>>(EventService::class.java)))
    }

    companion object {

        init {
            ClassInfoMap.put(TaskServiceImpl::class.java, ClassInfo(ModuleNameConstants.CRM, CrmTypeConstants.TASK))
        }
    }

}

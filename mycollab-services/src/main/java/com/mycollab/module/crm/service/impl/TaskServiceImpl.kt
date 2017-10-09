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

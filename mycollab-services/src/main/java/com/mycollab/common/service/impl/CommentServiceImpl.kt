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

import com.google.common.eventbus.AsyncEventBus
import com.mycollab.cache.CleanCacheEvent
import com.mycollab.common.ActivityStreamConstants
import com.mycollab.common.ModuleNameConstants
import com.mycollab.common.MonitorTypeConstants
import com.mycollab.common.dao.CommentMapper
import com.mycollab.common.dao.CommentMapperExt
import com.mycollab.common.domain.ActivityStreamWithBLOBs
import com.mycollab.common.domain.CommentWithBLOBs
import com.mycollab.common.domain.RelayEmailNotificationWithBLOBs
import com.mycollab.common.domain.criteria.CommentSearchCriteria
import com.mycollab.common.service.ActivityStreamService
import com.mycollab.common.service.CommentService
import com.mycollab.common.service.RelayEmailNotificationService
import com.mycollab.db.persistence.ICrudGenericDAO
import com.mycollab.db.persistence.ISearchableDAO
import com.mycollab.db.persistence.service.DefaultService
import com.mycollab.module.project.ProjectTypeConstants
import com.mycollab.module.project.service.MessageService
import com.mycollab.module.project.service.ProjectTaskService
import com.mycollab.module.project.service.ProjectTicketService
import com.mycollab.module.project.service.RiskService
import com.mycollab.module.tracker.service.BugService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@Service
open class CommentServiceImpl(private val commentMapper: CommentMapper,
                         private val commentMapperExt: CommentMapperExt,
                         private val relayEmailNotificationService: RelayEmailNotificationService,
                         private val activityStreamService: ActivityStreamService,
                         private val asyncEventBus: AsyncEventBus) : DefaultService<Int, CommentWithBLOBs, CommentSearchCriteria>(), CommentService {

    override val crudMapper: ICrudGenericDAO<Int, CommentWithBLOBs>
        get() = commentMapper as ICrudGenericDAO<Int, CommentWithBLOBs>

    override val searchMapper: ISearchableDAO<CommentSearchCriteria>
        get() = commentMapperExt

    override fun saveWithSession(record: CommentWithBLOBs, username: String?): Int {
        val saveId = super.saveWithSession(record, username)

        when {
            ProjectTypeConstants.MESSAGE == record.type -> asyncEventBus.post(CleanCacheEvent(record.saccountid, arrayOf<Class<*>>(MessageService::class.java)))
            ProjectTypeConstants.RISK == record.type -> asyncEventBus.post(CleanCacheEvent(record.saccountid, arrayOf<Class<*>>(RiskService::class.java, ProjectTicketService::class.java)))
            ProjectTypeConstants.TASK == record.type -> asyncEventBus.post(CleanCacheEvent(record.saccountid, arrayOf<Class<*>>(ProjectTaskService::class.java, ProjectTicketService::class.java)))
            ProjectTypeConstants.BUG == record.type -> asyncEventBus.post(CleanCacheEvent(record.saccountid, arrayOf<Class<*>>(BugService::class.java, ProjectTicketService::class.java)))
        }

        relayEmailNotificationService.saveWithSession(getRelayEmailNotification(record), username)
        activityStreamService.saveWithSession(getActivityStream(record, username), username)
        return saveId
    }

    private fun getActivityStream(record: CommentWithBLOBs, username: String?): ActivityStreamWithBLOBs {
        val activityStream = ActivityStreamWithBLOBs()
        activityStream.action = ActivityStreamConstants.ACTION_COMMENT
        activityStream.createduser = username
        activityStream.saccountid = record.saccountid
        activityStream.type = record.type
        activityStream.typeid = record.typeid
        activityStream.namefield = record.comment
        activityStream.extratypeid = record.extratypeid
        when {
            record.type != null && record.type.startsWith("Project-") -> activityStream.module = ModuleNameConstants.PRJ
            record.type != null && record.type.startsWith("Crm-") -> activityStream.module = ModuleNameConstants.CRM
            else -> LOG.error("Can not define module type of bean $record")
        }
        return activityStream
    }

    private fun getRelayEmailNotification(record: CommentWithBLOBs): RelayEmailNotificationWithBLOBs {
        val relayEmailNotification = RelayEmailNotificationWithBLOBs()
        relayEmailNotification.saccountid = record.saccountid
        relayEmailNotification.action = MonitorTypeConstants.ADD_COMMENT_ACTION
        relayEmailNotification.changeby = record.createduser
        relayEmailNotification.changecomment = record.comment
        relayEmailNotification.type = record.type
        relayEmailNotification.typeid = record.typeid
        relayEmailNotification.extratypeid = record.extratypeid
        return relayEmailNotification
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(CommentServiceImpl::class.java)
    }
}
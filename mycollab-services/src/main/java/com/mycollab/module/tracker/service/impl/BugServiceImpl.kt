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
package com.mycollab.module.tracker.service.impl

import com.google.common.eventbus.AsyncEventBus
import com.mycollab.aspect.ClassInfo
import com.mycollab.aspect.ClassInfoMap
import com.mycollab.aspect.Traceable
import com.mycollab.aspect.Watchable
import com.mycollab.cache.CleanCacheEvent
import com.mycollab.common.ModuleNameConstants
import com.mycollab.common.domain.GroupItem
import com.mycollab.common.event.TimelineTrackingUpdateEvent
import com.mycollab.common.i18n.OptionI18nEnum.StatusI18nEnum
import com.mycollab.common.service.TagService
import com.mycollab.common.service.TimelineTrackingService
import com.mycollab.concurrent.DistributionLockUtil
import com.mycollab.core.MyCollabException
import com.mycollab.core.cache.CacheKey
import com.mycollab.core.cache.CleanCache
import com.mycollab.db.persistence.ICrudGenericDAO
import com.mycollab.db.persistence.ISearchableDAO
import com.mycollab.db.persistence.service.DefaultService
import com.mycollab.module.project.ProjectTypeConstants
import com.mycollab.module.project.esb.DeleteProjectBugEvent
import com.mycollab.module.project.i18n.OptionI18nEnum.Priority
import com.mycollab.module.project.service.*
import com.mycollab.module.tracker.dao.BugMapper
import com.mycollab.module.tracker.dao.BugMapperExt
import com.mycollab.module.tracker.domain.BugWithBLOBs
import com.mycollab.module.tracker.domain.SimpleBug
import com.mycollab.module.tracker.domain.criteria.BugSearchCriteria
import com.mycollab.module.tracker.service.BugService
import org.springframework.jdbc.core.BatchPreparedStatementSetter
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional
import java.sql.PreparedStatement
import java.sql.SQLException
import java.util.concurrent.TimeUnit
import javax.sql.DataSource

/**
 * @author MyCollab Ltd.
 * @since 1.0.0
 */
@Service
@Transactional
@Traceable(nameField = "name", extraFieldName = "projectid")
@Watchable(userFieldName = "assignuser")
class BugServiceImpl(private val bugMapper: BugMapper,
                     private val bugMapperExt: BugMapperExt,
                     private val asyncEventBus: AsyncEventBus,
                     private val dataSource: DataSource) : DefaultService<Int, BugWithBLOBs, BugSearchCriteria>(), BugService {

    override val crudMapper: ICrudGenericDAO<Int, BugWithBLOBs>
        get() = bugMapper as ICrudGenericDAO<Int, BugWithBLOBs>

    override val searchMapper: ISearchableDAO<BugSearchCriteria>
        get() = bugMapperExt

    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    override fun saveWithSession(record: BugWithBLOBs, username: String?): Int {
        val lock = DistributionLockUtil.getLock("bug-" + record.saccountid!!)
        try {
            if (lock.tryLock(120, TimeUnit.SECONDS)) {
                val maxKey = bugMapperExt.getMaxKey(record.projectid!!)
                record.bugkey = if (maxKey == null) 1 else maxKey + 1
                if (record.priority == null) {
                    record.priority = Priority.Medium.name
                }
                if (record.status == null) {
                    record.status = StatusI18nEnum.Open.name
                }
                val bugId = super.saveWithSession(record, username)

                asyncEventBus.post(TimelineTrackingUpdateEvent(ProjectTypeConstants.BUG, bugId, "status", record.status,
                        record.projectid, record.saccountid))
                return bugId
            } else {
                throw MyCollabException("Timeout operation")
            }
        } catch (e: InterruptedException) {
            throw MyCollabException(e)
        } finally {
            DistributionLockUtil.removeLock("bug-" + record.saccountid!!)
            lock.unlock()
        }
    }

    override fun updateWithSession(record: BugWithBLOBs, username: String?): Int {
        asyncEventBus.post(TimelineTrackingUpdateEvent(ProjectTypeConstants.BUG, record.id, "status", record.status,
                record.projectid, record.saccountid))
        return super.updateWithSession(record, username)
    }

    @CleanCache
    fun postDirtyUpdate(sAccountId: Int?) {
        asyncEventBus.post(CleanCacheEvent(sAccountId, arrayOf<Class<*>>(ProjectService::class.java, ProjectTicketService::class.java, ProjectMemberService::class.java, ProjectActivityStreamService::class.java, ItemTimeLoggingService::class.java, TagService::class.java, TimelineTrackingService::class.java, ProjectTicketService::class.java)))
    }

    override fun updateSelectiveWithSession(record: BugWithBLOBs, username: String?): Int? {
        asyncEventBus.post(TimelineTrackingUpdateEvent(ProjectTypeConstants.BUG, record.id, "status", record.status,
                record.projectid, record.saccountid))
        return super.updateSelectiveWithSession(record, username)
    }

    override fun massRemoveWithSession(items: List<BugWithBLOBs>, username: String?, sAccountId: Int) {
        super.massRemoveWithSession(items, username, sAccountId)
        val event = DeleteProjectBugEvent(items.toTypedArray(), username, sAccountId)
        asyncEventBus.post(event)
    }

    override fun getStatusSummary(criteria: BugSearchCriteria): List<GroupItem> =
            bugMapperExt.getStatusSummary(criteria)

    override fun getPrioritySummary(criteria: BugSearchCriteria): List<GroupItem> =
            bugMapperExt.getPrioritySummary(criteria)

    override fun getAssignedDefectsSummary(criteria: BugSearchCriteria): List<GroupItem> =
            bugMapperExt.getAssignedDefectsSummary(criteria)

    override fun getReporterDefectsSummary(criteria: BugSearchCriteria): List<GroupItem> =
            bugMapperExt.getReporterDefectsSummary(criteria)

    override fun getResolutionDefectsSummary(criteria: BugSearchCriteria): List<GroupItem> =
            bugMapperExt.getResolutionDefectsSummary(criteria)

    override fun getComponentDefectsSummary(criteria: BugSearchCriteria): List<GroupItem> =
            bugMapperExt.getComponentDefectsSummary(criteria)

    override fun getVersionDefectsSummary(criteria: BugSearchCriteria): List<GroupItem> =
            bugMapperExt.getVersionDefectsSummary(criteria)

    override fun findById(bugId: Int, sAccountId: Int): SimpleBug =
            bugMapperExt.getBugById(bugId)

    override fun findByProjectAndBugKey(bugKey: Int, projectShortName: String, sAccountId: Int): SimpleBug? =
            bugMapperExt.findByProjectAndBugKey(bugKey, projectShortName, sAccountId)

    override fun massUpdateBugIndexes(mapIndexes: List<Map<String, Int>>, @CacheKey sAccountId: Int) {
        val jdbcTemplate = JdbcTemplate(dataSource)
        jdbcTemplate.batchUpdate("UPDATE `m_tracker_bug` SET `bugIndex`=? WHERE `id`=?", object : BatchPreparedStatementSetter {
            @Throws(SQLException::class)
            override fun setValues(preparedStatement: PreparedStatement, i: Int) {
                preparedStatement.setInt(1, mapIndexes[i]["index"]!!)
                preparedStatement.setInt(2, mapIndexes[i]["id"]!!)
            }

            override fun getBatchSize(): Int = mapIndexes.size
        })
    }

    companion object {
        init {
            val bugInfo = ClassInfo(ModuleNameConstants.PRJ, ProjectTypeConstants.BUG)
            bugInfo.addExcludeHistoryField(BugWithBLOBs.Field.bugindex.name)
            ClassInfoMap.put(BugServiceImpl::class.java, bugInfo)
        }
    }
}

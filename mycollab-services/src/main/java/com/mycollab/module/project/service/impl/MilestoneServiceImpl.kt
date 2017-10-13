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
package com.mycollab.module.project.service.impl

import com.google.common.eventbus.AsyncEventBus
import com.mycollab.aspect.ClassInfo
import com.mycollab.aspect.ClassInfoMap
import com.mycollab.aspect.Traceable
import com.mycollab.cache.CleanCacheEvent
import com.mycollab.common.ModuleNameConstants
import com.mycollab.core.cache.CacheKey
import com.mycollab.core.cache.CleanCache
import com.mycollab.db.persistence.ICrudGenericDAO
import com.mycollab.db.persistence.ISearchableDAO
import com.mycollab.db.persistence.service.DefaultService
import com.mycollab.module.project.ProjectTypeConstants
import com.mycollab.module.project.dao.MilestoneMapper
import com.mycollab.module.project.dao.MilestoneMapperExt
import com.mycollab.module.project.domain.Milestone
import com.mycollab.module.project.domain.SimpleMilestone
import com.mycollab.module.project.domain.criteria.MilestoneSearchCriteria
import com.mycollab.module.project.i18n.OptionI18nEnum.MilestoneStatus
import com.mycollab.module.project.service.*
import org.springframework.jdbc.core.BatchPreparedStatementSetter
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.sql.PreparedStatement
import java.sql.SQLException
import javax.sql.DataSource

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@Service
@Transactional
@Traceable(nameField = "name", extraFieldName = "projectid")
class MilestoneServiceImpl(private val milestoneMapper: MilestoneMapper,
                           private val milestoneMapperExt: MilestoneMapperExt,
                           private val dataSource: DataSource,
                           private val asyncEventBus: AsyncEventBus) : DefaultService<Int, Milestone, MilestoneSearchCriteria>(), MilestoneService {

    override val crudMapper: ICrudGenericDAO<Int, Milestone>
        get() = milestoneMapper as ICrudGenericDAO<Int, Milestone>

    override val searchMapper: ISearchableDAO<MilestoneSearchCriteria>
        get() = milestoneMapperExt

    override fun findById(milestoneId: Int, sAccountId: Int): SimpleMilestone? =
            milestoneMapperExt.findById(milestoneId)

    override fun saveWithSession(record: Milestone, username: String?): Int {
        if (record.status == null) {
            record.status = MilestoneStatus.InProgress.name
        }
        return super.saveWithSession(record, username)
    }

    @CleanCache
    fun postDirtyUpdate(sAccountId: Int?) {
        asyncEventBus.post(CleanCacheEvent(sAccountId, arrayOf<Class<*>>(ProjectService::class.java, GanttAssignmentService::class.java, ProjectTicketService::class.java, ProjectActivityStreamService::class.java)))
    }

    override fun massUpdateOptionIndexes(mapIndexes: List<Map<String, Int>>, @CacheKey sAccountId: Int) {
        val jdbcTemplate = JdbcTemplate(dataSource)
        jdbcTemplate.batchUpdate("UPDATE `m_prj_milestone` SET `orderIndex`=? WHERE `id`=?", object : BatchPreparedStatementSetter {
            @Throws(SQLException::class)
            override fun setValues(preparedStatement: PreparedStatement, i: Int) {
                preparedStatement.setInt(1, mapIndexes[i]["index"]!!)
                preparedStatement.setInt(2, mapIndexes[i]["id"]!!)
            }

            override fun getBatchSize(): Int {
                return mapIndexes.size
            }
        })
    }

    companion object {
        init {
            ClassInfoMap.put(MilestoneServiceImpl::class.java, ClassInfo(ModuleNameConstants.PRJ, ProjectTypeConstants.MILESTONE))
        }
    }
}

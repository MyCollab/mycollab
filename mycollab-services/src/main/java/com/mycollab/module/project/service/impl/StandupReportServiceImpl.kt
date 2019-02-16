package com.mycollab.module.project.service.impl

import com.google.common.eventbus.AsyncEventBus
import com.mycollab.aspect.ClassInfo
import com.mycollab.aspect.ClassInfoMap
import com.mycollab.aspect.Traceable
import com.mycollab.cache.CleanCacheEvent
import com.mycollab.common.ModuleNameConstants
import com.mycollab.core.cache.CacheKey
import com.mycollab.db.arguments.DateSearchField
import com.mycollab.db.arguments.SearchRequest
import com.mycollab.db.arguments.SetSearchField
import com.mycollab.db.arguments.StringSearchField
import com.mycollab.db.persistence.ICrudGenericDAO
import com.mycollab.db.persistence.ISearchableDAO
import com.mycollab.db.persistence.service.DefaultService
import com.mycollab.module.project.ProjectTypeConstants
import com.mycollab.module.project.dao.StandupReportMapper
import com.mycollab.module.project.dao.StandupReportMapperExt
import com.mycollab.module.project.domain.SimpleStandupReport
import com.mycollab.module.project.domain.StandupReportStatistic
import com.mycollab.module.project.domain.StandupReportWithBLOBs
import com.mycollab.module.project.domain.criteria.StandupReportSearchCriteria
import com.mycollab.module.project.service.ProjectActivityStreamService
import com.mycollab.module.project.service.StandupReportService
import com.mycollab.module.user.domain.SimpleUser
import org.apache.commons.collections.CollectionUtils
import org.apache.ibatis.session.RowBounds
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@Service
@Transactional
@Traceable(nameField = "forday", extraFieldName = "projectid")
class StandupReportServiceImpl(private val standupReportMapper: StandupReportMapper,
                               private val standupReportMapperExt: StandupReportMapperExt,
                               private val asyncEventBus: AsyncEventBus) : DefaultService<Int, StandupReportWithBLOBs, StandupReportSearchCriteria>(), StandupReportService {

    override val crudMapper: ICrudGenericDAO<Int, StandupReportWithBLOBs>
        get() = standupReportMapper as ICrudGenericDAO<Int, StandupReportWithBLOBs>

    override val searchMapper: ISearchableDAO<StandupReportSearchCriteria>
        get() = standupReportMapperExt

    override fun findById(standupId: Int, sAccountId: Int): SimpleStandupReport? =
            standupReportMapperExt.findReportById(standupId)

    override fun findStandupReportByDateUser(projectId: Int, username: String, onDate: LocalDate, sAccountId: Int): SimpleStandupReport? {
        val criteria = StandupReportSearchCriteria()
        criteria.projectIds = SetSearchField(projectId)
        criteria.logBy = StringSearchField.and(username)
        criteria.onDate = DateSearchField(onDate, DateSearchField.EQUAL)
        val reports = standupReportMapperExt.findPageableListByCriteria(criteria, RowBounds(0, Integer.MAX_VALUE))

        return if (CollectionUtils.isNotEmpty(reports)) {
            reports[0] as SimpleStandupReport
        } else null

    }

    override fun saveWithSession(record: StandupReportWithBLOBs, username: String?): Int {
        val result = super.saveWithSession(record, username)
        asyncEventBus.post(CleanCacheEvent(record.saccountid, arrayOf(ProjectActivityStreamService::class.java)))
        return result
    }

    override fun updateWithSession(record: StandupReportWithBLOBs, username: String?): Int {
        asyncEventBus.post(CleanCacheEvent(record.saccountid, arrayOf(ProjectActivityStreamService::class.java)))
        return super.updateWithSession(record, username)
    }

    override fun massRemoveWithSession(items: List<StandupReportWithBLOBs>, username: String?, sAccountId: Int) {
        super.massRemoveWithSession(items, username, sAccountId)
        asyncEventBus.post(CleanCacheEvent(sAccountId, arrayOf(ProjectActivityStreamService::class.java)))
    }

    override fun findUsersNotDoReportYet(projectId: Int, onDate: LocalDate, @CacheKey sAccountId: Int): List<SimpleUser> =
            standupReportMapperExt.findUsersNotDoReportYet(projectId, onDate)

    override fun getProjectReportsStatistic(projectIds: List<Int>, onDate: LocalDate, searchRequest: SearchRequest): List<StandupReportStatistic> =
            standupReportMapperExt.getProjectReportsStatistic(projectIds, onDate,
                    RowBounds((searchRequest.currentPage - 1) * searchRequest.numberOfItems,
                            searchRequest.numberOfItems))

    companion object {
        init {
            ClassInfoMap.put(StandupReportServiceImpl::class.java, ClassInfo(ModuleNameConstants.PRJ, ProjectTypeConstants.STANDUP))
        }
    }
}
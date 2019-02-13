package com.mycollab.module.project.dao

import com.mycollab.db.persistence.ISearchableDAO
import com.mycollab.module.project.domain.SimpleStandupReport
import com.mycollab.module.project.domain.StandupReportStatistic
import com.mycollab.module.project.domain.criteria.StandupReportSearchCriteria
import com.mycollab.module.user.domain.SimpleUser
import org.apache.ibatis.annotations.Param
import org.apache.ibatis.session.RowBounds
import java.time.LocalDate

/**
 * @author MyCollab Ltd
 * @since 1.0.0
 */
interface StandupReportMapperExt : ISearchableDAO<StandupReportSearchCriteria> {

    fun findReportById(standupId: Int?): SimpleStandupReport?

    fun getProjectReportsStatistic(@Param("projectIds") projectIds: List<Int>, @Param("onDate") onDate: LocalDate,
                                   rowBounds: RowBounds): List<StandupReportStatistic>

    fun findUsersNotDoReportYet(@Param("projectId") projectId: Int?, @Param("onDate") onDate: LocalDate): List<SimpleUser>
}

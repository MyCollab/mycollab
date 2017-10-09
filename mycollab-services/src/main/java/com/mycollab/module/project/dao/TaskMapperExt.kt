package com.mycollab.module.project.dao

import com.mycollab.common.domain.GroupItem
import com.mycollab.db.persistence.ISearchableDAO
import com.mycollab.module.project.domain.SimpleTask
import com.mycollab.module.project.domain.criteria.TaskSearchCriteria
import org.apache.ibatis.annotations.Param

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
interface TaskMapperExt : ISearchableDAO<TaskSearchCriteria> {

    fun findTaskById(taskId: Int): SimpleTask

    fun getMaxKey(projectId: Int): Int?

    fun getPrioritySummary(@Param("searchCriteria") criteria: TaskSearchCriteria): List<GroupItem>

    fun getStatusSummary(@Param("searchCriteria") criteria: TaskSearchCriteria): List<GroupItem>

    fun getAssignedDefectsSummary(@Param("searchCriteria") criteria: TaskSearchCriteria): List<GroupItem>

    fun findByProjectAndTaskKey(@Param("taskkey") taskkey: Int, @Param("prjShortName") projectShortName: String,
                                @Param("sAccountId") sAccountId: Int): SimpleTask

}

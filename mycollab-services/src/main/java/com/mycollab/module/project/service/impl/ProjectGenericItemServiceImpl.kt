package com.mycollab.module.project.service.impl

import com.mycollab.db.persistence.ISearchableDAO
import com.mycollab.db.persistence.service.DefaultSearchService
import com.mycollab.module.project.dao.ProjectGenericItemMapper
import com.mycollab.module.project.domain.criteria.ProjectGenericItemSearchCriteria
import com.mycollab.module.project.service.ProjectGenericItemService
import org.springframework.stereotype.Service

/**
 * @author MyCollab Ltd.
 * @since 5.0.3
 */
@Service
class ProjectGenericItemServiceImpl(private val projectGenericItemMapper: ProjectGenericItemMapper) : DefaultSearchService<ProjectGenericItemSearchCriteria>(), ProjectGenericItemService {

    override val searchMapper: ISearchableDAO<ProjectGenericItemSearchCriteria>
        get() = projectGenericItemMapper

    override fun getTotalCount(criteria: ProjectGenericItemSearchCriteria): Int {
        return projectGenericItemMapper.getTotalCountFromTask(criteria) +
                projectGenericItemMapper.getTotalCountFromMessage(criteria) +
                projectGenericItemMapper.getTotalCountFromMilestone(criteria) +
                projectGenericItemMapper.getTotalCountFromBug(criteria) +
                projectGenericItemMapper.getTotalCountFromVersion(criteria) +
                projectGenericItemMapper.getTotalCountFromComponent(criteria) +
                projectGenericItemMapper.getTotalCountFromRisk(criteria)
    }
}

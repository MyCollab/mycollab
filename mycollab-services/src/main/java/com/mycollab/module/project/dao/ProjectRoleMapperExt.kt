package com.mycollab.module.project.dao

import com.mycollab.db.persistence.ISearchableDAO
import com.mycollab.module.project.domain.SimpleProjectRole
import com.mycollab.module.project.domain.criteria.ProjectRoleSearchCriteria

/**
 * @author MyCollab Ltd
 * @since 1.0.0
 */
interface ProjectRoleMapperExt : ISearchableDAO<ProjectRoleSearchCriteria> {
    fun findRoleById(roleId: Int): SimpleProjectRole
}

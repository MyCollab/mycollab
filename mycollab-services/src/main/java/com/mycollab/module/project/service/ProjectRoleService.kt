package com.mycollab.module.project.service

import com.mycollab.core.cache.CacheEvict
import com.mycollab.core.cache.CacheKey
import com.mycollab.core.cache.Cacheable
import com.mycollab.db.persistence.service.IDefaultService
import com.mycollab.module.project.domain.ProjectRole
import com.mycollab.module.project.domain.SimpleProjectRole
import com.mycollab.module.project.domain.criteria.ProjectRoleSearchCriteria
import com.mycollab.security.PermissionMap

/**
 * @author MyCollab Ltd
 * @since 1.0.0
 */
interface ProjectRoleService : IDefaultService<Int, ProjectRole, ProjectRoleSearchCriteria> {

    @CacheEvict
    fun savePermission(projectId: Int, roleId: Int?, permissionMap: PermissionMap, @CacheKey sAccountId: Int)

    @Cacheable
    fun findById(roleId: Int, @CacheKey sAccountId: Int): SimpleProjectRole?
}

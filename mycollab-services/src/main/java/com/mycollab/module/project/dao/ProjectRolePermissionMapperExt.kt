package com.mycollab.module.project.dao

import com.mycollab.module.project.domain.RolePermissionVal
import org.apache.ibatis.annotations.Param

/**
 * @author MyCollab Ltd
 * @since 7.0.0
 */
interface ProjectRolePermissionMapperExt {

    fun findProjectsPermissions(@Param("username") username: String?,
                                @Param("projectIds") projectIds: List<Int>?,
                                @Param("sAccountId") sAccountId: Int): List<RolePermissionVal>

    fun findProjectPermission(@Param("username") username: String?,
                              @Param("projectId") projectId: Int,
                              @Param("sAccountId") sAccountId: Int): RolePermissionVal
}
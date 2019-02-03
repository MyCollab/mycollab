package com.mycollab.module.project.dao

import com.mycollab.core.Tuple2
import org.apache.ibatis.annotations.Param

/**
 * @author MyCollab Ltd
 * @since 7.0.0
 */
interface ProjectRolePermissionMapperExt {

    fun findProjectsPermissions(@Param("username") username: String?,
                                @Param("projectIds") projectIds: List<Int>?,
                                @Param("sAccountId") sAccountId: Int): List<Tuple2<Int, String>>
}
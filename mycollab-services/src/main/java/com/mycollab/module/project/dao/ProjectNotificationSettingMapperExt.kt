package com.mycollab.module.project.dao

import com.mycollab.module.project.domain.ProjectNotificationSetting
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Param

@Mapper
interface ProjectNotificationSettingMapperExt {
    fun findNotifications(@Param("projectId") projectId: Int, @Param("sAccountId") sAccountId: Int): List<ProjectNotificationSetting>
}
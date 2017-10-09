package com.mycollab.module.project.service.impl

import com.mycollab.common.NotificationType
import com.mycollab.core.cache.CacheKey
import com.mycollab.db.persistence.ICrudGenericDAO
import com.mycollab.db.persistence.service.DefaultCrudService
import com.mycollab.module.project.dao.ProjectNotificationSettingMapper
import com.mycollab.module.project.domain.ProjectNotificationSetting
import com.mycollab.module.project.domain.ProjectNotificationSettingExample
import com.mycollab.module.project.service.ProjectNotificationSettingService
import org.springframework.stereotype.Service

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@Service
class ProjectNotificationSettingServiceImpl(private val projectNotificationSettingMapper: ProjectNotificationSettingMapper) : DefaultCrudService<Int, ProjectNotificationSetting>(), ProjectNotificationSettingService {


    override val crudMapper: ICrudGenericDAO<Int, ProjectNotificationSetting>
        get() = projectNotificationSettingMapper as ICrudGenericDAO<Int, ProjectNotificationSetting>

    override fun findNotification(username: String, projectId: Int?, @CacheKey sAccountId: Int?): ProjectNotificationSetting {
        val ex = ProjectNotificationSettingExample()
        ex.createCriteria().andUsernameEqualTo(username).andProjectidEqualTo(projectId).andSaccountidEqualTo(sAccountId)
        val settings = projectNotificationSettingMapper.selectByExample(ex)
        if (settings.isNotEmpty()) {
            return settings[0]
        } else {
            val setting = ProjectNotificationSetting()
            setting.level = NotificationType.Default.name
            setting.projectid = projectId
            setting.saccountid = sAccountId
            setting.username = username
            return setting
        }
    }

    override fun findNotifications(projectId: Int?, @CacheKey sAccountId: Int?): List<ProjectNotificationSetting> {
        val ex = ProjectNotificationSettingExample()
        ex.createCriteria().andProjectidEqualTo(projectId).andSaccountidEqualTo(sAccountId)
        return projectNotificationSettingMapper.selectByExample(ex)
    }

}

package com.mycollab.schedule.jobs

import com.mycollab.common.domain.LiveInstance
import com.mycollab.common.service.AppPropertiesService
import com.mycollab.configuration.ServerConfiguration
import com.mycollab.core.Version
import com.mycollab.module.project.dao.ProjectMapper
import com.mycollab.module.project.domain.ProjectExample
import com.mycollab.module.user.dao.UserMapper
import com.mycollab.module.user.domain.UserExample
import org.joda.time.DateTime
import org.quartz.JobExecutionContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.config.BeanDefinition
import org.springframework.context.annotation.Profile
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
@Component
@Scope(BeanDefinition.SCOPE_SINGLETON)
class LiveInstanceMonitorJob  : GenericQuartzJobBean() {

    @Autowired
    lateinit private var projectMapper: ProjectMapper

    @Autowired
    lateinit private var  userMapper: UserMapper

    @Autowired
    lateinit private var  appPropertiesService: AppPropertiesService

    @Autowired
    lateinit private var  serverConfiguration: ServerConfiguration

    override fun executeJob(context: JobExecutionContext) {
        val numProjects = projectMapper.countByExample(ProjectExample()).toInt()
        val numUsers = userMapper.countByExample(UserExample()).toInt()

        val liveInstance = LiveInstance()
        liveInstance.appversion = Version.getVersion()
        liveInstance.installeddate = DateTime().toDate()
        liveInstance.javaversion = System.getProperty("java.version")
        liveInstance.sysid = appPropertiesService.sysId
        liveInstance.sysproperties = System.getProperty("os.arch") + ":" + System.getProperty("os.name") + ":" +
                System.getProperty("os.name")
        liveInstance.numprojects = numProjects
        liveInstance.numusers = numUsers
        liveInstance.edition = appPropertiesService.edition
        val restTemplate = RestTemplate()
        restTemplate.postForObject(serverConfiguration.getApiUrl("checkInstance"), liveInstance, String::class.java)
    }
}
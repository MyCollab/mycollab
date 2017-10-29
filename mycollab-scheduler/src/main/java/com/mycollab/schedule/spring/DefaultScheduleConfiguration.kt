/**
 * Copyright © MyCollab
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http:></http:>//www.gnu.org/licenses/>.
 */
package com.mycollab.schedule.spring

import com.mycollab.configuration.IDeploymentMode
import com.mycollab.module.project.schedule.email.service.OverdueProjectTicketsNotificationJob
import com.mycollab.schedule.AutowiringSpringBeanJobFactory
import com.mycollab.schedule.jobs.CleanupTimeTrackingCacheDataJob
import com.mycollab.schedule.jobs.CrmSendingRelayEmailNotificationJob
import com.mycollab.schedule.jobs.LiveInstanceMonitorJob
import com.mycollab.schedule.jobs.ProjectSendingRelayEmailNotificationJob
import org.quartz.CronTrigger
import org.quartz.Trigger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.scheduling.quartz.CronTriggerFactoryBean
import org.springframework.scheduling.quartz.JobDetailFactoryBean
import org.springframework.scheduling.quartz.SchedulerFactoryBean

import javax.sql.DataSource
import java.util.Properties

/**
 * @author MyCollab Ltd.
 * @since 4.6.0
 */
@Configuration
@Profile("production")
open class DefaultScheduleConfiguration {

    @Autowired
    private lateinit var dataSource: DataSource

    @Autowired
    private lateinit var applicationContext: ApplicationContext

    @Autowired
    private lateinit var deploymentMode: IDeploymentMode

    @Bean
    open fun cleanTimelineTrackingCacheJob(): JobDetailFactoryBean {
        val bean = JobDetailFactoryBean()
        bean.setDurability(true)
        bean.setJobClass(CleanupTimeTrackingCacheDataJob::class.java)
        return bean
    }

    @Bean
    open fun projectSendRelayNotificationEmailJob(): JobDetailFactoryBean {
        val bean = JobDetailFactoryBean()
        bean.setDurability(true)
        bean.setJobClass(ProjectSendingRelayEmailNotificationJob::class.java)
        return bean
    }

    @Bean
    open fun projectOverdueAssignmentsNotificationEmailJob(): JobDetailFactoryBean {
        val bean = JobDetailFactoryBean()
        bean.setDurability(true)
        bean.setJobClass(OverdueProjectTicketsNotificationJob::class.java)
        return bean
    }

    @Bean
    open fun crmSendRelayNotificationEmailJob(): JobDetailFactoryBean {
        val bean = JobDetailFactoryBean()
        bean.setDurability(true)
        bean.setJobClass(CrmSendingRelayEmailNotificationJob::class.java)
        return bean
    }

    @Bean
    open fun liveInstanceMonitorJobBean(): JobDetailFactoryBean {
        val bean = JobDetailFactoryBean()
        bean.setDurability(true)
        bean.setJobClass(LiveInstanceMonitorJob::class.java)
        return bean
    }

    @Bean
    open fun projectSendRelayNotificationEmailTrigger(): CronTriggerFactoryBean {
        val bean = CronTriggerFactoryBean()
        bean.setJobDetail(projectSendRelayNotificationEmailJob().`object`)
        bean.setCronExpression("0 * * * * ?")
        return bean
    }

    @Bean
    open fun projectOverdueAssignmentsNotificationEmailTrigger(): CronTriggerFactoryBean {
        val bean = CronTriggerFactoryBean()
        bean.setJobDetail(projectOverdueAssignmentsNotificationEmailJob().`object`)
        bean.setCronExpression("0 0 0 * * ?")
        return bean
    }

    @Bean
    open fun crmSendRelayNotificationEmailTrigger(): CronTriggerFactoryBean {
        val bean = CronTriggerFactoryBean()
        bean.setJobDetail(crmSendRelayNotificationEmailJob().`object`)
        bean.setCronExpression("0 * * * * ?")
        return bean
    }

    @Bean
    open fun cleanUpTimelineCacheDataTrigger(): CronTriggerFactoryBean {
        val bean = CronTriggerFactoryBean()
        bean.setJobDetail(cleanTimelineTrackingCacheJob().`object`)
        bean.setCronExpression("0 0 0 * * ?")
        return bean
    }

    @Bean
    open fun liveInstanceMonitorTrigger(): CronTriggerFactoryBean {
        val bean = CronTriggerFactoryBean()
        bean.setJobDetail(liveInstanceMonitorJobBean().`object`)
        bean.setCronExpression("0 0 6 * * ?")
        return bean
    }

    @Bean
    open fun quartzScheduler(): SchedulerFactoryBean {
        val bean = SchedulerFactoryBean()
        if (!deploymentMode.isCommunityEdition) {
            bean.setDataSource(dataSource)
        }

        bean.setQuartzProperties(buildProperties())
        bean.setOverwriteExistingJobs(true)
        val factory = AutowiringSpringBeanJobFactory()
        factory.setApplicationContext(applicationContext)
        bean.setJobFactory(factory)
        bean.setApplicationContextSchedulerContextKey("applicationContextSchedulerContextKey")

        val triggersMap = applicationContext.getBeansOfType(CronTrigger::class.java)
        val triggers = triggersMap.values
        bean.setTriggers(*triggers.toTypedArray<Trigger>())
        return bean
    }

    private fun buildProperties(): Properties {
        val props = Properties()
        props.setProperty("org.quartz.scheduler.instanceId", "AUTO")
        props.setProperty("org.quartz.scheduler.instanceName", "MYCOLLAB_SCHEDULER")
        props.setProperty("org.quartz.scheduler.rmi.export", "false")
        props.setProperty("org.quartz.scheduler.rmi.proxy", "false")
        props.setProperty("org.quartz.threadPool.class", "org.quartz.simpl.SimpleThreadPool")
        props.setProperty("org.quartz.threadPool.threadCount", "10")
        props.setProperty("org.quartz.threadPool.threadPriority", "5")
        props.setProperty("org.quartz.threadPool.threadsInheritContextClassLoaderOfInitializingThread", "true")

        if (deploymentMode.isCommunityEdition) {
            props.setProperty("org.quartz.jobStore.class", "org.quartz.simpl.RAMJobStore")
        } else {
            props.setProperty("org.quartz.jobStore.class", "org.quartz.impl.jdbcjobstore.JobStoreTX")
            props.setProperty("org.quartz.jobStore.dataSource", "dataSource")
            props.setProperty("org.quartz.jobStore.useProperties", "true")
            props.setProperty("org.quartz.jobStore.tablePrefix", "QRTZ_")
            props.setProperty("org.quartz.jobStore.isClustered", "true")
            props.setProperty("org.quartz.jobStore.driverDelegateClass", "org.quartz.impl.jdbcjobstore.StdJDBCDelegate")
        }
        return props
    }
}

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
import com.mycollab.schedule.jobs.LiveInstanceMonitorJob
import com.mycollab.schedule.jobs.ProjectSendingRelayEmailNotificationJob
import org.quartz.CronTrigger
import org.quartz.Scheduler
import org.quartz.Trigger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.quartz.CronTriggerFactoryBean
import org.springframework.scheduling.quartz.JobDetailFactoryBean
import org.springframework.scheduling.quartz.SchedulerFactoryBean
import java.util.*
import javax.sql.DataSource

/**
 * @author MyCollab Ltd.
 * @since 4.6.0
 */
@Configuration
class DefaultScheduleConfiguration {

    @Bean
    fun projectSendRelayNotificationEmailJob(): JobDetailFactoryBean {
        val bean = JobDetailFactoryBean()
        bean.setDurability(true)
        bean.setJobClass(ProjectSendingRelayEmailNotificationJob::class.java)
        return bean
    }

    @Bean
    fun projectOverdueAssignmentsNotificationEmailJob(): JobDetailFactoryBean {
        val bean = JobDetailFactoryBean()
        bean.setDurability(true)
        bean.setJobClass(OverdueProjectTicketsNotificationJob::class.java)
        return bean
    }

    @Bean
    fun liveInstanceMonitorJobBean(): JobDetailFactoryBean {
        val bean = JobDetailFactoryBean()
        bean.setDurability(true)
        bean.setJobClass(LiveInstanceMonitorJob::class.java)
        return bean
    }

    @Bean
    fun projectSendRelayNotificationEmailTrigger(): CronTriggerFactoryBean {
        val bean = CronTriggerFactoryBean()
        bean.setJobDetail(projectSendRelayNotificationEmailJob().`object`!!)
        bean.setCronExpression("0 * * * * ?")
        return bean
    }

    @Bean
    fun projectOverdueAssignmentsNotificationEmailTrigger(): CronTriggerFactoryBean {
        val bean = CronTriggerFactoryBean()
        bean.setJobDetail(projectOverdueAssignmentsNotificationEmailJob().`object`!!)
        bean.setCronExpression("0 0 0 * * ?")
        return bean
    }

    @Bean
    fun liveInstanceMonitorTrigger(): CronTriggerFactoryBean {
        val bean = CronTriggerFactoryBean()
        bean.setJobDetail(liveInstanceMonitorJobBean().`object`!!)
        bean.setCronExpression("0 0 6 * * ?")
        return bean
    }
}

/**
 * This file is part of mycollab-scheduler.
 *
 * mycollab-scheduler is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-scheduler is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-scheduler.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.schedule.spring;

import com.mycollab.schedule.AutowiringSpringBeanJobFactory;
import com.mycollab.schedule.QuartzScheduleProperties;
import com.mycollab.module.user.schedule.email.service.UserSignUpEmailNotificationJob;
import com.mycollab.schedule.jobs.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

/**
 * @author MyCollab Ltd.
 * @since 4.6.0
 */
@Configuration
public class DefaultScheduleConfiguration {
    @Bean
    public JobDetailFactoryBean sendRelayEmailJob() {
        JobDetailFactoryBean bean = new JobDetailFactoryBean();
        bean.setJobClass(SendingRelayEmailJob.class);
        return bean;
    }

    @Bean
    public JobDetailFactoryBean cleanTimelineTrackingCacheJob() {
        JobDetailFactoryBean bean = new JobDetailFactoryBean();
        bean.setJobClass(CleanupTimeTrackingCacheDataJob.class);
        return bean;
    }

    @Bean
    public JobDetailFactoryBean projectSendRelayNotificationEmailJob() {
        JobDetailFactoryBean bean = new JobDetailFactoryBean();
        bean.setJobClass(ProjectSendingRelayEmailNotificationJob.class);
        return bean;
    }

    @Bean
    public JobDetailFactoryBean projectOverdueAssignmentsNotificationEmailJob() {
        JobDetailFactoryBean bean = new JobDetailFactoryBean();
        bean.setJobClass(OverdueProjectAssignmentsNotificationJob.class);
        return bean;
    }

    @Bean
    public JobDetailFactoryBean crmSendRelayNotificationEmailJob() {
        JobDetailFactoryBean bean = new JobDetailFactoryBean();
        bean.setJobClass(CrmSendingRelayEmailNotificationJob.class);
        return bean;
    }

    @Bean
    public JobDetailFactoryBean userSignUpNotificationEmailJob() {
        JobDetailFactoryBean bean = new JobDetailFactoryBean();
        bean.setJobClass(UserSignUpEmailNotificationJob.class);
        return bean;
    }

    @Bean
    public JobDetailFactoryBean liveInstanceMonitorJob() {
        JobDetailFactoryBean bean = new JobDetailFactoryBean();
        bean.setJobClass(LiveInstanceMonitorJob.class);
        return bean;
    }

    @Bean
    public CronTriggerFactoryBean sendingRelayEmailTrigger() {
        CronTriggerFactoryBean bean = new CronTriggerFactoryBean();
        bean.setJobDetail(sendRelayEmailJob().getObject());
        bean.setCronExpression("0 * * * * ?");
        return bean;
    }

    @Bean
    public CronTriggerFactoryBean projectSendRelayNotificationEmailTrigger() {
        CronTriggerFactoryBean bean = new CronTriggerFactoryBean();
        bean.setJobDetail(projectSendRelayNotificationEmailJob().getObject());
        bean.setCronExpression("0 * * * * ?");
        return bean;
    }

    @Bean
    public CronTriggerFactoryBean projectOverdueAssignmentsNotificationEmailTrigger() {
        CronTriggerFactoryBean bean = new CronTriggerFactoryBean();
        bean.setJobDetail(projectOverdueAssignmentsNotificationEmailJob().getObject());
        bean.setCronExpression("0 0 0 * * ?");
        return bean;
    }

    @Bean
    public CronTriggerFactoryBean crmSendRelayNotificationEmailTrigger() {
        CronTriggerFactoryBean bean = new CronTriggerFactoryBean();
        bean.setJobDetail(crmSendRelayNotificationEmailJob().getObject());
        bean.setCronExpression("0 * * * * ?");
        return bean;
    }

    @Bean
    public CronTriggerFactoryBean userSignUpNotificationEmailTrigger() {
        CronTriggerFactoryBean bean = new CronTriggerFactoryBean();
        bean.setJobDetail(userSignUpNotificationEmailJob().getObject());
        bean.setCronExpression("0 * * * * ?");
        return bean;
    }

    @Bean
    public CronTriggerFactoryBean cleanUpTimelineCacheDataTrigger() {
        CronTriggerFactoryBean bean = new CronTriggerFactoryBean();
        bean.setJobDetail(cleanTimelineTrackingCacheJob().getObject());
        bean.setCronExpression("0 0 0 * * ?");
        return bean;
    }

    @Bean
    public CronTriggerFactoryBean liveInstanceMonitorTrigger() {
        CronTriggerFactoryBean bean = new CronTriggerFactoryBean();
        bean.setJobDetail(liveInstanceMonitorJob().getObject());
        bean.setCronExpression("0 0 6 * * ?");
        return bean;
    }

    @Autowired
    private ApplicationContext applicationContext;

    @Bean
    public SchedulerFactoryBean quartzScheduler() {
        SchedulerFactoryBean bean = new SchedulerFactoryBean();

//        if (DeploymentMode.site == SiteConfiguration.getDeploymentMode()) {
//            bean.setDataSource(new DataSourceConfiguration().dataSource());
//        }

        bean.setQuartzProperties(new QuartzScheduleProperties());
        bean.setOverwriteExistingJobs(true);
        AutowiringSpringBeanJobFactory factory = new AutowiringSpringBeanJobFactory();
        factory.setApplicationContext(applicationContext);
        bean.setJobFactory(factory);
        bean.setApplicationContextSchedulerContextKey("applicationContextSchedulerContextKey");

        bean.setTriggers(
                sendingRelayEmailTrigger().getObject(),
                projectSendRelayNotificationEmailTrigger().getObject(),
                projectOverdueAssignmentsNotificationEmailTrigger().getObject(),
                crmSendRelayNotificationEmailTrigger().getObject(),
                userSignUpNotificationEmailTrigger().getObject(),
                cleanUpTimelineCacheDataTrigger().getObject(),
                liveInstanceMonitorTrigger().getObject()
        );
        return bean;
    }
}

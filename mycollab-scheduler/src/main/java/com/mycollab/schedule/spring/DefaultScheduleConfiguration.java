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
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.schedule.spring;

import com.mycollab.configuration.IDeploymentMode;
import com.mycollab.module.project.schedule.email.service.OverdueProjectTicketsNotificationJob;
import com.mycollab.schedule.AutowiringSpringBeanJobFactory;
import com.mycollab.schedule.QuartzScheduleProperties;
import com.mycollab.schedule.jobs.CleanupTimeTrackingCacheDataJob;
import com.mycollab.schedule.jobs.CrmSendingRelayEmailNotificationJob;
import com.mycollab.schedule.jobs.LiveInstanceMonitorJob;
import com.mycollab.schedule.jobs.ProjectSendingRelayEmailNotificationJob;
import org.quartz.CronTrigger;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import javax.sql.DataSource;
import java.util.Collection;
import java.util.Map;

/**
 * @author MyCollab Ltd.
 * @since 4.6.0
 */
@Configuration
@Profile("production")
public class DefaultScheduleConfiguration {

    @Autowired
    private DataSource dataSource;

    @Bean
    public JobDetailFactoryBean cleanTimelineTrackingCacheJob() {
        JobDetailFactoryBean bean = new JobDetailFactoryBean();
        bean.setDurability(true);
        bean.setJobClass(CleanupTimeTrackingCacheDataJob.class);
        return bean;
    }

    @Bean
    public JobDetailFactoryBean projectSendRelayNotificationEmailJob() {
        JobDetailFactoryBean bean = new JobDetailFactoryBean();
        bean.setDurability(true);
        bean.setJobClass(ProjectSendingRelayEmailNotificationJob.class);
        return bean;
    }

    @Bean
    public JobDetailFactoryBean projectOverdueAssignmentsNotificationEmailJob() {
        JobDetailFactoryBean bean = new JobDetailFactoryBean();
        bean.setDurability(true);
        bean.setJobClass(OverdueProjectTicketsNotificationJob.class);
        return bean;
    }

    @Bean
    public JobDetailFactoryBean crmSendRelayNotificationEmailJob() {
        JobDetailFactoryBean bean = new JobDetailFactoryBean();
        bean.setDurability(true);
        bean.setJobClass(CrmSendingRelayEmailNotificationJob.class);
        return bean;
    }

    @Bean
    public JobDetailFactoryBean liveInstanceMonitorJobBean() {
        JobDetailFactoryBean bean = new JobDetailFactoryBean();
        bean.setDurability(true);
        bean.setJobClass(LiveInstanceMonitorJob.class);
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
    public CronTriggerFactoryBean cleanUpTimelineCacheDataTrigger() {
        CronTriggerFactoryBean bean = new CronTriggerFactoryBean();
        bean.setJobDetail(cleanTimelineTrackingCacheJob().getObject());
        bean.setCronExpression("0 0 0 * * ?");
        return bean;
    }

    @Bean
    public CronTriggerFactoryBean liveInstanceMonitorTrigger() {
        CronTriggerFactoryBean bean = new CronTriggerFactoryBean();
        bean.setJobDetail(liveInstanceMonitorJobBean().getObject());
        bean.setCronExpression("0 0 6 * * ?");
        return bean;
    }

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private IDeploymentMode deploymentMode;

    @Bean
    public SchedulerFactoryBean quartzScheduler() {
        SchedulerFactoryBean bean = new SchedulerFactoryBean();
        bean.setDataSource(dataSource);

        bean.setQuartzProperties(new QuartzScheduleProperties());
        bean.setOverwriteExistingJobs(true);
        AutowiringSpringBeanJobFactory factory = new AutowiringSpringBeanJobFactory();
        factory.setApplicationContext(applicationContext);
        bean.setJobFactory(factory);
        bean.setApplicationContextSchedulerContextKey("applicationContextSchedulerContextKey");

        Map<String, CronTrigger> triggersMap = applicationContext.getBeansOfType(CronTrigger.class);
        Collection<CronTrigger> triggers = triggersMap.values();
        bean.setTriggers(triggers.toArray(new Trigger[0]));
        return bean;
    }
}

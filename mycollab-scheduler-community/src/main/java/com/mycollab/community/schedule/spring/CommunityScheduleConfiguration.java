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
package com.mycollab.community.schedule.spring;

import com.mycollab.community.schedule.jobs.CheckUpdateJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;

import javax.sql.DataSource;

/**
 * @author MyCollab Ltd
 * @since 5.1.3
 */
@Configuration
@Profile("production")
public class CommunityScheduleConfiguration {

    @Autowired
    private DataSource dataSource;

    @Bean
    public JobDetailFactoryBean checkUpdateJob() {
        JobDetailFactoryBean bean = new JobDetailFactoryBean();
        bean.setDurability(true);
        bean.setJobClass(CheckUpdateJob.class);
        return bean;
    }

    @Bean
    public CronTriggerFactoryBean checkUpdateJobTrigger() {
        CronTriggerFactoryBean bean = new CronTriggerFactoryBean();
        bean.setJobDetail(checkUpdateJob().getObject());
        bean.setCronExpression("0 0 8 * * ?");
        return bean;
    }
}

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
package com.esofthead.mycollab.schedule;

import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailAwareTrigger;

/**
 * Needed to set Quartz useProperties=true when using Spring classes, because
 * Spring sets an object reference on JobDataMap that is not a String
 * 
 * @see http
 *      ://site.trimplement.com/using-spring-and-quartz-with-jobstore-properties
 * @see http
 *      ://forum.springsource.org/showthread.php?130984-Quartz-error-IOException
 */
public class PersistableCronTriggerFactoryBean extends CronTriggerFactoryBean {
	@Override
	public void afterPropertiesSet() {
		super.afterPropertiesSet();

		// Remove the JobDetail element
		getJobDataMap().remove(JobDetailAwareTrigger.JOB_DETAIL_KEY);
	}
}

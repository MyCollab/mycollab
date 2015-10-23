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

import java.util.Properties;

/**
 * @author MyCollab Ltd.
 * @since 4.6.0
 */
public class QuartzScheduleProperties extends Properties {
    public QuartzScheduleProperties() {
        this.setProperty("org.quartz.scheduler.instanceId", "AUTO");
        this.setProperty("org.quartz.scheduler.instanceName", "MYCOLLAB_SCHEDULER");
        this.setProperty("org.quartz.jobStore.class", "org.quartz.simpl.RAMJobStore");

//        if (DeploymentMode.site == SiteConfiguration.getDeploymentMode()) {
//            this.setProperty("org.quartz.jobStore.class", "org.quartz.impl.jdbcjobstore.JobStoreTX");
//            this.setProperty("org.quartz.jobStore.dataSource", "dataSource");
//            this.setProperty("org.quartz.jobStore.useProperties", "true");
//            this.setProperty("org.quartz.jobStore.tablePrefix", "QRTZ_");
//            this.setProperty("org.quartz.jobStore.isClustered", "true");
//
//            this.setProperty("org.quartz.jobStore.driverDelegateClass", "org.quartz.impl.jdbcjobstore.StdJDBCDelegate");
//
//            this.setProperty("org.quartz.scheduler.rmi.export", "false");
//            this.setProperty("org.quartz.scheduler.rmi.proxy", "false");
//            this.setProperty("org.quartz.threadPool.class", "org.quartz.simpl.SimpleThreadPool");
//            this.setProperty("org.quartz.threadPool.threadCount", "10");
//            this.setProperty("org.quartz.threadPool.threadPriority", "5");
//            this.setProperty("org.quartz.threadPool.threadsInheritContextClassLoaderOfInitializingThread", "true");
//        } else {
//            this.setProperty("org.quartz.jobStore.class", "org.quartz.simpl.RAMJobStore");
//        }
    }
}

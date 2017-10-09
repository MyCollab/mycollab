package com.mycollab.schedule

import java.util.Properties

/**
 * @author MyCollab Ltd.
 * @since 4.6.0
 */
class QuartzScheduleProperties : Properties() {
    init {
        this.setProperty("org.quartz.scheduler.instanceId", "AUTO")
        this.setProperty("org.quartz.scheduler.instanceName", "MYCOLLAB_SCHEDULER")
        this.setProperty("org.quartz.jobStore.class", "org.quartz.simpl.RAMJobStore")

        this.setProperty("org.quartz.jobStore.class", "org.quartz.impl.jdbcjobstore.JobStoreTX")
        this.setProperty("org.quartz.jobStore.dataSource", "dataSource")
        this.setProperty("org.quartz.jobStore.useProperties", "true")
        this.setProperty("org.quartz.jobStore.tablePrefix", "QRTZ_")
        this.setProperty("org.quartz.jobStore.isClustered", "true")

        this.setProperty("org.quartz.jobStore.driverDelegateClass", "org.quartz.impl.jdbcjobstore.StdJDBCDelegate")

        this.setProperty("org.quartz.scheduler.rmi.export", "false")
        this.setProperty("org.quartz.scheduler.rmi.proxy", "false")
        this.setProperty("org.quartz.threadPool.class", "org.quartz.simpl.SimpleThreadPool")
        this.setProperty("org.quartz.threadPool.threadCount", "10")
        this.setProperty("org.quartz.threadPool.threadPriority", "5")
        this.setProperty("org.quartz.threadPool.threadsInheritContextClassLoaderOfInitializingThread", "true")

    }
}

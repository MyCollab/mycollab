package com.mycollab.schedule

import org.quartz.spi.TriggerFiredBundle
import org.springframework.beans.factory.config.AutowireCapableBeanFactory
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.scheduling.quartz.SpringBeanJobFactory

/**
 * Autowire Quartz Jobs with Spring context dependencies
 *
 * @see http://stackoverflow.com/questions/6990767/inject-bean-reference-into-a-quartz-job-in-spring/15211030.15211030
 */
class AutowiringSpringBeanJobFactory : SpringBeanJobFactory(), ApplicationContextAware {
    @Transient private var beanFactory: AutowireCapableBeanFactory? = null

    override fun setApplicationContext(context: ApplicationContext) {
        beanFactory = context.autowireCapableBeanFactory
    }

    @Throws(Exception::class)
    override fun createJobInstance(bundle: TriggerFiredBundle): Any {
        val job = super.createJobInstance(bundle)
        beanFactory!!.autowireBean(job)
        return job
    }
}

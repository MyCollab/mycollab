package com.mycollab.schedule.jobs

import com.mycollab.common.dao.TimelineTrackingCachingMapper
import com.mycollab.common.domain.TimelineTrackingCachingExample
import org.joda.time.LocalDate
import org.quartz.JobExecutionContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.config.BeanDefinition
import org.springframework.context.annotation.Profile
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
@Component
@Profile("production")
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
class CleanupTimeTrackingCacheDataJob : GenericQuartzJobBean() {

    @Autowired
    lateinit var timeTrackingCacheMapper: TimelineTrackingCachingMapper

    override fun executeJob(context: JobExecutionContext) {
        val ex = TimelineTrackingCachingExample()
        val lessThan40Days = LocalDate().minusDays(40)
        ex.createCriteria().andFordayLessThan(lessThan40Days.toDate())
        timeTrackingCacheMapper.deleteByExample(ex)
    }
}
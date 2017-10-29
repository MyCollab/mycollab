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
    private lateinit var timeTrackingCacheMapper: TimelineTrackingCachingMapper

    override fun executeJob(context: JobExecutionContext) {
        val ex = TimelineTrackingCachingExample()
        val lessThan40Days = LocalDate().minusDays(40)
        ex.createCriteria().andFordayLessThan(lessThan40Days.toDate())
        timeTrackingCacheMapper.deleteByExample(ex)
    }
}
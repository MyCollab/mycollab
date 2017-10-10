/**
 * mycollab-esb - Parent pom providing dependency and plugin management for applications
		built with Maven
 * Copyright © ${project.inceptionYear} MyCollab (support@mycollab.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.common.esb

import com.google.common.eventbus.AllowConcurrentEvents
import com.google.common.eventbus.Subscribe
import com.mycollab.common.dao.TimelineTrackingMapper
import com.mycollab.common.event.TimelineTrackingAdjustIfEntityDeleteEvent
import com.mycollab.module.esb.GenericCommand

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
class TimelineAdjustWithDeleteEntityCommand(private val timelineMapper: TimelineTrackingMapper) : GenericCommand() {

    @AllowConcurrentEvents
    @Subscribe
    fun execute(event: TimelineTrackingAdjustIfEntityDeleteEvent) {
        //    val lock = DistributionLockUtil.getLock("timeline-" + event.accountId)
//    try {
//      if (lock.tryLock(120, TimeUnit.SECONDS)) {
//        for (groupVal <- event.groupVals) {
//          val ex = new TimelineTrackingExample
//          ex.setOrderByClause("forDay DESC, id DESC")
//          val criteria = ex.createCriteria()
//          criteria.andSaccountidEqualTo(event.accountId).andTypeidEqualTo(event.typeId).
//            andFieldgroupEqualTo(groupVal).andExtratypeidEqualTo(event.extratypeid).andTypeEqualTo(event.typevar)
//          val items = timelineMapper.selectByExample(ex)
//          if (items.size() > 0) {
//            val item = items.get(0)
//            val minusTimeline = new TimelineTracking
//            minusTimeline.setType(event.typevar)
//            minusTimeline.setTypeid(event.typeId)
//            minusTimeline.setFieldgroup(groupVal)
//            minusTimeline.setFieldval(item.getFieldval)
//            minusTimeline.setExtratypeid(event.extratypeid)
//            minusTimeline.setSaccountid(event.accountId)
//            minusTimeline.setForday(new LocalDate().toDate)
//            minusTimeline.setFlag(Byte.box(-1))
//            timelineMapper.insert(minusTimeline)
//          }
//        }
//      }
//    } finally {
//      DistributionLockUtil.removeLock("timeline-" + event.accountId)
//      lock.unlock()
//    }
    }
}
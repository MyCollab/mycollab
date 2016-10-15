/**
 * This file is part of mycollab-esb.
 *
 * mycollab-esb is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-esb is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-esb.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.common.esb

import java.util.concurrent.TimeUnit

import com.google.common.eventbus.{AllowConcurrentEvents, Subscribe}
import com.mycollab.common.dao.TimelineTrackingMapper
import com.mycollab.common.domain.{TimelineTracking, TimelineTrackingExample}
import com.mycollab.common.event.TimelineTrackingAdjustIfEntityDeleteEvent
import com.mycollab.lock.DistributionLockUtil
import com.mycollab.module.esb.GenericCommand
import org.joda.time.LocalDate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
  * @author MyCollab Ltd
  * @since 5.2.7
  */
@Component class TimelineAdjustWithDeleteEntityCommand extends GenericCommand {
  @Autowired var timelineMapper: TimelineTrackingMapper = _

  @AllowConcurrentEvents
  @Subscribe
  def execute(event: TimelineTrackingAdjustIfEntityDeleteEvent): Unit = {
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

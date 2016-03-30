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
package com.esofthead.mycollab.module.common.esb.impl

import java.util.concurrent.TimeUnit

import com.esofthead.mycollab.common.dao.TimelineTrackingMapper
import com.esofthead.mycollab.common.domain.{TimelineTracking, TimelineTrackingExample}
import com.esofthead.mycollab.common.event.TimelineTrackingUpdateEvent
import com.esofthead.mycollab.lock.DistributionLockUtil
import com.esofthead.mycollab.module.GenericCommand
import com.google.common.eventbus.{AllowConcurrentEvents, Subscribe}
import org.joda.time.LocalDate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
  * @author MyCollab Ltd
  * @since 5.2.2
  */
@Component class TimelineTrackingUpdateCommand extends GenericCommand {
  @Autowired var timelineMapper: TimelineTrackingMapper = _

  @AllowConcurrentEvents
  @Subscribe
  def execute(event: TimelineTrackingUpdateEvent): Unit = {
    val lock = DistributionLockUtil.getLock("timeline-" + event.accountId)
    try {
      if (lock.tryLock(120, TimeUnit.SECONDS)) {
        val ex = new TimelineTrackingExample
        val criteria = ex.createCriteria()
        val now = new LocalDate()
        criteria.andTypeEqualTo(event.typevar).andTypeidEqualTo(event.typeId).
          andFieldgroupEqualTo(event.fieldgroup).andSaccountidEqualTo(event.accountId).andFlagEqualTo(Byte.box(1))
        ex.setOrderByClause("forDay DESC, id DESC")
        if (event.extratypeid != null) {
          criteria.andExtratypeidEqualTo(event.extratypeid)
        }
        val items = timelineMapper.selectByExample(ex)
        var isNew = true
        if (items != null && items.size() > 0) {
          val timeline = items.get(0)
          val forDay = new LocalDate(timeline.getForday)
          if (now.isEqual(forDay)) {
            if (event.fieldVal != timeline.getFieldval) {
              timeline.setFieldval(event.fieldVal)
              timelineMapper.updateByPrimaryKey(timeline)
              isNew = false
            }
          } else {
            val minusTimeline = new TimelineTracking
            minusTimeline.setType(event.typevar)
            minusTimeline.setTypeid(event.typeId)
            minusTimeline.setFieldgroup(event.fieldgroup)
            minusTimeline.setFieldval(timeline.getFieldval)
            minusTimeline.setExtratypeid(event.extratypeid)
            minusTimeline.setSaccountid(event.accountId)
            minusTimeline.setForday(now.toDate)
            minusTimeline.setFlag(Byte.box(-1))
            timelineMapper.insert(minusTimeline)
          }
        }
        if (isNew) {
          val timeline = new TimelineTracking
          timeline.setType(event.typevar)
          timeline.setTypeid(event.typeId)
          timeline.setFieldgroup(event.fieldgroup)
          timeline.setFieldval(event.fieldVal)
          timeline.setExtratypeid(event.extratypeid)
          timeline.setSaccountid(event.accountId)
          timeline.setForday(now.toDate)
          timeline.setFlag(Byte.box(1))
          timelineMapper.insert(timeline)
        }
      }
    } finally {
      DistributionLockUtil.removeLock("timeline-" + event.accountId)
      lock.unlock()
    }
  }
}

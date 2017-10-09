package com.mycollab.module.common.esb

import com.google.common.eventbus.AllowConcurrentEvents
import com.google.common.eventbus.Subscribe
import com.mycollab.common.dao.TimelineTrackingMapper
import com.mycollab.common.event.TimelineTrackingUpdateEvent
import com.mycollab.module.esb.GenericCommand

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
class TimelineTrackingUpdateCommand(private val timelineMapper: TimelineTrackingMapper)  : GenericCommand() {
    @AllowConcurrentEvents
    @Subscribe
    fun execute(event: TimelineTrackingUpdateEvent){
//        if (event.fieldVal == null) {
//            return
//        }
//        val lock = DistributionLockUtil.getLock("timeline-" + event.accountId)
//        try {
//            if (lock.tryLock(120, TimeUnit.SECONDS)) {
//                val ex = new TimelineTrackingExample
//                val criteria = ex.createCriteria()
//                val now = new LocalDate()
//                criteria.andTypeEqualTo(event.typevar).andTypeidEqualTo(event.typeId).
//                        andFieldgroupEqualTo(event.fieldgroup).andSaccountidEqualTo(event.accountId).andFlagEqualTo(Byte.box(1))
//                ex.setOrderByClause("forDay DESC, id DESC")
//                if (event.extratypeid != null) {
//                    criteria.andExtratypeidEqualTo(event.extratypeid)
//                }
//                val items = timelineMapper.selectByExample(ex)
//                var isNew = true
//                if (items != null && items.size() > 0) {
//                    val timeline = items.get(0)
//                    val forDay = new LocalDate(timeline.getForday)
//                    if (now.isEqual(forDay)) {
//                        if (event.fieldVal != timeline.getFieldval) {
//                            timeline.setFieldval(event.fieldVal)
//                            timelineMapper.updateByPrimaryKey(timeline)
//                            isNew = false
//                        }
//                    } else {
//                        val minusTimeline = new TimelineTracking
//                                minusTimeline.setType(event.typevar)
//                        minusTimeline.setTypeid(event.typeId)
//                        minusTimeline.setFieldgroup(event.fieldgroup)
//                        minusTimeline.setFieldval(timeline.getFieldval)
//                        minusTimeline.setExtratypeid(event.extratypeid)
//                        minusTimeline.setSaccountid(event.accountId)
//                        minusTimeline.setForday(now.toDate)
//                        minusTimeline.setFlag(Byte.box(-1))
//                        timelineMapper.insert(minusTimeline)
//                    }
//                }
//                if (isNew) {
//                    val timeline = new TimelineTracking
//                            timeline.setType(event.typevar)
//                    timeline.setTypeid(event.typeId)
//                    timeline.setFieldgroup(event.fieldgroup)
//                    timeline.setFieldval(event.fieldVal)
//                    timeline.setExtratypeid(event.extratypeid)
//                    timeline.setSaccountid(event.accountId)
//                    timeline.setForday(now.toDate)
//                    timeline.setFlag(Byte.box(1))
//                    timelineMapper.insert(timeline)
//                }
//            }
//        } finally {
//            DistributionLockUtil.removeLock("timeline-" + event.accountId)
//            lock.unlock()
//        }
    }
}
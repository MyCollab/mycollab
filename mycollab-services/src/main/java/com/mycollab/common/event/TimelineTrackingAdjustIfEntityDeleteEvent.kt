package com.mycollab.common.event

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
class TimelineTrackingAdjustIfEntityDeleteEvent(val typevar: String, val typeId: Int, val groupVals: Array<String>,
                                                val extratypeid: Int, val accountId: Int)
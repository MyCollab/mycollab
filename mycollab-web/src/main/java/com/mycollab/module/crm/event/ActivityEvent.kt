package com.mycollab.module.crm.event

import com.mycollab.vaadin.event.ApplicationEvent

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
object ActivityEvent {
    class GotoCalendar(source: Any, val data: Any?) : ApplicationEvent(source)

    class GotoTodoList(source: Any, val data: Any?) : ApplicationEvent(source)

    class TaskAdd(source: Any, val data: Any?) : ApplicationEvent(source)

    class TaskEdit(source: Any, val data: Any?) : ApplicationEvent(source)

    class TaskRead(source: Any, val data: Any?) : ApplicationEvent(source)

    class MeetingAdd(source: Any, val data: Any?) : ApplicationEvent(source)

    class MeetingEdit(source: Any, val data: Any?) : ApplicationEvent(source)

    class MeetingRead(source: Any, val data: Any?) : ApplicationEvent(source)

    class CallAdd(source: Any, val data: Any?) : ApplicationEvent(source)

    class CallEdit(source: Any, val data: Any?) : ApplicationEvent(source)

    class CallRead(source: Any, val data: Any?) : ApplicationEvent(source)
}
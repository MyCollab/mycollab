package com.mycollab.module.project.event

import com.mycollab.vaadin.event.ApplicationEvent

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
object MilestoneEvent {
    class GotoAdd(source: Any, val data: Any?) : ApplicationEvent(source)

    class GotoEdit(source: Any, val data: Any?) : ApplicationEvent(source)

    class GotoList(source: Any, val data: Any?) : ApplicationEvent(source)

    class GotoRead(source: Any, val data: Any?) : ApplicationEvent(source)

    class GotoRoadmap(source: Any) : ApplicationEvent(source)

    class GotoKanban(source: Any) : ApplicationEvent(source)

    class NewMilestoneAdded(source: Any, val data: Int) : ApplicationEvent(source)

    class MilestoneDeleted(source: Any, val data: Int) : ApplicationEvent(source)
}
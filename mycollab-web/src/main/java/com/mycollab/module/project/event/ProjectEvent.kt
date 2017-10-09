package com.mycollab.module.project.event

import com.mycollab.vaadin.event.ApplicationEvent
import com.mycollab.module.project.domain.ProjectGenericItem

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
object ProjectEvent {
    class GotoAdd(source: Any, val data: Any?) : ApplicationEvent(source)

    class GotoEdit(source: Any, val data: Any?) : ApplicationEvent(source)

    class GotoList(source: Any, val data: Any?) : ApplicationEvent(source)

    class GotoMyProject(source: Any, val data: Any?) : ApplicationEvent(source)

    class GotoTagListView(source: Any, val data: Any?) : ApplicationEvent(source)

    class GotoFavoriteView(source: Any, val data: Any?) : ApplicationEvent(source)

    class SelectFavoriteItem(source: Any, val data: ProjectGenericItem) : ApplicationEvent(source)

    class TimeLoggingChangedEvent(source: Any) : ApplicationEvent(source)

    class GotoCalendarView(source: Any) : ApplicationEvent(source)

    class GotoGanttChart(source: Any, val data: Any?) : ApplicationEvent(source)

    class GotoDashboard(source: Any, val data: Any?) : ApplicationEvent(source)

    class GotoUserDashboard(source: Any, val data: Any?) : ApplicationEvent(source)
}
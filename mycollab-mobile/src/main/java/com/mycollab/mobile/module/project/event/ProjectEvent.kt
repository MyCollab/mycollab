package com.mycollab.mobile.module.project.event

import com.mycollab.vaadin.event.ApplicationEvent

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
object ProjectEvent {
    class GotoAdd(source: Any, val data: Any?) : ApplicationEvent(source)

    class GotoProjectList(source: Any, val data: Any?) : ApplicationEvent(source)

    class GotoMyProject(source: Any, val data: Any?) : ApplicationEvent(source)

    class MyProjectActivities(source: Any, val projectId: Int) : ApplicationEvent(source)

    class GotoAllActivitiesView(source: Any, val data: Any?) : ApplicationEvent(source)

    class GotoDashboard(source: Any, val data: Any?) : ApplicationEvent(source)
}
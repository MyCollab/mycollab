package com.mycollab.module.project.event

import com.mycollab.module.project.domain.criteria.TaskSearchCriteria
import com.mycollab.vaadin.event.ApplicationEvent

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
object TaskEvent {
    class SearchRequest(source: Any, val data: TaskSearchCriteria) : ApplicationEvent(source)

    class NewTaskAdded(source: Any, val data: Int) : ApplicationEvent(source)

    class TaskDeleted(source: Any, val data: Int) : ApplicationEvent(source)

    class RemoveParentRelationship(source: Any, val data: Int) : ApplicationEvent(source)

    class Search(source: Any, val data: Any?) : ApplicationEvent(source)

    class GotoAdd(source: Any, val data: Any?) : ApplicationEvent(source)

    class GotoEdit(source: Any, val data: Any?) : ApplicationEvent(source)

    class GotoRead(source: Any, val data: Any?) : ApplicationEvent(source)

    class GotoKanbanView(source: Any, val data: Any?) : ApplicationEvent(source)
}
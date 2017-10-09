package com.mycollab.module.project.view.parameters

import com.mycollab.module.project.domain.Task
import com.mycollab.vaadin.mvp.ScreenData

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
object TaskScreenData {
    class Read(param: Int) : ScreenData<Int>(param) 

    class Edit(param: Task) : ScreenData<Task>(param)

    class Add(param: Task) : ScreenData<Task>(param) 

    class GotoKanbanView : ScreenData<Any>(null)
}
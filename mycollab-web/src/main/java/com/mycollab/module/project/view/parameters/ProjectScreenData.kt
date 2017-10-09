package com.mycollab.module.project.view.parameters

import com.mycollab.module.project.domain.Project
import com.mycollab.vaadin.mvp.ScreenData

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
object ProjectScreenData {
    class GotoList : ScreenData<Any>(null)

    class Goto(params: Int) : ScreenData<Int>(params)

    class Edit(params: Project) : ScreenData<Project>(params)

    class GotoTagList(params: Any?) : ScreenData<Any>(params)

    class GotoFavorite() : ScreenData<Any>(null)

    class SearchItem(params: String) : ScreenData<String>(params)

    class GotoGanttChart : ScreenData<Any>(null)

    class GotoReportConsole : ScreenData<Any>(null)

    class GotoCalendarView : ScreenData<Any>(null)
}
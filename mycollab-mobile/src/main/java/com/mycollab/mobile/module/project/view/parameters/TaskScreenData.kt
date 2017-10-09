package com.mycollab.mobile.module.project.view.parameters

import com.mycollab.module.project.domain.SimpleTask
import com.mycollab.vaadin.mvp.ScreenData

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
object TaskScreenData {
    class Read(param: Int) : ScreenData<Int>(param) {}

    class Edit(param: SimpleTask) : ScreenData<SimpleTask>(param)

    class Add(param: SimpleTask) : ScreenData<SimpleTask>(param)
}
package com.mycollab.mobile.module.project.view.parameters

import com.mycollab.module.tracker.domain.BugWithBLOBs
import com.mycollab.vaadin.mvp.ScreenData

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
object BugScreenData {
    class Read(param: Int) : ScreenData<Int>(param)

    class Add(param: BugWithBLOBs) : ScreenData<BugWithBLOBs>(param)

    class Edit(param: BugWithBLOBs) : ScreenData<BugWithBLOBs>(param)
}
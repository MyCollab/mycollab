package com.mycollab.module.project.view.parameters

import com.mycollab.module.tracker.domain.BugWithBLOBs
import com.mycollab.vaadin.mvp.ScreenData

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
object BugScreenData {
    class Read(params: Int) : ScreenData<Int>(params) 

    class Add(params: BugWithBLOBs) : ScreenData<BugWithBLOBs>(params) 

    class Edit(params: BugWithBLOBs) : ScreenData<BugWithBLOBs>(params) 
}
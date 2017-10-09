package com.mycollab.mobile.module.project.view.parameters

import com.mycollab.module.project.domain.Risk
import com.mycollab.vaadin.mvp.ScreenData

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
object RiskScreenData {
    class Read(param: Int) : ScreenData<Int>(param)

    class Add(param: Risk) : ScreenData<Risk>(param)

    class Edit(param: Risk) : ScreenData<Risk>(param)
}
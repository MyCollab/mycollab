package com.mycollab.module.project.view.parameters

import com.mycollab.module.project.domain.Risk
import com.mycollab.vaadin.mvp.ScreenData

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
object RiskScreenData {
    class Read(params: Int) : ScreenData<Int>(params)

    class Add(param: Risk) : ScreenData<Risk>(param)

    class Edit(param: Risk) : ScreenData<Risk>(param)
}
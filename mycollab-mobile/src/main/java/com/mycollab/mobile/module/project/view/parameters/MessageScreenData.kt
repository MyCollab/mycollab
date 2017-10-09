package com.mycollab.mobile.module.project.view.parameters

import com.mycollab.module.project.domain.criteria.MessageSearchCriteria
import com.mycollab.vaadin.mvp.ScreenData

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
object MessageScreenData {
    class Add : ScreenData<Any>(null)

    class Read(param: Int) : ScreenData<Int>(param)

    class Search(param: MessageSearchCriteria) : ScreenData<MessageSearchCriteria>(param)
}
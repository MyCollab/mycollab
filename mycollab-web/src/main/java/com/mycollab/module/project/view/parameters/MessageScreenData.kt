package com.mycollab.module.project.view.parameters

import com.mycollab.module.project.domain.criteria.MessageSearchCriteria
import com.mycollab.vaadin.mvp.ScreenData

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
object MessageScreenData {
    class Read(params: Int) : ScreenData<Int>(params)

    class Search(params: MessageSearchCriteria) : ScreenData<MessageSearchCriteria>(params)
}
package com.mycollab.module.project.view.parameters

import com.mycollab.module.page.domain.Page
import com.mycollab.vaadin.mvp.ScreenData

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
object PageScreenData {
    class Read(params: Page) : ScreenData<Page>(params)

    class Add(params: Page) : ScreenData<Page>(params)

    class Edit(params: Page) : ScreenData<Page>(params)

    class Search(params: String) : ScreenData<String>(params)
}
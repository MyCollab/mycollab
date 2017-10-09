package com.mycollab.module.project.view.page

import com.mycollab.module.page.domain.PageResource
import com.mycollab.vaadin.mvp.PageView

/**
 * @author MyCollab Ltd.
 * @since 4.4.0
 */
interface PageListView : PageView {
    /**
     * @param resources
     */
    fun displayDefaultPages(resources: List<PageResource>)

    fun showNoItemView()
}

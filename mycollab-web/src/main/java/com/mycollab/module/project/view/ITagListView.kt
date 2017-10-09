package com.mycollab.module.project.view

import com.mycollab.common.domain.Tag
import com.mycollab.vaadin.mvp.PageView

/**
 * @author MyCollab Ltd
 * @since 5.2.9
 */
interface ITagListView : PageView {
    fun displayTags(tag: Tag)
}

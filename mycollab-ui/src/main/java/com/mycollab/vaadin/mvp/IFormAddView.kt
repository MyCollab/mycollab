package com.mycollab.vaadin.mvp

import com.mycollab.vaadin.event.HasEditFormHandlers

/**
 * @param <B>
 * @author MyCollab Ltd.
 * @since 1.0
</B> */
interface IFormAddView<B> : PageView {
    /**
     * @param item
     */
    fun editItem(item: B)

    /**
     * @return
     */
    val editFormHandlers: HasEditFormHandlers<B>
}

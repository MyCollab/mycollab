package com.mycollab.vaadin.mvp

/**
 * @param <T>
 * @author MyCollab Ltd.
 * @since 1.0
</T> */
interface IPreviewView<T> : PageView {
    fun previewItem(item: T)

    val item: T
}

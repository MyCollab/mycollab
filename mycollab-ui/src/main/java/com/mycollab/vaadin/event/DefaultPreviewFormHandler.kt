package com.mycollab.vaadin.event

import com.mycollab.core.MyCollabException

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
open class DefaultPreviewFormHandler<T> : PreviewFormHandler<T> {
    override fun gotoNext(data: T) {}

    override fun gotoPrevious(data: T) {}

    override fun onEdit(data: T) {}

    override fun onDelete(data: T) {}

    override fun onPrint(source: Any, data: T) {}

    override fun onClone(data: T) {}

    override fun onCancel() {}

    override fun onAssign(data: T) {}

    override fun onAdd(data: T) {}

    override fun onExtraAction(action: String, data: T) {
        throw MyCollabException("Must be override by sub class")
    }
}
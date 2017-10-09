package com.mycollab.vaadin.event

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
interface PreviewFormHandler<T> {
    /**
     *
     * @param data
     */
    fun gotoNext(data: T)

    /**
     * @param data
     */
    fun gotoPrevious(data: T)

    /**
     * @param data
     */
    fun onAssign(data: T)

    /**
     * @param data
     */
    fun onEdit(data: T)

    /**
     *
     * @param data
     */
    fun onAdd(data: T)

    /**
     *
     * @param data
     */
    fun onDelete(data: T)

    fun onPrint(source: Any, data: T)

    /**
     * @param data
     */
    fun onClone(data: T)

    /**
     *
     */
    fun onCancel()

    /**
     * @param action
     * @param data
     */
    fun onExtraAction(action: String, data: T)
}
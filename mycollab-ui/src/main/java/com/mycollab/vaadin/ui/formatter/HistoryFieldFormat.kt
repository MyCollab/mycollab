package com.mycollab.vaadin.ui.formatter

/**
 * @author MyCollab Ltd.
 * @since 4.0
 */
interface HistoryFieldFormat {

    /**
     * @param value
     * @return
     */
    fun toString(value: String): String

    /**
     *
     * @param value
     * @param msgIfBlank
     * @return
     */
    fun toString(value: String, displayAsHtml: Boolean?, msgIfBlank: String): String
}

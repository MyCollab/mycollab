package com.mycollab.module.mail.service

import java.util.*

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
interface IContentGenerator {
    /**
     *
     * @param key
     * @param value
     */
    fun putVariable(key: String, value: Any)

    /**
     *
     * @param templateFilePath
     * @return
     */
    fun parseFile(templateFilePath: String): String

    /**
     *
     * @param templateFilePath
     * @param currentLocale
     * @return
     */
    fun parseFile(templateFilePath: String, currentLocale: Locale?): String
}
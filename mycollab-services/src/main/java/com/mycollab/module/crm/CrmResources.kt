package com.mycollab.module.crm

import com.mycollab.core.MyCollabException
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import java.lang.reflect.Method

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
object CrmResources {
    private val LOG = LoggerFactory.getLogger(CrmResources::class.java)
    private var getResMethod: Method? = null

    init {
        try {
            val resourceCls = Class.forName("com.mycollab.module.crm.ui.CrmAssetsManager")
            getResMethod = resourceCls.getMethod("toHexString", String::class.java)
        } catch (e: Exception) {
            throw MyCollabException("Can not reload resource", e)
        }

    }

    fun getFontIconHtml(type: String): String {
        try {
            val codePoint = getResMethod!!.invoke(null, type) as String
            return String.format("<span class=\"v-icon\" style=\"font-family: FontAwesome;\">%s;</span>", codePoint)
        } catch (e: Exception) {
            LOG.error("Can not get resource type {}", type)
            return ""
        }

    }
}

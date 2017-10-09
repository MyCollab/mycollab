package com.mycollab.vaadin

import com.mycollab.configuration.SiteConfiguration
import com.vaadin.server.VaadinRequest
import com.vaadin.server.VaadinServletRequest

/**
 * @author MyCollab Ltd
 * @since 5.0.10
 */
object Utils {
    @JvmStatic fun getSubDomain(request: VaadinRequest): String {
        val servletRequest = request as VaadinServletRequest
        return when {
            SiteConfiguration.isDemandEdition() -> servletRequest.serverName.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]
            else -> servletRequest.serverName
        }
    }

    @JvmStatic fun isTablet(request: VaadinRequest): Boolean {
        return try {
            val userAgent = request.getHeader("user-agent").toLowerCase()
            userAgent.contains("ipad")
        } catch (e: Exception) {
            false
        }

    }
}

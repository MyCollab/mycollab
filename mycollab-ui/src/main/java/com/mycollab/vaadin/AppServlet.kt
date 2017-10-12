/**
 * Copyright © MyCollab
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.vaadin

import com.mycollab.core.Version
import com.vaadin.addon.touchkit.annotations.CacheManifestEnabled
import com.vaadin.addon.touchkit.server.TouchKitServlet
import com.vaadin.server.DeploymentConfiguration
import java.util.*
import javax.servlet.ServletException
import javax.servlet.annotation.WebInitParam
import javax.servlet.annotation.WebServlet

/**
 * @author MyCollab Ltd.
 * @since 3.0
 */
@WebServlet(name = "MyCollabApplication", urlPatterns = arrayOf("/*"), asyncSupported = true, loadOnStartup = 0, initParams = arrayOf(WebInitParam(name = "closeIdleSessions", value = "false"), WebInitParam(name = "productionMode", value = "true"), WebInitParam(name = "resourceCacheTime", value = "8640000"), WebInitParam(name = "maxIdleTime", value = "10000"), WebInitParam(name = "org.atmosphere.websocket.maxIdleTime", value = "86400000")))
@CacheManifestEnabled(false)
class AppServlet : TouchKitServlet() {

    private val uiProvider = AppUIProvider()
    private val bootstrapListener = AppBootstrapListener()

    override fun createDeploymentConfiguration(initParameters: Properties): DeploymentConfiguration {
        initParameters.setProperty("productionMode", "true")
        return super.createDeploymentConfiguration(initParameters)
    }

    @Throws(ServletException::class)
    override fun servletInitialized() {
        super.servletInitialized()
        val s = touchKitSettings
        s.webAppSettings.isWebAppCapable = true
        val contextPath = servletConfig.servletContext.contextPath
        s.applicationIcons.addApplicationIcon(contextPath + "VAADIN/themes/" + Version.THEME_MOBILE_VERSION + "/icons/icon.png")
        s.webAppSettings.startupImage = contextPath + "VAADIN/themes/" + Version.THEME_MOBILE_VERSION + "/icons/icon.png"

        service.addSessionInitListener { sessionInitEvent ->
            sessionInitEvent.session.addBootstrapListener(bootstrapListener)
            sessionInitEvent.session.addUIProvider(uiProvider)
        }
    }
}

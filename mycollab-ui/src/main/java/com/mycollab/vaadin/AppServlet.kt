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

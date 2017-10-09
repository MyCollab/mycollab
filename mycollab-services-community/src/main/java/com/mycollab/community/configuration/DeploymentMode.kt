package com.mycollab.community.configuration

import com.mycollab.configuration.IDeploymentMode
import com.mycollab.configuration.ServerConfiguration
import com.mycollab.configuration.SiteConfiguration
import org.springframework.context.annotation.Profile
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component

/**
 * @author MyCollab Ltd
 * @since 5.2.2
 */
@Component
@Profile("production")
@Order(value = 1)
class DeploymentMode(private val serverConfiguration: ServerConfiguration) : IDeploymentMode {

    override val isDemandEdition: Boolean
        get() = false

    override val isCommunityEdition: Boolean
        get() = true

    override val isPremiumEdition: Boolean
        get() = false

    override fun getSiteUrl(subDomain: String?): String =
            "${SiteConfiguration.getServerAddress()}:${serverConfiguration.port}/"

    override fun getResourceDownloadUrl(): String = "http://${SiteConfiguration.getServerAddress()}:${serverConfiguration.port}/file/"

    override fun getCdnUrl(): String = "http://${SiteConfiguration.getServerAddress()}:${serverConfiguration.port}/assets/"
}

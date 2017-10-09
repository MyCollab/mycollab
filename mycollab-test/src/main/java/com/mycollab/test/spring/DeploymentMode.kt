package com.mycollab.test.spring

import com.mycollab.configuration.IDeploymentMode
import org.springframework.context.annotation.Profile
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component

/**
 * @author MyCollab Ltd
 * @since 5.2.2
 */
@Component
@Profile("test")
@Order(value = 10)
class DeploymentMode : IDeploymentMode {
    override val isDemandEdition: Boolean
        get() = false

    override val isCommunityEdition: Boolean
        get() = true

    override val isPremiumEdition: Boolean
        get() = false

    override fun getSiteUrl(subDomain: String?): String = ""

    override fun getResourceDownloadUrl(): String  = ""

    override fun getCdnUrl(): String  = ""
}

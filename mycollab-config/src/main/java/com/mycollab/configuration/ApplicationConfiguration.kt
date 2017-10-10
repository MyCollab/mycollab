package com.mycollab.configuration

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

/**
 * @author MyCollab Ltd
 * @since 5.5.0
 */
@Component
@Profile("production", "test")
@ConfigurationProperties(prefix = "app")
open class ApplicationConfiguration(var description: String? = "", var facebookUrl: String? = "",
                               var twitterUrl: String? = "", var googleUrl: String? = "",
                               var linkedinUrl: String? = "") {

    fun defaultUrls(): Map<String, String> =
            mutableMapOf<String, String>("facebook_url" to (facebookUrl ?: ""),
                    "google_url" to (googleUrl ?: ""),
                    "linkedin_url" to (linkedinUrl ?: ""),
                    "twitter_url" to (twitterUrl ?: ""))
}

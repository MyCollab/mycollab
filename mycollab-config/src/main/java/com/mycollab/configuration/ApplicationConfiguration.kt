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

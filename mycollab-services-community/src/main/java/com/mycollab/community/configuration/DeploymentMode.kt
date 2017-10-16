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
package com.mycollab.community.configuration

import com.mycollab.configuration.IDeploymentMode
import com.mycollab.configuration.ServerConfiguration
import com.mycollab.configuration.SiteConfiguration
import com.mycollab.db.persistence.service.IService
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
class DeploymentMode(private val serverConfiguration: ServerConfiguration) : IDeploymentMode, IService {

    override val isDemandEdition: Boolean
        get() = false

    override val isCommunityEdition: Boolean
        get() = false

    override val isPremiumEdition: Boolean
        get() = true

    override fun getSiteUrl(subDomain: String?): String = String.format(serverConfiguration.siteUrl, SiteConfiguration.getServerAddress(), serverConfiguration.port)

    override fun getResourceDownloadUrl(): String = String.format(serverConfiguration.resourceDownloadUrl, SiteConfiguration.getServerAddress(), serverConfiguration.port)

    override fun getCdnUrl(): String = String.format(serverConfiguration.cdnUrl, SiteConfiguration.getServerAddress(), serverConfiguration.port)
}

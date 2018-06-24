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
 * along with this program.  If not, see <http:></http:>//www.gnu.org/licenses/>.
 */
package com.mycollab.community.module.file.service

import com.mycollab.cache.IgnoreCacheClass
import com.mycollab.db.persistence.service.IService
import com.mycollab.module.file.service.RawContentService
import com.mycollab.module.file.service.impl.FileRawContentServiceImpl
import org.springframework.beans.factory.config.AbstractFactoryBean
import org.springframework.stereotype.Service

/**
 * Factory spring bean to solve resolution of MyCollab raw content service
 * should be `FileRawContentServiceImpl` if MyCollab is installed in
 * local server (dev, community or premium mode) or
 * `AmazonRawContentServiceImpl` if MyCollab is installed on MyCollab
 * server.
 *
 * @author MyCollab Ltd
 * @since 1.0.0
 */
@Service
@IgnoreCacheClass
class RawContentServiceFactoryBean : AbstractFactoryBean<RawContentService>(), IService {

    @Throws(Exception::class)
    override fun createInstance() = FileRawContentServiceImpl()

    override fun getObjectType() = RawContentService::class.java
}

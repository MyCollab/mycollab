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
package com.mycollab.community.module.ecm.service.impl

import com.mycollab.db.persistence.ICrudGenericDAO
import com.mycollab.db.persistence.service.DefaultCrudService
import com.mycollab.module.ecm.dao.ExternalDriveMapper
import com.mycollab.module.ecm.domain.ExternalDrive
import com.mycollab.module.ecm.service.ExternalDriveService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

import java.util.ArrayList

/**
 * @author MyCollab Ltd
 * @since 1.0.0
 */
@Service
class ExternalDriveServiceImpl(private val externalDriveMapper: ExternalDriveMapper) : DefaultCrudService<Int, ExternalDrive>(), ExternalDriveService {

    override val crudMapper: ICrudGenericDAO<Int, ExternalDrive>
        get() = externalDriveMapper as ICrudGenericDAO<Int, ExternalDrive>

    override fun getExternalDrivesOfUser(username: String): List<ExternalDrive> = listOf()
}

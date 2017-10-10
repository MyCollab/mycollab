/**
 * mycollab-services - Parent pom providing dependency and plugin management for applications
		built with Maven
 * Copyright © ${project.inceptionYear} MyCollab (support@mycollab.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.project.service.impl

import com.mycollab.db.persistence.ICrudGenericDAO
import com.mycollab.db.persistence.service.DefaultCrudService
import com.mycollab.module.project.dao.ProjectCustomizeViewMapper
import com.mycollab.module.project.domain.ProjectCustomizeView
import com.mycollab.module.project.service.ProjectCustomizeViewService
import org.springframework.stereotype.Service

/**
 * @author MyCollab Ltd.
 * @since 4.4.0
 */
@Service
class ProjectCustomizeViewServiceImpl(private val projectCustomizeMapper: ProjectCustomizeViewMapper) : DefaultCrudService<Int, ProjectCustomizeView>(), ProjectCustomizeViewService {

    override val crudMapper: ICrudGenericDAO<Int, ProjectCustomizeView>
        get() = projectCustomizeMapper as ICrudGenericDAO<Int, ProjectCustomizeView>

}

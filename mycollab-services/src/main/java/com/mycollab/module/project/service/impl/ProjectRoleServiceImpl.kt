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
package com.mycollab.module.project.service.impl

import com.mycollab.aspect.ClassInfo
import com.mycollab.aspect.ClassInfoMap
import com.mycollab.common.ModuleNameConstants
import com.mycollab.core.utils.JsonDeSerializer
import com.mycollab.db.persistence.ICrudGenericDAO
import com.mycollab.db.persistence.ISearchableDAO
import com.mycollab.db.persistence.service.DefaultService
import com.mycollab.module.project.ProjectTypeConstants
import com.mycollab.module.project.dao.ProjectRoleMapper
import com.mycollab.module.project.dao.ProjectRoleMapperExt
import com.mycollab.module.project.dao.ProjectRolePermissionMapper
import com.mycollab.module.project.domain.ProjectRole
import com.mycollab.module.project.domain.ProjectRolePermission
import com.mycollab.module.project.domain.ProjectRolePermissionExample
import com.mycollab.module.project.domain.SimpleProjectRole
import com.mycollab.module.project.domain.criteria.ProjectRoleSearchCriteria
import com.mycollab.module.project.service.ProjectRoleService
import com.mycollab.security.PermissionMap
import org.springframework.stereotype.Service

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@Service
open class ProjectRoleServiceImpl(private val roleMapper: ProjectRoleMapper,
                             private val roleMapperExt: ProjectRoleMapperExt,
                             private val projectRolePermissionMapper: ProjectRolePermissionMapper) : DefaultService<Int, ProjectRole, ProjectRoleSearchCriteria>(), ProjectRoleService {

    override val crudMapper: ICrudGenericDAO<Int, ProjectRole>
        get() = roleMapper as ICrudGenericDAO<Int, ProjectRole>

    override val searchMapper: ISearchableDAO<ProjectRoleSearchCriteria>
        get() = roleMapperExt

    override fun savePermission(projectId: Int, roleId: Int?, permissionMap: PermissionMap, sAccountId: Int) {
        val perVal = JsonDeSerializer.toJson(permissionMap)

        val ex = ProjectRolePermissionExample()
        ex.createCriteria().andRoleidEqualTo(roleId)

        val rolePer = ProjectRolePermission()
        rolePer.roleid = roleId
        rolePer.projectid = projectId
        rolePer.roleval = perVal

        val data = projectRolePermissionMapper.countByExample(ex)
        when {
            data > 0 -> projectRolePermissionMapper.updateByExampleSelective(rolePer, ex)
            else -> projectRolePermissionMapper.insert(rolePer)
        }
    }

    override fun findById(roleId: Int, sAccountId: Int): SimpleProjectRole? = roleMapperExt.findRoleById(roleId)

    companion object {

        init {
            ClassInfoMap.put(ProjectRoleServiceImpl::class.java, ClassInfo(ModuleNameConstants.PRJ, ProjectTypeConstants.PROJECT_ROLE))
        }
    }
}

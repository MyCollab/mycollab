/**
 * This file is part of mycollab-services.
 *
 * mycollab-services is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-services is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-services.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.module.project.service.ibatis;

import com.esofthead.mycollab.common.interceptor.aspect.ClassInfo;
import com.esofthead.mycollab.common.interceptor.aspect.ClassInfoMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.esofthead.mycollab.common.ModuleNameConstants;
import com.esofthead.mycollab.common.interceptor.aspect.Auditable;
import com.esofthead.mycollab.core.persistence.ICrudGenericDAO;
import com.esofthead.mycollab.core.persistence.ISearchableDAO;
import com.esofthead.mycollab.core.persistence.service.DefaultService;
import com.esofthead.mycollab.core.utils.JsonDeSerializer;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.dao.ProjectRoleMapper;
import com.esofthead.mycollab.module.project.dao.ProjectRoleMapperExt;
import com.esofthead.mycollab.module.project.dao.ProjectRolePermissionMapper;
import com.esofthead.mycollab.module.project.domain.ProjectRole;
import com.esofthead.mycollab.module.project.domain.ProjectRolePermission;
import com.esofthead.mycollab.module.project.domain.ProjectRolePermissionExample;
import com.esofthead.mycollab.module.project.domain.SimpleProjectRole;
import com.esofthead.mycollab.module.project.domain.criteria.ProjectRoleSearchCriteria;
import com.esofthead.mycollab.module.project.service.ProjectRoleService;
import com.esofthead.mycollab.security.PermissionMap;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
@Service
@Auditable()
public class ProjectRoleServiceImpl extends
		DefaultService<Integer, ProjectRole, ProjectRoleSearchCriteria>
		implements ProjectRoleService {

    static {
        ClassInfoMap.put(ProjectRoleServiceImpl.class, new ClassInfo(ModuleNameConstants.PRJ, ProjectTypeConstants.PROJECT_ROLE));
    }

	@Autowired
	private ProjectRoleMapper roleMapper;

	@Autowired
	private ProjectRoleMapperExt roleMapperExt;

	@Autowired
	private ProjectRolePermissionMapper projectRolePermissionMapper;

	@Override
	public ICrudGenericDAO<Integer, ProjectRole> getCrudMapper() {
		return roleMapper;
	}

	@Override
	public ISearchableDAO<ProjectRoleSearchCriteria> getSearchMapper() {
		return roleMapperExt;
	}

	@Override
	public void savePermission(int projectId, int roleId,
			PermissionMap permissionMap, Integer sAccountId) {
		String perVal = JsonDeSerializer.toJson(permissionMap);

		ProjectRolePermissionExample ex = new ProjectRolePermissionExample();
		ex.createCriteria().andRoleidEqualTo(roleId);

		ProjectRolePermission rolePer = new ProjectRolePermission();
		rolePer.setRoleid(roleId);
		rolePer.setProjectid(projectId);
		rolePer.setRoleval(perVal);

		int data = projectRolePermissionMapper.countByExample(ex);
		if (data > 0) {
			projectRolePermissionMapper.updateByExampleSelective(rolePer, ex);
		} else {
			projectRolePermissionMapper.insert(rolePer);
		}

	}

	@Override
	public SimpleProjectRole findById(int roleId, int sAccountId) {
		return roleMapperExt.findRoleById(roleId);
	}
}

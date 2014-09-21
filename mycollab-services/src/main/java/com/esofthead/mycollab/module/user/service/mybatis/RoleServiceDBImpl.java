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
package com.esofthead.mycollab.module.user.service.mybatis;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.esofthead.mycollab.core.cache.CacheKey;
import com.esofthead.mycollab.core.persistence.ICrudGenericDAO;
import com.esofthead.mycollab.core.persistence.ISearchableDAO;
import com.esofthead.mycollab.core.persistence.service.DefaultService;
import com.esofthead.mycollab.module.user.dao.RoleMapper;
import com.esofthead.mycollab.module.user.dao.RoleMapperExt;
import com.esofthead.mycollab.module.user.dao.RolePermissionMapper;
import com.esofthead.mycollab.module.user.domain.Role;
import com.esofthead.mycollab.module.user.domain.RoleExample;
import com.esofthead.mycollab.module.user.domain.RolePermission;
import com.esofthead.mycollab.module.user.domain.RolePermissionExample;
import com.esofthead.mycollab.module.user.domain.SimpleRole;
import com.esofthead.mycollab.module.user.domain.criteria.RoleSearchCriteria;
import com.esofthead.mycollab.module.user.service.RoleService;
import com.esofthead.mycollab.security.PermissionMap;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
@Service
public class RoleServiceDBImpl extends
		DefaultService<Integer, Role, RoleSearchCriteria> implements
		RoleService {

	@Autowired
	private RoleMapper roleMapper;
	@Autowired
	private RoleMapperExt roleMapperExt;

	@Autowired
	private RolePermissionMapper rolePermissionMapper;

	@Override
	public ICrudGenericDAO<Integer, Role> getCrudMapper() {
		return roleMapper;
	}

	@Override
	public ISearchableDAO<RoleSearchCriteria> getSearchMapper() {
		return roleMapperExt;
	}

	@Override
	public void savePermission(int roleId, PermissionMap permissionMap,
			int accountid) {
		String perVal = permissionMap.toJsonString();

		RolePermissionExample ex = new RolePermissionExample();
		ex.createCriteria().andRoleidEqualTo(roleId);

		RolePermission rolePer = new RolePermission();
		rolePer.setRoleid(roleId);
		rolePer.setRoleval(perVal);

		int data = rolePermissionMapper.countByExample(ex);
		if (data > 0) {
			rolePermissionMapper.updateByExampleSelective(rolePer, ex);
		} else {
			rolePermissionMapper.insert(rolePer);
		}
	}

	@Override
	public SimpleRole findById(int roleId, int sAccountId) {
		return roleMapperExt.findById(roleId);
	}

	@Override
	public Integer getSystemRoleId(String systemRoleName,
			@CacheKey Integer sAccountId) {
		RoleExample ex = new RoleExample();
		ex.createCriteria().andRolenameEqualTo(systemRoleName)
				.andIssystemroleEqualTo(Boolean.TRUE);
		List<Role> roles = roleMapper.selectByExample(ex);
		if (CollectionUtils.isNotEmpty(roles)) {
			return roles.get(0).getId();
		} else {
			return null;
		}
	}
}

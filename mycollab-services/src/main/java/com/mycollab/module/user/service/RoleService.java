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
package com.mycollab.module.user.service;

import com.mycollab.core.cache.CacheEvict;
import com.mycollab.core.cache.CacheKey;
import com.mycollab.core.cache.Cacheable;
import com.mycollab.db.persistence.service.IDefaultService;
import com.mycollab.module.user.domain.Role;
import com.mycollab.module.user.domain.SimpleRole;
import com.mycollab.module.user.domain.criteria.RoleSearchCriteria;
import com.mycollab.security.PermissionMap;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public interface RoleService extends IDefaultService<Integer, Role, RoleSearchCriteria> {
    @CacheEvict
    void savePermission(Integer roleId, PermissionMap permissionMap, @CacheKey Integer sAccountId);

    @Cacheable
    SimpleRole findById(Integer roleId, @CacheKey Integer sAccountId);

    Integer getDefaultRoleId(Integer sAccountId);
}

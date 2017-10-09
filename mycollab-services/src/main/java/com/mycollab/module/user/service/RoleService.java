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

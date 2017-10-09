package com.mycollab.module.user.dao;

import com.mycollab.db.persistence.ISearchableDAO;
import com.mycollab.module.user.domain.SimpleRole;
import com.mycollab.module.user.domain.criteria.RoleSearchCriteria;

public interface RoleMapperExt extends ISearchableDAO<RoleSearchCriteria> {
    SimpleRole findById(Integer roleId);
}

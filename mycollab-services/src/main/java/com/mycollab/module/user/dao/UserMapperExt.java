package com.mycollab.module.user.dao;

import com.mycollab.db.persistence.ISearchableDAO;
import com.mycollab.module.user.domain.criteria.UserSearchCriteria;

import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public interface UserMapperExt extends ISearchableDAO<UserSearchCriteria> {

    void removeKeysWithSession(List<String> primaryKeys);

}

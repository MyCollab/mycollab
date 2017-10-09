package com.mycollab.module.tracker.service.impl;

import com.mycollab.aspect.ClassInfo;
import com.mycollab.aspect.ClassInfoMap;
import com.mycollab.aspect.Traceable;
import com.mycollab.common.ModuleNameConstants;
import com.mycollab.db.persistence.ICrudGenericDAO;
import com.mycollab.db.persistence.ISearchableDAO;
import com.mycollab.db.persistence.service.DefaultService;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.tracker.dao.VersionMapper;
import com.mycollab.module.tracker.dao.VersionMapperExt;
import com.mycollab.module.tracker.domain.SimpleVersion;
import com.mycollab.module.tracker.domain.Version;
import com.mycollab.module.tracker.domain.criteria.VersionSearchCriteria;
import com.mycollab.module.tracker.service.VersionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author MyCollab Ltd.
 * @since 1.0.0
 */
@Service
@Transactional
@Traceable(nameField = "name", extraFieldName = "projectid")
public class VersionServiceImpl extends DefaultService<Integer, Version, VersionSearchCriteria> implements VersionService {
    static {
        ClassInfoMap.put(VersionServiceImpl.class, new ClassInfo(ModuleNameConstants.PRJ, ProjectTypeConstants.BUG_VERSION));
    }

    @Autowired
    private VersionMapper versionMapper;

    @Autowired
    private VersionMapperExt versionMapperExt;

    @Override
    public ICrudGenericDAO<Integer, Version> getCrudMapper() {
        return versionMapper;
    }

    @Override
    public ISearchableDAO<VersionSearchCriteria> getSearchMapper() {
        return versionMapperExt;
    }

    @Override
    public SimpleVersion findById(Integer versionId, Integer sAccountId) {
        return versionMapperExt.findVersionById(versionId);
    }
}

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
package com.mycollab.module.tracker.service.impl;

import com.mycollab.common.ModuleNameConstants;
import com.mycollab.aspect.ClassInfo;
import com.mycollab.aspect.ClassInfoMap;
import com.mycollab.aspect.Traceable;
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
@Traceable(nameField = "versionname", extraFieldName = "projectid")
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

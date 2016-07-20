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
import com.mycollab.common.interceptor.aspect.ClassInfo;
import com.mycollab.common.interceptor.aspect.ClassInfoMap;
import com.mycollab.common.interceptor.aspect.Traceable;
import com.mycollab.core.MyCollabException;
import com.mycollab.db.persistence.ICrudGenericDAO;
import com.mycollab.db.persistence.ISearchableDAO;
import com.mycollab.db.persistence.service.DefaultService;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.tracker.dao.ComponentMapper;
import com.mycollab.module.tracker.dao.ComponentMapperExt;
import com.mycollab.module.tracker.domain.Component;
import com.mycollab.module.tracker.domain.ComponentExample;
import com.mycollab.module.tracker.domain.SimpleComponent;
import com.mycollab.module.tracker.domain.criteria.ComponentSearchCriteria;
import com.mycollab.module.tracker.service.ComponentService;
import com.google.common.eventbus.AsyncEventBus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author MyCollab Ltd.
 * @since 1.0.0
 */
@Service
@Transactional
@Traceable(nameField = "componentname", extraFieldName = "projectid")
public class ComponentServiceImpl extends DefaultService<Integer, Component, ComponentSearchCriteria> implements ComponentService {
    static {
        ClassInfoMap.put(ComponentServiceImpl.class, new ClassInfo(ModuleNameConstants.PRJ, ProjectTypeConstants.BUG_COMPONENT));
    }

    @Autowired
    private ComponentMapper componentMapper;
    @Autowired
    private ComponentMapperExt componentMapperExt;
    @Autowired
    private AsyncEventBus asyncEventBus;

    @Override
    public ICrudGenericDAO<Integer, Component> getCrudMapper() {
        return componentMapper;
    }

    @Override
    public ISearchableDAO<ComponentSearchCriteria> getSearchMapper() {
        return componentMapperExt;
    }

    @Override
    public SimpleComponent findById(Integer componentId, Integer sAccountId) {
        return componentMapperExt.findComponentById(componentId);
    }

    @Override
    public Integer saveWithSession(Component record, String username) {
        // check whether there is exiting record
        ComponentExample ex = new ComponentExample();
        ex.createCriteria().andComponentnameEqualTo(record.getComponentname()).andProjectidEqualTo(record.getProjectid());

        int count = componentMapper.countByExample(ex);
        if (count > 0) {
            throw new MyCollabException("There is an existing record has name " + record.getComponentname());
        } else {
            return super.saveWithSession(record, username);
        }
    }
}
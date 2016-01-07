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
package com.esofthead.mycollab.module.crm.service.ibatis;

import com.esofthead.mycollab.cache.CleanCacheEvent;
import com.esofthead.mycollab.common.ModuleNameConstants;
import com.esofthead.mycollab.common.interceptor.aspect.ClassInfo;
import com.esofthead.mycollab.common.interceptor.aspect.ClassInfoMap;
import com.esofthead.mycollab.common.interceptor.aspect.Traceable;
import com.esofthead.mycollab.common.interceptor.aspect.Watchable;
import com.esofthead.mycollab.core.persistence.ICrudGenericDAO;
import com.esofthead.mycollab.core.persistence.ISearchableDAO;
import com.esofthead.mycollab.core.persistence.service.DefaultService;
import com.esofthead.mycollab.module.crm.CrmTypeConstants;
import com.esofthead.mycollab.module.crm.dao.MeetingMapper;
import com.esofthead.mycollab.module.crm.dao.MeetingMapperExt;
import com.esofthead.mycollab.module.crm.domain.MeetingWithBLOBs;
import com.esofthead.mycollab.module.crm.domain.SimpleMeeting;
import com.esofthead.mycollab.module.crm.domain.criteria.MeetingSearchCriteria;
import com.esofthead.mycollab.module.crm.service.EventService;
import com.esofthead.mycollab.module.crm.service.MeetingService;
import com.google.common.eventbus.AsyncEventBus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@Service
@Transactional
@Traceable(nameField = "subject")
@Watchable(userFieldName = "createduser")
public class MeetingServiceImpl extends DefaultService<Integer, MeetingWithBLOBs, MeetingSearchCriteria> implements MeetingService {
    static {
        ClassInfoMap.put(MeetingServiceImpl.class, new ClassInfo(ModuleNameConstants.CRM, CrmTypeConstants.MEETING));
    }

    @Autowired
    private MeetingMapper meetingMapper;
    @Autowired
    private MeetingMapperExt meetingMapperExt;

    @Autowired
    private AsyncEventBus asyncEventBus;

    @SuppressWarnings("unchecked")
    @Override
    public ICrudGenericDAO<Integer, MeetingWithBLOBs> getCrudMapper() {
        return meetingMapper;
    }

    @Override
    public SimpleMeeting findById(Integer meetingId, Integer sAccountId) {
        return meetingMapperExt.findById(meetingId);
    }

    @Override
    public ISearchableDAO<MeetingSearchCriteria> getSearchMapper() {
        return meetingMapperExt;
    }

    @Override
    public Integer saveWithSession(MeetingWithBLOBs record, String username) {
        Integer result = super.saveWithSession(record, username);
        asyncEventBus.post(new CleanCacheEvent(record.getSaccountid(), new Class[]{EventService.class}));
        return result;
    }

    @Override
    public Integer updateWithSession(MeetingWithBLOBs record, String username) {
        Integer result = super.updateWithSession(record, username);
        asyncEventBus.post(new CleanCacheEvent(record.getSaccountid(), new Class[]{EventService.class}));
        return result;
    }

    @Override
    public void removeByCriteria(MeetingSearchCriteria criteria, Integer accountId) {
        super.removeByCriteria(criteria, accountId);
        asyncEventBus.post(new CleanCacheEvent(accountId, new Class[]{EventService.class}));
    }

    @Override
    public void massRemoveWithSession(List<MeetingWithBLOBs> items, String username, Integer accountId) {
        super.massRemoveWithSession(items, username, accountId);
        asyncEventBus.post(new CleanCacheEvent(accountId, new Class[]{EventService.class}));
    }

    @Override
    public void massUpdateWithSession(MeetingWithBLOBs record, List<Integer> primaryKeys, Integer accountId) {
        super.massUpdateWithSession(record, primaryKeys, accountId);
        asyncEventBus.post(new CleanCacheEvent(accountId, new Class[]{EventService.class}));
    }

    @Override
    public void updateBySearchCriteria(MeetingWithBLOBs record, MeetingSearchCriteria searchCriteria) {
        super.updateBySearchCriteria(record, searchCriteria);
        asyncEventBus.post(new CleanCacheEvent((Integer) searchCriteria.getSaccountid().getValue(),
                new Class[]{EventService.class}));
    }
}

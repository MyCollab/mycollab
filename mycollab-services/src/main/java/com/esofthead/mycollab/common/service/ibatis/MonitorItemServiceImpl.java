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
package com.esofthead.mycollab.common.service.ibatis;

import com.esofthead.mycollab.common.dao.MonitorItemMapper;
import com.esofthead.mycollab.common.dao.MonitorItemMapperExt;
import com.esofthead.mycollab.common.domain.MonitorItem;
import com.esofthead.mycollab.common.domain.MonitorItemExample;
import com.esofthead.mycollab.common.domain.criteria.MonitorSearchCriteria;
import com.esofthead.mycollab.common.service.MonitorItemService;
import com.esofthead.mycollab.core.persistence.ICrudGenericDAO;
import com.esofthead.mycollab.core.persistence.ISearchableDAO;
import com.esofthead.mycollab.core.persistence.service.DefaultService;
import com.esofthead.mycollab.module.user.domain.SimpleUser;
import com.google.common.eventbus.AsyncEventBus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@Service
public class MonitorItemServiceImpl extends DefaultService<Integer, MonitorItem, MonitorSearchCriteria> implements MonitorItemService {

    @Autowired
    private MonitorItemMapper monitorItemMapper;

    @Autowired
    private MonitorItemMapperExt monitorItemMapperExt;

    @Autowired
    private AsyncEventBus asyncEventBus;

    @Override
    public ICrudGenericDAO<Integer, MonitorItem> getCrudMapper() {
        return monitorItemMapper;
    }

    @Override
    public ISearchableDAO<MonitorSearchCriteria> getSearchMapper() {
        return monitorItemMapperExt;
    }

    @Override
    public boolean isUserWatchingItem(String username, String type, Integer typeId) {
        MonitorItemExample ex = new MonitorItemExample();
        ex.createCriteria().andUserEqualTo(username).andTypeEqualTo(type).andTypeidEqualTo(typeId);
        return monitorItemMapper.countByExample(ex) > 0;
    }

    @Override
    public Integer saveWithSession(MonitorItem record, String username) {
        MonitorItemExample ex = new MonitorItemExample();
        ex.createCriteria().andTypeEqualTo(record.getType()).andTypeidEqualTo(record.getTypeid()).andUserEqualTo(record.getUser());
        int count = monitorItemMapper.countByExample(ex);
        if (count > 0) {
            return 1;
        } else {
            return super.saveWithSession(record, username);
        }
    }

    @Override
    public void saveMonitorItems(Collection<MonitorItem> monitorItems) {
        if (monitorItems.size() > 0) {
            monitorItemMapperExt.saveMonitorItems(monitorItems);
        }
    }

    @Override
    public List<SimpleUser> getWatchers(String type, Integer typeId) {
        return monitorItemMapperExt.getWatchers(type, typeId);
    }

    @Override
    public Integer getNextItemKey(MonitorSearchCriteria arg0) {
        return null;
    }

    @Override
    public Integer getPreviousItemKey(MonitorSearchCriteria arg0) {
        return null;
    }
}

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
package com.esofthead.mycollab.module.project.service.ibatis;

import com.esofthead.mycollab.cache.CleanCacheEvent;
import com.esofthead.mycollab.common.ModuleNameConstants;
import com.esofthead.mycollab.common.interceptor.aspect.ClassInfo;
import com.esofthead.mycollab.common.interceptor.aspect.ClassInfoMap;
import com.esofthead.mycollab.common.interceptor.aspect.Traceable;
import com.esofthead.mycollab.core.persistence.ICrudGenericDAO;
import com.esofthead.mycollab.core.persistence.ISearchableDAO;
import com.esofthead.mycollab.core.persistence.service.DefaultService;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.dao.MessageMapper;
import com.esofthead.mycollab.module.project.dao.MessageMapperExt;
import com.esofthead.mycollab.module.project.domain.Message;
import com.esofthead.mycollab.module.project.domain.SimpleMessage;
import com.esofthead.mycollab.module.project.domain.criteria.MessageSearchCriteria;
import com.esofthead.mycollab.module.project.esb.DeleteProjectMessageEvent;
import com.esofthead.mycollab.module.project.service.MessageService;
import com.esofthead.mycollab.module.project.service.ProjectActivityStreamService;
import com.esofthead.mycollab.schedule.email.project.MessageRelayEmailNotificationAction;
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
@Traceable(nameField = "title", extraFieldName = "projectid", notifyAgent = MessageRelayEmailNotificationAction.class)
public class MessageServiceImpl extends DefaultService<Integer, Message, MessageSearchCriteria> implements MessageService {

    static {
        ClassInfoMap.put(MessageServiceImpl.class, new ClassInfo(ModuleNameConstants.PRJ, ProjectTypeConstants.MESSAGE));
    }

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private MessageMapperExt messageMapperExt;

    @Autowired
    private AsyncEventBus asyncEventBus;

    @Override
    public ICrudGenericDAO<Integer, Message> getCrudMapper() {
        return messageMapper;
    }

    @Override
    public Integer saveWithSession(Message record, String username) {
        Integer recordId = super.saveWithSession(record, username);
        asyncEventBus.post(new CleanCacheEvent(record.getSaccountid(), new Class[]{ProjectActivityStreamService.class}));
        return recordId;
    }

    @Override
    public Integer updateWithSession(Message record, String username) {
        int result = super.updateWithSession(record, username);
        asyncEventBus.post(new CleanCacheEvent(record.getSaccountid(), new Class[]{ProjectActivityStreamService.class}));
        return result;
    }

    @Override
    public void massRemoveWithSession(List<Message> items, String username, Integer accountId) {
        super.massRemoveWithSession(items, username, accountId);
        asyncEventBus.post(new CleanCacheEvent(accountId, new Class[]{ProjectActivityStreamService.class}));
        DeleteProjectMessageEvent event = new DeleteProjectMessageEvent(items.toArray(new Message[items.size()]),
                username, accountId);
        asyncEventBus.post(event);
    }

    @Override
    public ISearchableDAO<MessageSearchCriteria> getSearchMapper() {
        return messageMapperExt;
    }

    @Override
    public SimpleMessage findById(Integer messageId, Integer sAccountId) {
        return messageMapperExt.findMessageById(messageId);
    }
}

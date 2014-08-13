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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.esofthead.mycollab.cache.CacheUtils;
import com.esofthead.mycollab.common.ModuleNameConstants;
import com.esofthead.mycollab.common.MonitorTypeConstants;
import com.esofthead.mycollab.common.domain.RelayEmailNotification;
import com.esofthead.mycollab.common.interceptor.aspect.Traceable;
import com.esofthead.mycollab.common.service.RelayEmailNotificationService;
import com.esofthead.mycollab.core.persistence.ICrudGenericDAO;
import com.esofthead.mycollab.core.persistence.ISearchableDAO;
import com.esofthead.mycollab.core.persistence.service.DefaultService;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.dao.MessageMapper;
import com.esofthead.mycollab.module.project.dao.MessageMapperExt;
import com.esofthead.mycollab.module.project.domain.Message;
import com.esofthead.mycollab.module.project.domain.SimpleMessage;
import com.esofthead.mycollab.module.project.domain.criteria.MessageSearchCriteria;
import com.esofthead.mycollab.module.project.service.MessageService;
import com.esofthead.mycollab.module.project.service.ProjectActivityStreamService;
import com.esofthead.mycollab.schedule.email.project.MessageRelayEmailNotificationAction;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
@Service
@Transactional
@Traceable(module = ModuleNameConstants.PRJ, nameField = "title", type = ProjectTypeConstants.MESSAGE, extraFieldName = "projectid")
public class MessageServiceImpl extends
		DefaultService<Integer, Message, MessageSearchCriteria> implements
		MessageService {

	@Autowired
	private MessageMapper messageMapper;

	@Autowired
	private MessageMapperExt messageMapperExt;

	@Autowired
	private RelayEmailNotificationService relayEmailNotificationService;

	@Override
	public ICrudGenericDAO<Integer, Message> getCrudMapper() {
		return messageMapper;
	}

	@Override
	public int saveWithSession(Message record, String username) {
		int recordId = super.saveWithSession(record, username);
		relayEmailNotificationService.saveWithSession(
				createNotification(record, username, recordId), username);
		CacheUtils.cleanCaches(record.getSaccountid(),
				ProjectActivityStreamService.class);
		return recordId;
	}

	@Override
	public int updateWithSession(Message record, String username) {
		relayEmailNotificationService.saveWithSession(
				createNotification(record, username, record.getId()), username);
		CacheUtils.cleanCaches(record.getSaccountid(),
				ProjectActivityStreamService.class);
		return super.updateWithSession(record, username);
	}

	@Override
	public int removeWithSession(Integer primaryKey, String username,
			int accountId) {
		CacheUtils.cleanCaches(accountId, ProjectActivityStreamService.class);
		return super.removeWithSession(primaryKey, username, accountId);
	}

	private RelayEmailNotification createNotification(Message record,
			String username, int recordId) {
		RelayEmailNotification relayNotification = new RelayEmailNotification();
		relayNotification.setChangeby(username);
		relayNotification.setChangecomment("");
		if (record.getSaccountid() != null) {
			int sAccountId = record.getSaccountid();
			relayNotification.setSaccountid(sAccountId);
		}
		relayNotification.setType(ProjectTypeConstants.MESSAGE);
		relayNotification.setAction(MonitorTypeConstants.CREATE_ACTION);
		relayNotification
				.setEmailhandlerbean(MessageRelayEmailNotificationAction.class
						.getName());
		relayNotification.setTypeid("" + recordId);
		relayNotification.setExtratypeid(record.getProjectid());
		return relayNotification;
	}

	@Override
	public ISearchableDAO<MessageSearchCriteria> getSearchMapper() {
		return messageMapperExt;
	}

	@Override
	public SimpleMessage findMessageById(int messageId, Integer sAccountId) {
		return messageMapperExt.findMessageById(messageId);
	}
}

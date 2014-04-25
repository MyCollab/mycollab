/**
 * This file is part of mycollab-scheduler.
 *
 * mycollab-scheduler is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-scheduler is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-scheduler.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.schedule.email.crm.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.esofthead.mycollab.common.MonitorTypeConstants;
import com.esofthead.mycollab.common.domain.MailRecipientField;
import com.esofthead.mycollab.common.domain.SimpleRelayEmailNotification;
import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchRequest;
import com.esofthead.mycollab.core.arguments.StringSearchField;
import com.esofthead.mycollab.core.arguments.ValuedBean;
import com.esofthead.mycollab.core.utils.BeanUtility;
import com.esofthead.mycollab.module.crm.domain.CrmNotificationSetting;
import com.esofthead.mycollab.module.crm.domain.SimpleNote;
import com.esofthead.mycollab.module.crm.domain.criteria.NoteSearchCriteria;
import com.esofthead.mycollab.module.crm.service.CrmNotificationSettingService;
import com.esofthead.mycollab.module.crm.service.NoteService;
import com.esofthead.mycollab.module.mail.TemplateGenerator;
import com.esofthead.mycollab.module.mail.service.ExtMailService;
import com.esofthead.mycollab.module.user.domain.SimpleUser;
import com.esofthead.mycollab.module.user.service.UserService;
import com.esofthead.mycollab.schedule.email.LinkUtils;
import com.esofthead.mycollab.schedule.email.SendingRelayEmailNotificationAction;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public abstract class CrmDefaultSendingRelayEmailAction<B extends ValuedBean>
		implements SendingRelayEmailNotificationAction {

	private static Logger log = LoggerFactory
			.getLogger(CrmDefaultSendingRelayEmailAction.class);

	@Autowired
	protected ExtMailService extMailService;

	@Autowired
	protected NoteService noteService;

	@Autowired
	protected UserService userService;

	@Autowired
	protected CrmNotificationSettingService notificationService;

	protected String crmType;

	protected String currentUserTimezone;

	protected String siteUrl;

	public CrmDefaultSendingRelayEmailAction(String crmType) {
		this.crmType = crmType;
	}

	@Override
	public void sendNotificationForCreateAction(
			SimpleRelayEmailNotification notification) {
		List<SimpleUser> notifiers = getListNotififyUserWithFilter(
				notification, MonitorTypeConstants.CREATE_ACTION);
		if ((notifiers != null) && !notifiers.isEmpty()) {
			onInitAction(notification);
			for (SimpleUser user : notifiers) {
				currentUserTimezone = user.getTimezone();
				TemplateGenerator templateGenerator = templateGeneratorForCreateAction(
						notification, user);
				if (templateGenerator != null) {
					String notifierFullName = user.getDisplayName();
					if (notifierFullName == null
							|| notifierFullName.trim().length() == 0) {
						log.error(
								"Can not find user {} of notification {}",
								new String[] { BeanUtility.printBeanObj(user),
										BeanUtility.printBeanObj(notification) });
						return;
					}
					templateGenerator.putVariable("userName", notifierFullName);

					MailRecipientField userMail = new MailRecipientField(
							user.getEmail(), user.getUsername());
					List<MailRecipientField> lst = new ArrayList<MailRecipientField>();
					lst.add(userMail);

					extMailService.sendHTMLMail("noreply@mycollab.com",
							"MyCollab", lst, null, null,
							templateGenerator.generateSubjectContent(),
							templateGenerator.generateBodyContent(), null);
				}
			}
		}

	}

	@Override
	public void sendNotificationForUpdateAction(
			SimpleRelayEmailNotification notification) {
		List<SimpleUser> notifiers = getListNotififyUserWithFilter(
				notification, MonitorTypeConstants.UPDATE_ACTION);
		if ((notifiers != null) && !notifiers.isEmpty()) {
			onInitAction(notification);
			for (SimpleUser user : notifiers) {
				currentUserTimezone = user.getTimezone();
				TemplateGenerator templateGenerator = templateGeneratorForUpdateAction(
						notification, user);
				if (templateGenerator != null) {
					String notifierFullName = user.getDisplayName();
					if (notifierFullName == null) {
						log.error(
								"Can not find user {} of notification {}",
								new String[] { BeanUtility.printBeanObj(user),
										BeanUtility.printBeanObj(notification) });
						return;
					}
					templateGenerator.putVariable("userName", notifierFullName);

					MailRecipientField userMail = new MailRecipientField(
							user.getEmail(), user.getUsername());
					List<MailRecipientField> lst = new ArrayList<MailRecipientField>();
					lst.add(userMail);

					extMailService.sendHTMLMail("noreply@mycollab.com",
							"MyCollab", lst, null, null,
							templateGenerator.generateSubjectContent(),
							templateGenerator.generateBodyContent(), null);
				}
			}
		}
	}

	@Override
	public void sendNotificationForCommentAction(
			SimpleRelayEmailNotification notification) {
		List<SimpleUser> notifiers = getListNotififyUserWithFilter(
				notification, MonitorTypeConstants.ADD_COMMENT_ACTION);
		if ((notifiers != null) && !notifiers.isEmpty()) {
			onInitAction(notification);
			for (SimpleUser user : notifiers) {
				currentUserTimezone = user.getTimezone();
				TemplateGenerator templateGenerator = templateGeneratorForCommentAction(
						notification, user);
				if (templateGenerator != null) {
					String notifierFullName = user.getDisplayName();
					if (notifierFullName == null) {
						log.error(
								"Can not find user {} of notification {}",
								new String[] { BeanUtility.printBeanObj(user),
										BeanUtility.printBeanObj(notification) });
						return;
					}
					templateGenerator.putVariable("userName", notifierFullName);

					MailRecipientField userMail = new MailRecipientField(
							user.getEmail(), user.getUsername());
					List<MailRecipientField> lst = new ArrayList<MailRecipientField>();
					lst.add(userMail);

					extMailService.sendHTMLMail("noreply@mycollab.com",
							"MyCollab", lst, null, null,
							templateGenerator.generateSubjectContent(),
							templateGenerator.generateBodyContent(), null);
				}
			}
		}
	}

	protected List<SimpleUser> getListNotififyUserWithFilter(
			SimpleRelayEmailNotification notification, String type) {
		List<CrmNotificationSetting> notificationSettings = notificationService
				.findNotifications(notification.getSaccountid());
		List<SimpleUser> inListUsers = notification.getNotifyUsers();

		// If an user has add notes to Item , So we must include him to list
		// "need sending notifications"
		// ---------------------------------------------------------------
		NoteSearchCriteria noteSearchCriteria = new NoteSearchCriteria();
		noteSearchCriteria.setType(new StringSearchField(crmType));
		noteSearchCriteria.setTypeid(new NumberSearchField(notification
				.getTypeid()));
		noteSearchCriteria.setSaccountid(new NumberSearchField(notification
				.getSaccountid()));
		List<SimpleNote> lstNote = noteService
				.findPagableListByCriteria(new SearchRequest<NoteSearchCriteria>(
						noteSearchCriteria, 0, Integer.MAX_VALUE));

		if (lstNote != null && lstNote.size() > 0) {
			for (SimpleNote note : lstNote) {
				if (note.getCreateduser() != null) {
					SimpleUser user = userService.findUserByUserNameInAccount(
							note.getCreateduser(), note.getSaccountid());
					if (user != null
							&& checkExistInList(inListUsers, user) == false) {
						inListUsers.add(user);
					}
				}
			}
		}

		// Now filter them with notification setting.
		for (int i = 0; i < inListUsers.size(); i++) {
			SimpleUser user = inListUsers.get(i);
			for (CrmNotificationSetting notificationSetting : notificationSettings) {
				if (user.getUsername() != null
						&& user.getUsername().equals(
								notificationSetting.getUsername())) {
					if (notificationSetting.getLevel().equals("None")) {
						inListUsers.remove(user);
						i--;
					} else if (notificationSetting.getLevel().equals("Minimal")
							&& type.equals(MonitorTypeConstants.ADD_COMMENT_ACTION)) {
						inListUsers.remove(user);
						i--;
					}
				}
				// else is Default sending ...
			}
		}
		return inListUsers;

	}
	
	private void onInitAction(SimpleRelayEmailNotification notification) {
		siteUrl = LinkUtils.getSiteUrl(notification.getSaccountid());
	}

	private boolean checkExistInList(List<SimpleUser> lst, SimpleUser user) {
		for (SimpleUser simpleUser : lst) {
			if (simpleUser.getUsername() != null
					&& simpleUser.getUsername().equals(user.getUsername())
					|| simpleUser.getEmail().equals(user.getUsername())) {
				return true;
			}
		}
		return false;
	}

	protected abstract TemplateGenerator templateGeneratorForCreateAction(
			SimpleRelayEmailNotification emailNotification, SimpleUser user);

	protected abstract TemplateGenerator templateGeneratorForUpdateAction(
			SimpleRelayEmailNotification emailNotification, SimpleUser user);

	protected abstract TemplateGenerator templateGeneratorForCommentAction(
			SimpleRelayEmailNotification emailNotification, SimpleUser user);

}

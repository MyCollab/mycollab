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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.esofthead.mycollab.common.MonitorTypeConstants;
import com.esofthead.mycollab.common.domain.MailRecipientField;
import com.esofthead.mycollab.common.domain.SimpleRelayEmailNotification;
import com.esofthead.mycollab.configuration.SiteConfiguration;
import com.esofthead.mycollab.core.DeploymentMode;
import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchRequest;
import com.esofthead.mycollab.core.arguments.StringSearchField;
import com.esofthead.mycollab.core.arguments.ValuedBean;
import com.esofthead.mycollab.core.utils.BeanUtility;
import com.esofthead.mycollab.module.crm.domain.CrmNotificationSetting;
import com.esofthead.mycollab.module.crm.domain.SimpleAccount;
import com.esofthead.mycollab.module.crm.domain.SimpleCampaign;
import com.esofthead.mycollab.module.crm.domain.SimpleCase;
import com.esofthead.mycollab.module.crm.domain.SimpleContact;
import com.esofthead.mycollab.module.crm.domain.SimpleLead;
import com.esofthead.mycollab.module.crm.domain.SimpleNote;
import com.esofthead.mycollab.module.crm.domain.SimpleOpportunity;
import com.esofthead.mycollab.module.crm.domain.criteria.NoteSearchCriteria;
import com.esofthead.mycollab.module.crm.service.AccountService;
import com.esofthead.mycollab.module.crm.service.CampaignService;
import com.esofthead.mycollab.module.crm.service.CaseService;
import com.esofthead.mycollab.module.crm.service.ContactService;
import com.esofthead.mycollab.module.crm.service.CrmNotificationSettingService;
import com.esofthead.mycollab.module.crm.service.LeadService;
import com.esofthead.mycollab.module.crm.service.NoteService;
import com.esofthead.mycollab.module.crm.service.OpportunityService;
import com.esofthead.mycollab.module.mail.TemplateGenerator;
import com.esofthead.mycollab.module.mail.service.ExtMailService;
import com.esofthead.mycollab.module.user.domain.BillingAccount;
import com.esofthead.mycollab.module.user.domain.SimpleUser;
import com.esofthead.mycollab.module.user.service.BillingAccountService;
import com.esofthead.mycollab.module.user.service.UserService;
import com.esofthead.mycollab.schedule.email.LinkUtils;
import com.esofthead.mycollab.schedule.email.MailItemLink;
import com.esofthead.mycollab.schedule.email.SendingRelayEmailNotificationAction;
import com.esofthead.mycollab.schedule.email.crm.CrmMailLinkGenerator;
import com.esofthead.mycollab.schedule.email.format.DefaultFieldFormat;
import com.esofthead.mycollab.schedule.email.format.FieldFormat;
import com.esofthead.mycollab.schedule.email.format.FieldFormat.Type;
import com.esofthead.mycollab.spring.ApplicationContextUtil;

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

	public static final String DEFAULT_FIELD = "default";
	public static final String DATE_FIELD = "date";
	public static final String DATETIME_FIELD = "datetime";
	public static final String CURRENCY_FIELD = "currency";

	protected String currentUserTimezone;

	protected Map<String, FieldFormat> fieldsFormat = new HashMap<String, FieldFormat>();

	public CrmDefaultSendingRelayEmailAction(String crmType) {
		this.crmType = crmType;
	}

	public void generateFieldDisplayHandler(String fieldName, String displayName) {
		fieldsFormat.put(fieldName, new DefaultFieldFormat(fieldName,
				displayName));
	}

	public void generateFieldDisplayHandler(String fieldname, FieldFormat format) {
		fieldsFormat.put(fieldname, format);
	}

	public void generateFieldDisplayHandler(String fieldName,
			String displayName, Type formatType) {
		fieldsFormat.put(fieldName, FieldFormat.createFieldFormat(formatType,
				fieldName, displayName));
	}

	@Override
	public void sendNotificationForCreateAction(
			SimpleRelayEmailNotification notification) {
		List<SimpleUser> notifiers = getListNotififyUserWithFilter(
				notification, MonitorTypeConstants.CREATE_ACTION);
		if ((notifiers != null) && !notifiers.isEmpty()) {
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

	@Deprecated
	protected String getSiteUrl(Integer sAccountId) {
		return LinkUtils.getSiteUrl(sAccountId);
	}

	protected MailItemLink generateRelatedItem(String type, Integer typeId,
			int sAccountId, CrmMailLinkGenerator crmLinkGenerator) {
		MailItemLink relatedItem = new MailItemLink(null, "");

		if ("Account".equals(type)) {
			AccountService accountService = ApplicationContextUtil
					.getSpringBean(AccountService.class);
			final SimpleAccount account = accountService.findById(typeId,
					sAccountId);
			if (account != null) {
				relatedItem.setDisplayName(account.getAccountname());
				relatedItem.setWebLink(crmLinkGenerator
						.generateAccountPreviewFullLink(typeId));
			}
		} else if ("Contact".equals(type)) {
			ContactService contactService = ApplicationContextUtil
					.getSpringBean(ContactService.class);
			final SimpleContact contact = contactService.findById(typeId,
					sAccountId);
			if (contact != null) {
				relatedItem.setDisplayName(contact.getContactName());
				relatedItem.setWebLink(crmLinkGenerator
						.generateContactPreviewFullLink(typeId));
			}
		} else if ("Case".equals(type)) {
			CaseService caseService = ApplicationContextUtil
					.getSpringBean(CaseService.class);
			final SimpleCase simpleCase = caseService.findById(typeId,
					sAccountId);
			if (simpleCase != null) {
				relatedItem.setDisplayName(simpleCase.getSubject());
				relatedItem.setWebLink(crmLinkGenerator
						.generateCasePreviewFullLink(typeId));
			}
		} else if ("Campaign".equals(type)) {
			CampaignService campaignService = ApplicationContextUtil
					.getSpringBean(CampaignService.class);
			final SimpleCampaign campaign = campaignService.findById(typeId,
					sAccountId);
			if (campaign != null) {
				relatedItem.setDisplayName(campaign.getCampaignname());
				relatedItem.setWebLink(crmLinkGenerator
						.generateCampainPreviewFullLilnk(typeId));
			}
		} else if ("Lead".equals(type)) {
			LeadService leadService = ApplicationContextUtil
					.getSpringBean(LeadService.class);
			final SimpleLead lead = leadService.findById(typeId, sAccountId);
			if (lead != null) {
				relatedItem.setDisplayName(lead.getLeadName());
				relatedItem.setWebLink(crmLinkGenerator
						.generateLeadPreviewFullLink(typeId));
			}
		} else if ("Opportunity".equals(type)) {
			OpportunityService opportunityService = ApplicationContextUtil
					.getSpringBean(OpportunityService.class);
			final SimpleOpportunity opportunity = opportunityService.findById(
					typeId, sAccountId);
			if (opportunity != null) {
				relatedItem.setDisplayName(opportunity.getOpportunityname());
				relatedItem.setWebLink(crmLinkGenerator
						.generateOpportunityPreviewFullLink(typeId));
			}
		}

		return relatedItem;
	}

	protected abstract TemplateGenerator templateGeneratorForCreateAction(
			SimpleRelayEmailNotification emailNotification, SimpleUser user);

	protected abstract TemplateGenerator templateGeneratorForUpdateAction(
			SimpleRelayEmailNotification emailNotification, SimpleUser user);

	protected abstract TemplateGenerator templateGeneratorForCommentAction(
			SimpleRelayEmailNotification emailNotification, SimpleUser user);

}

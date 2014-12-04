/**
 * This file is part of mycollab-web.
 *
 * mycollab-web is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-web is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-web.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.module.crm.ui.components;

import org.apache.commons.beanutils.PropertyUtils;

import com.esofthead.mycollab.module.crm.data.CrmLinkBuilder;
import com.esofthead.mycollab.module.crm.domain.SimpleAccount;
import com.esofthead.mycollab.module.crm.domain.SimpleCampaign;
import com.esofthead.mycollab.module.crm.domain.SimpleCase;
import com.esofthead.mycollab.module.crm.domain.SimpleContact;
import com.esofthead.mycollab.module.crm.domain.SimpleLead;
import com.esofthead.mycollab.module.crm.domain.SimpleOpportunity;
import com.esofthead.mycollab.module.crm.service.AccountService;
import com.esofthead.mycollab.module.crm.service.CampaignService;
import com.esofthead.mycollab.module.crm.service.CaseService;
import com.esofthead.mycollab.module.crm.service.ContactService;
import com.esofthead.mycollab.module.crm.service.LeadService;
import com.esofthead.mycollab.module.crm.service.OpportunityService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.LabelLink;
import com.esofthead.mycollab.vaadin.ui.MyCollabResource;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.Label;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public class RelatedReadItemField extends CustomField {
	private static final long serialVersionUID = 1L;

	private Object bean;

	public RelatedReadItemField(Object bean) {
		this.bean = bean;
	}

	@Override
	protected Component initContent() {
		try {
			final Integer typeid = (Integer) PropertyUtils.getProperty(
					RelatedReadItemField.this.bean, "typeid");
			if (typeid == null) {
				return new Label("");
			}

			final String subject = (String) PropertyUtils.getProperty(
					RelatedReadItemField.this.bean, "subject");

			final String type = (String) PropertyUtils
					.getProperty(bean, "type");
			if (type == null || type.equals("")) {
				return new Label("");
			}

			String relatedLink = null;
			String relateItemName = null;

			if ("Account".equals(type)) {
				AccountService accountService = ApplicationContextUtil
						.getSpringBean(AccountService.class);
				final SimpleAccount account = accountService.findById(typeid,
						AppContext.getAccountId());
				if (account != null) {
					relateItemName = account.getAccountname();
					relatedLink = MyCollabResource
							.newResourceLink("icons/16/crm/account.png");
				}
			} else if ("Campaign".equals(type)) {
				CampaignService campaignService = ApplicationContextUtil
						.getSpringBean(CampaignService.class);
				final SimpleCampaign campaign = campaignService.findById(
						typeid, AppContext.getAccountId());
				if (campaign != null) {
					relateItemName = campaign.getCampaignname();
					relatedLink = MyCollabResource
							.newResourceLink("icons/16/crm/campaign.png");

				}
			} else if ("Contact".equals(type)) {
				ContactService contactService = ApplicationContextUtil
						.getSpringBean(ContactService.class);
				final SimpleContact contact = contactService.findById(typeid,
						AppContext.getAccountId());
				if (contact != null) {
					relateItemName = contact.getContactName();
					relatedLink = MyCollabResource
							.newResourceLink("icons/16/crm/contact.png");

				}
			} else if ("Lead".equals(type)) {
				LeadService leadService = ApplicationContextUtil
						.getSpringBean(LeadService.class);
				final SimpleLead lead = leadService.findById(typeid,
						AppContext.getAccountId());
				if (lead != null) {
					relateItemName = lead.getLeadName();
					relatedLink = MyCollabResource
							.newResourceLink("icons/16/crm/lead.png");

				}
			} else if ("Opportunity".equals(type)) {
				OpportunityService opportunityService = ApplicationContextUtil
						.getSpringBean(OpportunityService.class);
				final SimpleOpportunity opportunity = opportunityService
						.findById(typeid, AppContext.getAccountId());
				if (opportunity != null) {
					relateItemName = opportunity.getOpportunityname();
					relatedLink = MyCollabResource
							.newResourceLink("icons/16/crm/opportunity.png");

				}
			} else if ("Case".equals(type)) {
				CaseService caseService = ApplicationContextUtil
						.getSpringBean(CaseService.class);
				final SimpleCase cases = caseService.findById(typeid,
						AppContext.getAccountId());
				if (cases != null) {
					relateItemName = cases.getSubject();
					relatedLink = MyCollabResource
							.newResourceLink("icons/16/crm/case.png");

				}
			}

			LabelLink related = new LabelLink(relateItemName,
					CrmLinkBuilder
							.generateActivityPreviewLinkFull(type, typeid));
			if (relatedLink != null)
				related.setIconLink(relatedLink);

			if (relatedLink != null) {
				return related;
			} else {
				return new Label("");
			}

		} catch (Exception e) {
			return new Label("");
		}
	}

	@Override
	public Class<?> getType() {
		return Object.class;
	}

}

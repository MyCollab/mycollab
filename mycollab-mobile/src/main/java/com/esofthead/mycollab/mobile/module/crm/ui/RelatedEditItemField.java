/**
 * This file is part of mycollab-mobile.
 *
 * mycollab-mobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-mobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-mobile.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.mobile.module.crm.ui;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.esofthead.mycollab.eventmanager.EventBus;
import com.esofthead.mycollab.mobile.module.crm.events.CrmEvent;
import com.esofthead.mycollab.mobile.module.crm.view.account.AccountSelectionView;
import com.esofthead.mycollab.mobile.module.crm.view.campaign.CampaignSelectionView;
import com.esofthead.mycollab.mobile.module.crm.view.cases.CaseSelectionView;
import com.esofthead.mycollab.mobile.module.crm.view.contact.ContactSelectionView;
import com.esofthead.mycollab.mobile.module.crm.view.lead.LeadSelectionView;
import com.esofthead.mycollab.mobile.module.crm.view.opportunity.OpportunitySelectionView;
import com.esofthead.mycollab.mobile.ui.MobileNavigationButton;
import com.esofthead.mycollab.mobile.ui.ValueComboBox;
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
import com.esofthead.mycollab.vaadin.ui.FieldSelection;
import com.vaadin.addon.touchkit.ui.NavigationButton;
import com.vaadin.data.Property;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.1
 * 
 */
@SuppressWarnings("rawtypes")
public class RelatedEditItemField extends CustomField<String> implements
		FieldSelection {

	private static final long serialVersionUID = 1L;
	private static Logger log = LoggerFactory
			.getLogger(RelatedEditItemField.class);

	private RelatedItemComboBox relatedItemComboBox;
	private Object bean;

	private MobileNavigationButton itemField;

	public RelatedEditItemField(String[] types, Object bean) {
		this.bean = bean;

		relatedItemComboBox = new RelatedItemComboBox(types);
		relatedItemComboBox.setWidth("100%");
		itemField = new MobileNavigationButton();
		itemField.setWidth("100%");
		itemField.setStyleName("combo-box");

		itemField
				.addClickListener(new NavigationButton.NavigationButtonClickListener() {
					private static final long serialVersionUID = 1L;

					@SuppressWarnings("unchecked")
					@Override
					public void buttonClick(
							NavigationButton.NavigationButtonClickEvent event) {
						String type = (String) relatedItemComboBox.getValue();
						if ("Account".equals(type)) {
//							AccountSelectionView accountView = new AccountSelectionView(
//									RelatedEditItemField.this);
//							EventBus.getInstance().fireEvent(
//									new CrmEvent.PushView(
//											RelatedEditItemField.this,
//											accountView));
						} else if ("Campaign".equals(type)) {
							CampaignSelectionView campaignView = new CampaignSelectionView(
									RelatedEditItemField.this);
							EventBus.getInstance().fireEvent(
									new CrmEvent.PushView(
											RelatedEditItemField.this,
											campaignView));
						} else if ("Contact".equals(type)) {
							ContactSelectionView contactView = new ContactSelectionView(
									RelatedEditItemField.this);
							EventBus.getInstance().fireEvent(
									new CrmEvent.PushView(
											RelatedEditItemField.this,
											contactView));
						} else if ("Lead".equals(type)) {
							LeadSelectionView leadView = new LeadSelectionView(
									RelatedEditItemField.this);
							EventBus.getInstance()
									.fireEvent(
											new CrmEvent.PushView(
													RelatedEditItemField.this,
													leadView));
						} else if ("Opportunity".equals(type)) {
							OpportunitySelectionView opportunityView = new OpportunitySelectionView(
									RelatedEditItemField.this);
							EventBus.getInstance().fireEvent(
									new CrmEvent.PushView(
											RelatedEditItemField.this,
											opportunityView));
						} else if ("Case".equals(type)) {
							CaseSelectionView caseView = new CaseSelectionView(
									RelatedEditItemField.this);
							EventBus.getInstance()
									.fireEvent(
											new CrmEvent.PushView(
													RelatedEditItemField.this,
													caseView));
						} else {
							relatedItemComboBox.focus();
						}
					}
				});
	}

	@Override
	protected Component initContent() {
		VerticalLayout layout = new VerticalLayout();
		layout.setSpacing(true);

		layout.addComponent(relatedItemComboBox);

		layout.addComponent(itemField);

		return layout;
	}

	@Override
	public Class<String> getType() {
		return String.class;
	}

	@Override
	public void setPropertyDataSource(Property newDataSource) {
		Object value = newDataSource.getValue();
		if (value instanceof String) {
			setType((String) value);
			super.setPropertyDataSource(newDataSource);
		} else {
			super.setPropertyDataSource(newDataSource);
		}
	}

	@Override
	public void commit() throws SourceException, InvalidValueException {
		String value = (String) relatedItemComboBox.getValue();
		this.setInternalValue(value);
		super.commit();
	}

	public void setType(String type) {
		log.debug("Set type: " + type);
		relatedItemComboBox.select(type);
		try {
			Integer typeid = (Integer) PropertyUtils
					.getProperty(bean, "typeid");
			if (typeid != null) {
				if ("Account".equals(type)) {
					AccountService accountService = ApplicationContextUtil
							.getSpringBean(AccountService.class);
					SimpleAccount account = accountService.findById(typeid,
							AppContext.getAccountId());
					if (account != null) {
						itemField.setCaption(account.getAccountname());
					}
				} else if ("Campaign".equals(type)) {
					CampaignService campaignService = ApplicationContextUtil
							.getSpringBean(CampaignService.class);
					SimpleCampaign campaign = campaignService.findById(typeid,
							AppContext.getAccountId());
					if (campaign != null) {
						itemField.setCaption(campaign.getCampaignname());
					}
				} else if ("Contact".equals(type)) {
					ContactService contactService = ApplicationContextUtil
							.getSpringBean(ContactService.class);
					SimpleContact contact = contactService.findById(typeid,
							AppContext.getAccountId());
					if (contact != null) {
						itemField.setCaption(contact.getContactName());
					}
				} else if ("Lead".equals(type)) {
					LeadService leadService = ApplicationContextUtil
							.getSpringBean(LeadService.class);
					SimpleLead lead = leadService.findById(typeid,
							AppContext.getAccountId());
					if (lead != null) {
						itemField.setCaption(lead.getLeadName());
					}
				} else if ("Opportunity".equals(type)) {
					OpportunityService opportunityService = ApplicationContextUtil
							.getSpringBean(OpportunityService.class);
					SimpleOpportunity opportunity = opportunityService
							.findById(typeid, AppContext.getAccountId());
					if (opportunity != null) {
						itemField.setCaption(opportunity.getOpportunityname());
					}
				} else if ("Case".equals(type)) {
					CaseService caseService = ApplicationContextUtil
							.getSpringBean(CaseService.class);
					SimpleCase cases = caseService.findById(typeid,
							AppContext.getAccountId());
					if (cases != null) {
						itemField.setCaption(cases.getSubject());
					}
				}
			}

		} catch (Exception e) {
			log.error("Error when set type", e);
		}
	}

	@Override
	public void fireValueChange(Object data) {
		try {
			if (data instanceof SimpleAccount) {
				PropertyUtils.setProperty(bean, "typeid",
						((SimpleAccount) data).getId());
				itemField.setCaption(((SimpleAccount) data).getAccountname());
			} else if (data instanceof SimpleCampaign) {
				PropertyUtils.setProperty(bean, "typeid",
						((SimpleCampaign) data).getId());
				itemField.setCaption(((SimpleCampaign) data).getCampaignname());
			} else if (data instanceof SimpleContact) {
				PropertyUtils.setProperty(bean, "typeid",
						((SimpleContact) data).getId());
				itemField.setCaption(((SimpleContact) data).getContactName());
			} else if (data instanceof SimpleLead) {
				PropertyUtils.setProperty(bean, "typeid",
						((SimpleLead) data).getId());
				itemField.setCaption(((SimpleLead) data).getLeadName());
			} else if (data instanceof SimpleOpportunity) {
				PropertyUtils.setProperty(bean, "typeid",
						((SimpleOpportunity) data).getId());
				itemField.setCaption(((SimpleOpportunity) data)
						.getOpportunityname());
			} else if (data instanceof SimpleCase) {
				PropertyUtils.setProperty(bean, "typeid",
						((SimpleCase) data).getId());
				itemField.setCaption(((SimpleCase) data).getSubject());
			}
		} catch (Exception e) {
			log.error("Error when fire value", e);
		}
	}

	private class RelatedItemComboBox extends ValueComboBox {

		private static final long serialVersionUID = 1L;

		public RelatedItemComboBox(String[] types) {
			super();
			setCaption(null);
			this.setWidth("100px");
			this.loadData(types);
			this.select(getNullSelectionItemId());
		}
	}
}

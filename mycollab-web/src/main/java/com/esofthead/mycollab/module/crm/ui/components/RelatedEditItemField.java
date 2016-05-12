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

import com.esofthead.mycollab.module.crm.CrmTypeConstants;
import com.esofthead.mycollab.module.crm.domain.*;
import com.esofthead.mycollab.module.crm.i18n.*;
import com.esofthead.mycollab.module.crm.service.*;
import com.esofthead.mycollab.module.crm.view.account.AccountSelectionWindow;
import com.esofthead.mycollab.module.crm.view.campaign.CampaignSelectionWindow;
import com.esofthead.mycollab.module.crm.view.cases.CaseSelectionWindow;
import com.esofthead.mycollab.module.crm.view.contact.ContactSelectionWindow;
import com.esofthead.mycollab.module.crm.view.lead.LeadSelectionWindow;
import com.esofthead.mycollab.module.crm.view.opportunity.OpportunitySelectionWindow;
import com.esofthead.mycollab.spring.AppContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.FieldSelection;
import com.esofthead.mycollab.vaadin.web.ui.KeyCaptionComboBox;
import com.esofthead.mycollab.vaadin.web.ui.UIConstants;
import com.vaadin.data.Property;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.viritin.layouts.MHorizontalLayout;

/**
 * @author MyCollab Ltd.
 * @since 2.0
 */
public class RelatedEditItemField extends CustomField<String> implements FieldSelection {
    private static final long serialVersionUID = 1L;

    private static final Logger LOG = LoggerFactory.getLogger(RelatedEditItemField.class);

    private RelatedItemComboBox relatedItemComboBox;
    private Object bean;

    private TextField itemField;
    private Button browseBtn;
    private Button clearBtn;

    public RelatedEditItemField(Object bean) {
        this.bean = bean;

        relatedItemComboBox = new RelatedItemComboBox();
        itemField = new TextField();
        itemField.setEnabled(true);

        browseBtn = new Button(null, FontAwesome.ELLIPSIS_H);
        browseBtn.addStyleName(UIConstants.BUTTON_OPTION);
        browseBtn.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                String type = (String) relatedItemComboBox.getValue();
                if (CrmTypeConstants.ACCOUNT.equals(type)) {
                    AccountSelectionWindow accountWindow = new AccountSelectionWindow(RelatedEditItemField.this);
                    UI.getCurrent().addWindow(accountWindow);
                    accountWindow.show();
                } else if (CrmTypeConstants.CAMPAIGN.equals(type)) {
                    CampaignSelectionWindow campaignWindow = new CampaignSelectionWindow(RelatedEditItemField.this);
                    UI.getCurrent().addWindow(campaignWindow);
                    campaignWindow.show();
                } else if (CrmTypeConstants.CONTACT.equals(type)) {
                    ContactSelectionWindow contactWindow = new ContactSelectionWindow(RelatedEditItemField.this);
                    UI.getCurrent().addWindow(contactWindow);
                    contactWindow.show();
                } else if (CrmTypeConstants.LEAD.equals(type)) {
                    LeadSelectionWindow leadWindow = new LeadSelectionWindow(RelatedEditItemField.this);
                    UI.getCurrent().addWindow(leadWindow);
                    leadWindow.show();
                } else if (CrmTypeConstants.OPPORTUNITY.equals(type)) {
                    OpportunitySelectionWindow opportunityWindow = new OpportunitySelectionWindow(RelatedEditItemField.this);
                    UI.getCurrent().addWindow(opportunityWindow);
                    opportunityWindow.show();
                } else if (CrmTypeConstants.CASE.equals(type)) {
                    CaseSelectionWindow caseWindow = new CaseSelectionWindow(RelatedEditItemField.this);
                    UI.getCurrent().addWindow(caseWindow);
                    caseWindow.show();
                } else {
                    relatedItemComboBox.focus();
                }
            }
        });

        clearBtn = new Button(null, FontAwesome.TRASH_O);
        clearBtn.addStyleName(UIConstants.BUTTON_OPTION);
        clearBtn.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                try {
                    PropertyUtils.setProperty(RelatedEditItemField.this.bean,
                            "typeid", null);
                } catch (Exception e) {
                    LOG.error("Error while saving type", e);
                }
            }
        });
    }

    @Override
    protected Component initContent() {
        return new MHorizontalLayout().with(relatedItemComboBox, itemField, browseBtn, clearBtn).alignAll(Alignment.MIDDLE_LEFT);
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
        LOG.debug("Set type: " + type);
        relatedItemComboBox.select(type);
        try {
            Integer typeid = (Integer) PropertyUtils.getProperty(bean, "typeid");
            if (typeid != null) {
                if (CrmTypeConstants.ACCOUNT.equals(type)) {
                    AccountService accountService = AppContextUtil.getSpringBean(AccountService.class);
                    SimpleAccount account = accountService.findById(typeid, AppContext.getAccountId());
                    if (account != null) {
                        itemField.setValue(account.getAccountname());
                    }
                } else if (CrmTypeConstants.CAMPAIGN.equals(type)) {
                    CampaignService campaignService = AppContextUtil.getSpringBean(CampaignService.class);
                    SimpleCampaign campaign = campaignService.findById(typeid, AppContext.getAccountId());
                    if (campaign != null) {
                        itemField.setValue(campaign.getCampaignname());
                    }
                } else if (CrmTypeConstants.CONTACT.equals(type)) {
                    ContactService contactService = AppContextUtil.getSpringBean(ContactService.class);
                    SimpleContact contact = contactService.findById(typeid, AppContext.getAccountId());
                    if (contact != null) {
                        itemField.setValue(contact.getContactName());
                    }
                } else if (CrmTypeConstants.LEAD.equals(type)) {
                    LeadService leadService = AppContextUtil.getSpringBean(LeadService.class);
                    SimpleLead lead = leadService.findById(typeid, AppContext.getAccountId());
                    if (lead != null) {
                        itemField.setValue(lead.getLeadName());
                    }
                } else if (CrmTypeConstants.OPPORTUNITY.equals(type)) {
                    OpportunityService opportunityService = AppContextUtil
                            .getSpringBean(OpportunityService.class);
                    SimpleOpportunity opportunity = opportunityService
                            .findById(typeid, AppContext.getAccountId());
                    if (opportunity != null) {
                        itemField.setValue(opportunity.getOpportunityname());
                    }
                } else if (CrmTypeConstants.CASE.equals(type)) {
                    CaseService caseService = AppContextUtil.getSpringBean(CaseService.class);
                    SimpleCase cases = caseService.findById(typeid, AppContext.getAccountId());
                    if (cases != null) {
                        itemField.setValue(cases.getSubject());
                    }
                }
            }

        } catch (Exception e) {
            LOG.error("Error when set type", e);
        }
    }

    @Override
    public void fireValueChange(Object data) {
        try {
            if (data instanceof SimpleAccount) {
                PropertyUtils.setProperty(bean, "typeid", ((SimpleAccount) data).getId());
                itemField.setValue(((SimpleAccount) data).getAccountname());
            } else if (data instanceof SimpleCampaign) {
                PropertyUtils.setProperty(bean, "typeid", ((SimpleCampaign) data).getId());
                itemField.setValue(((SimpleCampaign) data).getCampaignname());
            } else if (data instanceof SimpleContact) {
                PropertyUtils.setProperty(bean, "typeid", ((SimpleContact) data).getId());
                itemField.setValue(((SimpleContact) data).getContactName());
            } else if (data instanceof SimpleLead) {
                PropertyUtils.setProperty(bean, "typeid", ((SimpleLead) data).getId());
                itemField.setValue(((SimpleLead) data).getLeadName());
            } else if (data instanceof SimpleOpportunity) {
                PropertyUtils.setProperty(bean, "typeid", ((SimpleOpportunity) data).getId());
                itemField.setValue(((SimpleOpportunity) data).getOpportunityname());
            } else if (data instanceof SimpleCase) {
                PropertyUtils.setProperty(bean, "typeid", ((SimpleCase) data).getId());
                itemField.setValue(((SimpleCase) data).getSubject());
            }
        } catch (Exception e) {
            LOG.error("Error when fire value", e);
        }
    }

    private static class RelatedItemComboBox extends KeyCaptionComboBox {
        private static final long serialVersionUID = 1L;

        public RelatedItemComboBox() {
            super(true);
            setCaption(null);
            this.setWidth("100px");
            this.addItem(CrmTypeConstants.ACCOUNT, AppContext.getMessage(AccountI18nEnum.SINGLE));
            this.addItem(CrmTypeConstants.CAMPAIGN, AppContext.getMessage(CampaignI18nEnum.SINGLE));
            this.addItem(CrmTypeConstants.CONTACT, AppContext.getMessage(ContactI18nEnum.SINGLE));
            this.addItem(CrmTypeConstants.LEAD, AppContext.getMessage(LeadI18nEnum.SINGLE));
            this.addItem(CrmTypeConstants.OPPORTUNITY, AppContext.getMessage(OpportunityI18nEnum.SINGLE));
            this.addItem(CrmTypeConstants.CASE, AppContext.getMessage(CaseI18nEnum.SINGLE));
            this.select(getNullSelectionItemId());
        }
    }
}
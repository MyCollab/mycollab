package com.mycollab.module.crm.ui.components;

import com.mycollab.module.crm.CrmTypeConstants;
import com.mycollab.module.crm.domain.*;
import com.mycollab.module.crm.i18n.*;
import com.mycollab.module.crm.service.*;
import com.mycollab.module.crm.view.account.AccountSelectionWindow;
import com.mycollab.module.crm.view.campaign.CampaignSelectionWindow;
import com.mycollab.module.crm.view.cases.CaseSelectionWindow;
import com.mycollab.module.crm.view.contact.ContactSelectionWindow;
import com.mycollab.module.crm.view.lead.LeadSelectionWindow;
import com.mycollab.module.crm.view.opportunity.OpportunitySelectionWindow;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.FieldSelection;
import com.mycollab.vaadin.web.ui.KeyCaptionComboBox;
import com.mycollab.vaadin.web.ui.WebThemes;
import com.vaadin.data.Property;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.viritin.button.MButton;
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
    private MButton browseBtn, clearBtn;

    public RelatedEditItemField(Object bean) {
        this.bean = bean;

        relatedItemComboBox = new RelatedItemComboBox();
        itemField = new TextField();
        itemField.setEnabled(true);

        browseBtn = new MButton("", clickEvent -> {
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
        }).withIcon(FontAwesome.ELLIPSIS_H).withStyleName(WebThemes.BUTTON_OPTION, WebThemes.BUTTON_SMALL_PADDING);

        clearBtn = new MButton("", clickEvent -> {
            try {
                PropertyUtils.setProperty(bean, "typeid", null);
                PropertyUtils.setProperty(bean, "type", null);
                relatedItemComboBox.setValue(null);
                itemField.setValue("");
            } catch (Exception e) {
                LOG.error("Error while saving type", e);
            }
        }).withIcon(FontAwesome.TRASH_O).withStyleName(WebThemes.BUTTON_OPTION, WebThemes.BUTTON_SMALL_PADDING);
    }

    @Override
    protected Component initContent() {
        return new MHorizontalLayout(relatedItemComboBox, itemField, browseBtn, clearBtn).alignAll(Alignment.MIDDLE_LEFT);
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
        relatedItemComboBox.select(type);
        try {
            Integer typeId = (Integer) PropertyUtils.getProperty(bean, "typeid");
            if (typeId != null) {
                if (CrmTypeConstants.ACCOUNT.equals(type)) {
                    AccountService accountService = AppContextUtil.getSpringBean(AccountService.class);
                    SimpleAccount account = accountService.findById(typeId, AppUI.getAccountId());
                    if (account != null) {
                        itemField.setValue(account.getAccountname());
                    }
                } else if (CrmTypeConstants.CAMPAIGN.equals(type)) {
                    CampaignService campaignService = AppContextUtil.getSpringBean(CampaignService.class);
                    SimpleCampaign campaign = campaignService.findById(typeId, AppUI.getAccountId());
                    if (campaign != null) {
                        itemField.setValue(campaign.getCampaignname());
                    }
                } else if (CrmTypeConstants.CONTACT.equals(type)) {
                    ContactService contactService = AppContextUtil.getSpringBean(ContactService.class);
                    SimpleContact contact = contactService.findById(typeId, AppUI.getAccountId());
                    if (contact != null) {
                        itemField.setValue(contact.getContactName());
                    }
                } else if (CrmTypeConstants.LEAD.equals(type)) {
                    LeadService leadService = AppContextUtil.getSpringBean(LeadService.class);
                    SimpleLead lead = leadService.findById(typeId, AppUI.getAccountId());
                    if (lead != null) {
                        itemField.setValue(lead.getLeadName());
                    }
                } else if (CrmTypeConstants.OPPORTUNITY.equals(type)) {
                    OpportunityService opportunityService = AppContextUtil.getSpringBean(OpportunityService.class);
                    SimpleOpportunity opportunity = opportunityService.findById(typeId, AppUI.getAccountId());
                    if (opportunity != null) {
                        itemField.setValue(opportunity.getOpportunityname());
                    }
                } else if (CrmTypeConstants.CASE.equals(type)) {
                    CaseService caseService = AppContextUtil.getSpringBean(CaseService.class);
                    SimpleCase cases = caseService.findById(typeId, AppUI.getAccountId());
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

        private RelatedItemComboBox() {
            super(true);
            setCaption(null);
            this.setWidth("100px");
            this.addItem(CrmTypeConstants.ACCOUNT, UserUIContext.getMessage(AccountI18nEnum.SINGLE));
            this.addItem(CrmTypeConstants.CAMPAIGN, UserUIContext.getMessage(CampaignI18nEnum.SINGLE));
            this.addItem(CrmTypeConstants.CONTACT, UserUIContext.getMessage(ContactI18nEnum.SINGLE));
            this.addItem(CrmTypeConstants.LEAD, UserUIContext.getMessage(LeadI18nEnum.SINGLE));
            this.addItem(CrmTypeConstants.OPPORTUNITY, UserUIContext.getMessage(OpportunityI18nEnum.SINGLE));
            this.addItem(CrmTypeConstants.CASE, UserUIContext.getMessage(CaseI18nEnum.SINGLE));
            this.select(getNullSelectionItemId());
        }
    }
}
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
package com.mycollab.mobile.module.crm.view.activity;

import com.mycollab.module.crm.CrmTypeConstants;
import com.mycollab.module.crm.domain.*;
import com.mycollab.module.crm.service.*;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppContext;
import com.mycollab.vaadin.ui.FieldSelection;
import com.vaadin.addon.touchkit.ui.NavigationButton;
import com.vaadin.data.Property;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;

/**
 * @author MyCollab Ltd.
 * @since 4.2
 */
public class RelatedItemSelectionField extends CustomField<Integer> implements FieldSelection {
    private static final long serialVersionUID = -3572873867793792681L;

    private static final Logger LOG = LoggerFactory.getLogger(RelatedItemSelectionField.class);

    protected NavigationButton navButton = new NavigationButton();
    protected Object bean;

    public RelatedItemSelectionField(Object bean) {
        this.bean = bean;
    }

    @Override
    public void setPropertyDataSource(Property newDataSource) {
        Object value = newDataSource.getValue();
        if (value instanceof Integer) {
            setValue((Integer) value);
            super.setPropertyDataSource(newDataSource);
        } else {
            super.setPropertyDataSource(newDataSource);
        }
    }

    @Override
    public void setValue(Integer typeid) {
        try {
            String type = (String) PropertyUtils.getProperty(bean, "type");
            if (type != null && typeid != null) {
                if ("Account".equals(type)) {
                    AccountService accountService = AppContextUtil.getSpringBean(AccountService.class);
                    SimpleAccount account = accountService.findById(typeid, AppContext.getAccountId());
                    if (account != null) {
                        navButton.setCaption(account.getAccountname());
                    }
                } else if ("Campaign".equals(type)) {
                    CampaignService campaignService = AppContextUtil.getSpringBean(CampaignService.class);
                    SimpleCampaign campaign = campaignService.findById(typeid, AppContext.getAccountId());
                    if (campaign != null) {
                        navButton.setCaption(campaign.getCampaignname());
                    }
                } else if ("Contact".equals(type)) {
                    ContactService contactService = AppContextUtil.getSpringBean(ContactService.class);
                    SimpleContact contact = contactService.findById(typeid, AppContext.getAccountId());
                    if (contact != null) {
                        navButton.setCaption(contact.getContactName());
                    }
                } else if ("Lead".equals(type)) {
                    LeadService leadService = AppContextUtil.getSpringBean(LeadService.class);
                    SimpleLead lead = leadService.findById(typeid, AppContext.getAccountId());
                    if (lead != null) {
                        navButton.setCaption(lead.getLeadName());
                    }
                } else if ("Opportunity".equals(type)) {
                    OpportunityService opportunityService = AppContextUtil.getSpringBean(OpportunityService.class);
                    SimpleOpportunity opportunity = opportunityService.findById(typeid, AppContext.getAccountId());
                    if (opportunity != null) {
                        navButton.setCaption(opportunity.getOpportunityname());
                    }
                } else if ("Case".equals(type)) {
                    CaseService caseService = AppContextUtil.getSpringBean(CaseService.class);
                    SimpleCase cases = caseService.findById(typeid, AppContext.getAccountId());
                    if (cases != null) {
                        navButton.setCaption(cases.getSubject());
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
            Object dataId = PropertyUtils.getProperty(data, "id");
            if (dataId == null) {
                PropertyUtils.setProperty(bean, "type", null);
                return;
            }

            setInternalValue((Integer) dataId);

            if (data instanceof SimpleAccount) {
                PropertyUtils.setProperty(bean, "type", CrmTypeConstants.ACCOUNT);
                navButton.setCaption(((SimpleAccount) data).getAccountname());
            } else if (data instanceof SimpleCampaign) {
                PropertyUtils.setProperty(bean, "type", CrmTypeConstants.CAMPAIGN);
                navButton.setCaption(((SimpleCampaign) data).getCampaignname());
            } else if (data instanceof SimpleContact) {
                PropertyUtils.setProperty(bean, "type", CrmTypeConstants.CONTACT);
                navButton.setCaption(((SimpleContact) data).getContactName());
            } else if (data instanceof SimpleLead) {
                PropertyUtils.setProperty(bean, "type", CrmTypeConstants.LEAD);
                navButton.setCaption(((SimpleLead) data).getLeadName());
            } else if (data instanceof SimpleOpportunity) {
                PropertyUtils.setProperty(bean, "type", CrmTypeConstants.OPPORTUNITY);
                navButton.setCaption(((SimpleOpportunity) data).getOpportunityname());
            } else if (data instanceof SimpleCase) {
                PropertyUtils.setProperty(bean, "type", CrmTypeConstants.CASE);
                navButton.setCaption(((SimpleCase) data).getSubject());
            }
        } catch (Exception e) {
            LOG.error("Error when fire value", e);
        }
    }

    @Override
    protected Component initContent() {
        final RelatedItemSelectionView targetView = new RelatedItemSelectionView(this);
        navButton.setWidth("100%");
        navButton.setTargetView(targetView);
        navButton.addClickListener(new NavigationButton.NavigationButtonClickListener() {
            private static final long serialVersionUID = -3929991734772006774L;

            @Override
            public void buttonClick(NavigationButton.NavigationButtonClickEvent event) {
                try {
                    targetView.selectTab((String) PropertyUtils.getProperty(bean, "type"));
                } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    LOG.error("Error when select tab", e);
                }
            }
        });
        return navButton;
    }

    @Override
    public Class<? extends Integer> getType() {
        return Integer.class;
    }

}

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
package com.mycollab.mobile.module.crm.ui;

import com.mycollab.configuration.StorageFactory;
import com.mycollab.module.crm.domain.*;
import com.mycollab.module.crm.service.*;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppContext;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.Resource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.Label;
import org.apache.commons.beanutils.PropertyUtils;

/**
 * @author MyCollab Ltd.
 * @since 4.1
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
            final String type = (String) PropertyUtils.getProperty(RelatedReadItemField.this.bean, "type");
            if (type == null || type.equals("")) {
                return new Label("");
            }

            final Integer typeid = (Integer) PropertyUtils.getProperty(bean, "typeid");
            if (typeid == null) {
                return new Label("");
            }

            Resource relatedLink = null;
            String relateItemName = null;

            if ("Account".equals(type)) {
                AccountService accountService = AppContextUtil.getSpringBean(AccountService.class);
                final SimpleAccount account = accountService.findById(typeid, AppContext.getAccountId());
                if (account != null) {
                    relateItemName = account.getAccountname();
                    relatedLink = new ExternalResource(StorageFactory.generateAssetRelativeLink("icons/16/crm/account.png"));
                }
            } else if ("Campaign".equals(type)) {
                CampaignService campaignService = AppContextUtil.getSpringBean(CampaignService.class);
                final SimpleCampaign campaign = campaignService.findById(typeid, AppContext.getAccountId());
                if (campaign != null) {
                    relateItemName = campaign.getCampaignname();
                    relatedLink = new ExternalResource(StorageFactory.generateAssetRelativeLink("icons/16/crm/campaign.png"));
                }
            } else if ("Contact".equals(type)) {
                ContactService contactService = AppContextUtil.getSpringBean(ContactService.class);
                final SimpleContact contact = contactService.findById(typeid, AppContext.getAccountId());
                if (contact != null) {
                    relateItemName = contact.getContactName();
                    relatedLink = new ExternalResource(StorageFactory.generateAssetRelativeLink("icons/16/crm/contact.png"));

                }
            } else if ("Lead".equals(type)) {
                LeadService leadService = AppContextUtil.getSpringBean(LeadService.class);
                final SimpleLead lead = leadService.findById(typeid, AppContext.getAccountId());
                if (lead != null) {
                    relateItemName = lead.getLeadName();
                    relatedLink = new ExternalResource(StorageFactory.generateAssetRelativeLink("icons/16/crm/lead.png"));
                }
            } else if ("Opportunity".equals(type)) {
                OpportunityService opportunityService = AppContextUtil.getSpringBean(OpportunityService.class);
                final SimpleOpportunity opportunity = opportunityService.findById(typeid, AppContext.getAccountId());
                if (opportunity != null) {
                    relateItemName = opportunity.getOpportunityname();
                    relatedLink = new ExternalResource(StorageFactory.generateAssetRelativeLink("icons/16/crm/opportunity.png"));

                }
            } else if ("Case".equals(type)) {
                CaseService caseService = AppContextUtil.getSpringBean(CaseService.class);
                final SimpleCase cases = caseService.findById(typeid, AppContext.getAccountId());
                if (cases != null) {
                    relateItemName = cases.getSubject();
                    relatedLink = new ExternalResource(StorageFactory.generateAssetRelativeLink("icons/16/crm/case.png"));
                }
            }

            Button related = new Button(relateItemName);
            if (relatedLink != null)
                related.setIcon(relatedLink);

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

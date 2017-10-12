/**
 * Copyright © MyCollab
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.mobile.module.crm.ui;

import com.mycollab.module.crm.domain.*;
import com.mycollab.module.crm.service.*;
import com.mycollab.module.file.StorageUtils;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppUI;
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

            final Integer typeId = (Integer) PropertyUtils.getProperty(bean, "typeid");
            if (typeId == null) {
                return new Label("");
            }

            Resource relatedLink = null;
            String relateItemName = null;

            if ("Account".equals(type)) {
                AccountService accountService = AppContextUtil.getSpringBean(AccountService.class);
                final SimpleAccount account = accountService.findById(typeId, AppUI.getAccountId());
                if (account != null) {
                    relateItemName = account.getAccountname();
                    relatedLink = new ExternalResource(StorageUtils.generateAssetRelativeLink("icons/16/crm/account.png"));
                }
            } else if ("Campaign".equals(type)) {
                CampaignService campaignService = AppContextUtil.getSpringBean(CampaignService.class);
                final SimpleCampaign campaign = campaignService.findById(typeId, AppUI.getAccountId());
                if (campaign != null) {
                    relateItemName = campaign.getCampaignname();
                    relatedLink = new ExternalResource(StorageUtils.generateAssetRelativeLink("icons/16/crm/campaign.png"));
                }
            } else if ("Contact".equals(type)) {
                ContactService contactService = AppContextUtil.getSpringBean(ContactService.class);
                final SimpleContact contact = contactService.findById(typeId, AppUI.getAccountId());
                if (contact != null) {
                    relateItemName = contact.getContactName();
                    relatedLink = new ExternalResource(StorageUtils.generateAssetRelativeLink("icons/16/crm/contact.png"));

                }
            } else if ("Lead".equals(type)) {
                LeadService leadService = AppContextUtil.getSpringBean(LeadService.class);
                final SimpleLead lead = leadService.findById(typeId, AppUI.getAccountId());
                if (lead != null) {
                    relateItemName = lead.getLeadName();
                    relatedLink = new ExternalResource(StorageUtils.generateAssetRelativeLink("icons/16/crm/lead.png"));
                }
            } else if ("Opportunity".equals(type)) {
                OpportunityService opportunityService = AppContextUtil.getSpringBean(OpportunityService.class);
                final SimpleOpportunity opportunity = opportunityService.findById(typeId, AppUI.getAccountId());
                if (opportunity != null) {
                    relateItemName = opportunity.getOpportunityname();
                    relatedLink = new ExternalResource(StorageUtils.generateAssetRelativeLink("icons/16/crm/opportunity.png"));

                }
            } else if ("Case".equals(type)) {
                CaseService caseService = AppContextUtil.getSpringBean(CaseService.class);
                final SimpleCase cases = caseService.findById(typeId, AppUI.getAccountId());
                if (cases != null) {
                    relateItemName = cases.getSubject();
                    relatedLink = new ExternalResource(StorageUtils.generateAssetRelativeLink("icons/16/crm/case.png"));
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

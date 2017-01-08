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
package com.mycollab.module.crm.ui.components;

import com.mycollab.module.crm.CrmTypeConstants;
import com.mycollab.module.crm.CrmLinkBuilder;
import com.mycollab.module.crm.ui.CrmAssetsManager;
import com.mycollab.module.crm.domain.*;
import com.mycollab.module.crm.service.*;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.MyCollabUI;
import com.mycollab.vaadin.web.ui.LabelLink;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.Label;
import org.apache.commons.beanutils.PropertyUtils;

/**
 * @author MyCollab Ltd.
 * @since 1.0
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
            final Integer typeId = (Integer) PropertyUtils.getProperty(RelatedReadItemField.this.bean, "typeid");
            if (typeId == null) {
                return new Label("");
            }

            final String type = (String) PropertyUtils.getProperty(bean, "type");
            if (type == null || type.equals("")) {
                return new Label("");
            }

            FontAwesome relatedLink = null;
            String relateItemName = null;

            if (CrmTypeConstants.ACCOUNT.equals(type)) {
                AccountService accountService = AppContextUtil.getSpringBean(AccountService.class);
                final SimpleAccount account = accountService.findById(typeId, MyCollabUI.getAccountId());
                if (account != null) {
                    relateItemName = account.getAccountname();
                    relatedLink = CrmAssetsManager.getAsset(CrmTypeConstants.ACCOUNT);
                }
            } else if (CrmTypeConstants.CAMPAIGN.equals(type)) {
                CampaignService campaignService = AppContextUtil.getSpringBean(CampaignService.class);
                final SimpleCampaign campaign = campaignService.findById(typeId, MyCollabUI.getAccountId());
                if (campaign != null) {
                    relateItemName = campaign.getCampaignname();
                    relatedLink = CrmAssetsManager.getAsset(CrmTypeConstants.CAMPAIGN);

                }
            } else if (CrmTypeConstants.CONTACT.equals(type)) {
                ContactService contactService = AppContextUtil.getSpringBean(ContactService.class);
                final SimpleContact contact = contactService.findById(typeId, MyCollabUI.getAccountId());
                if (contact != null) {
                    relateItemName = contact.getContactName();
                    relatedLink = CrmAssetsManager.getAsset(CrmTypeConstants.CONTACT);

                }
            } else if (CrmTypeConstants.LEAD.equals(type)) {
                LeadService leadService = AppContextUtil.getSpringBean(LeadService.class);
                final SimpleLead lead = leadService.findById(typeId, MyCollabUI.getAccountId());
                if (lead != null) {
                    relateItemName = lead.getLeadName();
                    relatedLink = CrmAssetsManager.getAsset(CrmTypeConstants.LEAD);

                }
            } else if (CrmTypeConstants.OPPORTUNITY.equals(type)) {
                OpportunityService opportunityService = AppContextUtil.getSpringBean(OpportunityService.class);
                final SimpleOpportunity opportunity = opportunityService.findById(typeId, MyCollabUI.getAccountId());
                if (opportunity != null) {
                    relateItemName = opportunity.getOpportunityname();
                    relatedLink = CrmAssetsManager.getAsset(CrmTypeConstants.OPPORTUNITY);

                }
            } else if (CrmTypeConstants.CASE.equals(type)) {
                CaseService caseService = AppContextUtil.getSpringBean(CaseService.class);
                final SimpleCase cases = caseService.findById(typeId, MyCollabUI.getAccountId());
                if (cases != null) {
                    relateItemName = cases.getSubject();
                    relatedLink = CrmAssetsManager.getAsset(CrmTypeConstants.CASE);
                }
            }

            LabelLink related = new LabelLink(relateItemName, CrmLinkBuilder.generateActivityPreviewLinkFull(type, typeId));
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
/**
 * mycollab-web - Parent pom providing dependency and plugin management for applications
		built with Maven
 * Copyright © ${project.inceptionYear} MyCollab (support@mycollab.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.crm.view.lead;

import com.mycollab.module.crm.domain.Lead;
import com.mycollab.module.crm.domain.SimpleLead;
import com.mycollab.module.crm.i18n.OptionI18nEnum.AccountIndustry;
import com.mycollab.module.crm.i18n.OptionI18nEnum.LeadStatus;
import com.mycollab.module.crm.i18n.OptionI18nEnum.OpportunityLeadSource;
import com.mycollab.vaadin.ui.AbstractBeanFieldGroupViewFieldFactory;
import com.mycollab.vaadin.ui.GenericBeanForm;
import com.mycollab.vaadin.ui.UIConstants;
import com.mycollab.vaadin.ui.field.*;
import com.mycollab.vaadin.web.ui.field.LinkViewField;
import com.mycollab.vaadin.web.ui.field.UserLinkViewField;
import com.vaadin.ui.Field;

/**
 * @author MyCollab Ltd.
 * @since 3.0
 */
class LeadReadFormFieldFactory extends AbstractBeanFieldGroupViewFieldFactory<SimpleLead> {
    private static final long serialVersionUID = 1L;

    LeadReadFormFieldFactory(GenericBeanForm<SimpleLead> form) {
        super(form);
    }

    @Override
    protected Field<?> onCreateField(Object propertyId) {
        SimpleLead lead = attachForm.getBean();

        if (propertyId.equals("firstname")) {
            String prefix = "", firstName = "";
            if (lead.getPrefixname() != null) {
                prefix = lead.getPrefixname();
            }

            if (lead.getFirstname() != null) {
                firstName = lead.getFirstname();
            }

            return new DefaultViewField(prefix + firstName);
        } else if (propertyId.equals("website")) {
            return new UrlLinkViewField(lead.getWebsite());
        } else if (propertyId.equals("email")) {
            return new EmailViewField(lead.getEmail());
        } else if (propertyId.equals("accountid")) {
            return new LinkViewField(lead.getAccountname(), null, null);
        } else if (propertyId.equals("assignuser")) {
            return new UserLinkViewField(lead.getAssignuser(), lead.getAssignUserAvatarId(), lead.getAssignUserFullName());
        } else if (propertyId.equals("description")) {
            return new RichTextViewField(lead.getDescription());
        } else if (Lead.Field.status.equalTo(propertyId)) {
            return new I18nFormViewField(lead.getStatus(), LeadStatus.class).withStyleName(UIConstants.FIELD_NOTE);
        } else if (Lead.Field.industry.equalTo(propertyId)) {
            return new I18nFormViewField(lead.getIndustry(), AccountIndustry.class);
        } else if (Lead.Field.source.equalTo(propertyId)) {
            return new I18nFormViewField(lead.getSource(), OpportunityLeadSource.class);
        } else if (Lead.Field.primcountry.equalTo(propertyId)) {
            return new CountryViewField(lead.getPrimcountry());
        } else if (Lead.Field.othercountry.equalTo(propertyId)) {
            return new CountryViewField(lead.getOthercountry());
        }

        return null;
    }

}

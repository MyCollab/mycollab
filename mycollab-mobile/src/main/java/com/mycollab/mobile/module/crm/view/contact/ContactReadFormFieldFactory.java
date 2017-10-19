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
package com.mycollab.mobile.module.crm.view.contact;

import com.hp.gagawa.java.elements.A;
import com.hp.gagawa.java.elements.Div;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.html.DivLessFormatter;
import com.mycollab.module.crm.CrmLinkGenerator;
import com.mycollab.module.crm.CrmTypeConstants;
import com.mycollab.module.crm.domain.Contact;
import com.mycollab.module.crm.domain.SimpleContact;
import com.mycollab.module.crm.i18n.OptionI18nEnum.OpportunityLeadSource;
import com.mycollab.module.crm.ui.CrmAssetsManager;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.AbstractBeanFieldGroupViewFieldFactory;
import com.mycollab.vaadin.ui.GenericBeanForm;
import com.mycollab.vaadin.ui.UIConstants;
import com.mycollab.vaadin.ui.field.CountryViewField;
import com.mycollab.vaadin.ui.field.DefaultViewField;
import com.mycollab.vaadin.ui.field.EmailViewField;
import com.mycollab.vaadin.ui.field.I18nFormViewField;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Field;

/**
 * @author MyCollab Ltd.
 * @since 4.0
 */
class ContactReadFormFieldFactory extends AbstractBeanFieldGroupViewFieldFactory<SimpleContact> {
    private static final long serialVersionUID = 1L;

    ContactReadFormFieldFactory(GenericBeanForm<SimpleContact> form) {
        super(form);
    }

    @Override
    protected Field<?> onCreateField(Object propertyId) {
        SimpleContact contact = attachForm.getBean();
        if (Contact.Field.accountid.equalTo(propertyId)) {
            if (contact.getAccountid() != null) {
                A accountLink = new A(CrmLinkGenerator.generateAccountPreviewLink(contact.getAccountid()))
                        .appendText(contact.getAccountName());
                Div accountDiv = new Div().appendText(CrmAssetsManager.getAsset(CrmTypeConstants.ACCOUNT).getHtml()).appendChild(DivLessFormatter.EMPTY_SPACE, accountLink);
                return new DefaultViewField(accountDiv.write(), ContentMode.HTML);
            }
        } else if (Contact.Field.email.equalTo(propertyId)) {
            return new EmailViewField(contact.getEmail());
        } else if (Contact.Field.assignuser.equalTo(propertyId)) {
            return new DefaultViewField(contact.getAssignUserFullName());
        } else if (Contact.Field.iscallable.equalTo(propertyId)) {
            if (Boolean.FALSE.equals(contact.getIscallable())) {
                return new DefaultViewField(UserUIContext.getMessage(GenericI18Enum.BUTTON_NO));
            } else {
                return new DefaultViewField(UserUIContext.getMessage(GenericI18Enum.BUTTON_YES));
            }
        } else if (Contact.Field.birthday.equalTo(propertyId)) {
            return new DefaultViewField(UserUIContext.formatDate(contact.getBirthday()));
        } else if (Contact.Field.firstname.equalTo(propertyId)) {
            return new DefaultViewField(contact.getFirstname());
        } else if (Contact.Field.leadsource.equalTo(propertyId)) {
            return new I18nFormViewField(contact.getLeadsource(), OpportunityLeadSource.class).withStyleName(UIConstants.FIELD_NOTE);
        } else if (Contact.Field.primcountry.equalTo(propertyId)) {
            return new CountryViewField(contact.getPrimcountry());
        } else if (Contact.Field.othercountry.equalTo(propertyId)) {
            return new CountryViewField(contact.getOthercountry());
        }

        return null;
    }
}

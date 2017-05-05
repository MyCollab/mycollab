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
package com.mycollab.mobile.module.crm.view.contact;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.module.crm.domain.Contact;
import com.mycollab.module.crm.domain.SimpleContact;
import com.mycollab.module.crm.i18n.OptionI18nEnum;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.AbstractBeanFieldGroupViewFieldFactory;
import com.mycollab.vaadin.ui.GenericBeanForm;
import com.mycollab.vaadin.ui.UIConstants;
import com.mycollab.vaadin.ui.field.CountryViewField;
import com.mycollab.vaadin.ui.field.DefaultViewField;
import com.mycollab.vaadin.ui.field.EmailViewField;
import com.mycollab.vaadin.ui.field.I18nFormViewField;
import com.vaadin.ui.Field;

/**
 * @author MyCollab Ltd.
 * @since 4.0
 */
public class ContactReadFormFieldFactory extends AbstractBeanFieldGroupViewFieldFactory<SimpleContact> {
    private static final long serialVersionUID = 1L;

    public ContactReadFormFieldFactory(GenericBeanForm<SimpleContact> form) {
        super(form);
    }

    @Override
    protected Field<?> onCreateField(Object propertyId) {
        SimpleContact contact = attachForm.getBean();
        if (propertyId.equals("accountid")) {
            return new DefaultViewField(contact.getAccountName());
        } else if (propertyId.equals("email")) {
            return new EmailViewField(contact.getEmail());
        } else if (propertyId.equals("assignuser")) {
            return new DefaultViewField(contact.getAssignUserFullName());
        } else if (propertyId.equals("iscallable")) {
            if (Boolean.FALSE.equals(contact.getIscallable())) {
                return new DefaultViewField(UserUIContext.getMessage(GenericI18Enum.BUTTON_NO));
            } else {
                return new DefaultViewField(UserUIContext.getMessage(GenericI18Enum.BUTTON_YES));
            }
        } else if (propertyId.equals("birthday")) {
            return new DefaultViewField(UserUIContext.formatDate(contact.getBirthday()));
        } else if (propertyId.equals("firstname")) {
            return new DefaultViewField(contact.getFirstname());
        } else if (Contact.Field.leadsource.equalTo(propertyId)) {
            return new I18nFormViewField(contact.getLeadsource(), OptionI18nEnum.OpportunityLeadSource.class).withStyleName(UIConstants.FIELD_NOTE);
        } else if (Contact.Field.primcountry.equalTo(propertyId)) {
            return new CountryViewField(contact.getPrimcountry());
        } else if (Contact.Field.othercountry.equalTo(propertyId)) {
            return new CountryViewField(contact.getOthercountry());
        }

        return null;
    }
}

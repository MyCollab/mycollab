/**
 * mycollab-mobile - Parent pom providing dependency and plugin management for applications
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
package com.mycollab.mobile.module.crm.view.account;

import com.mycollab.module.crm.domain.Account;
import com.mycollab.module.crm.domain.SimpleAccount;
import com.mycollab.module.crm.i18n.OptionI18nEnum.AccountIndustry;
import com.mycollab.vaadin.ui.AbstractBeanFieldGroupViewFieldFactory;
import com.mycollab.vaadin.ui.GenericBeanForm;
import com.mycollab.vaadin.ui.field.DefaultViewField;
import com.mycollab.vaadin.ui.field.I18nFormViewField;
import com.vaadin.ui.Field;

/**
 * @author MyCollab Ltd.
 * @since 3.0
 */
class AccountReadFormFieldFactory extends AbstractBeanFieldGroupViewFieldFactory<SimpleAccount> {
    private static final long serialVersionUID = 1L;

    AccountReadFormFieldFactory(GenericBeanForm<SimpleAccount> form) {
        super(form);
    }

    @Override
    protected Field<?> onCreateField(Object propertyId) {
        SimpleAccount account = attachForm.getBean();

        if (propertyId.equals("email")) {
            return new DefaultViewField(account.getEmail());
        } else if (propertyId.equals("assignuser")) {
            return new DefaultViewField(account.getAssignUserFullName());
        } else if (propertyId.equals("website")) {
            return new DefaultViewField(account.getWebsite());
        } else if (Account.Field.industry.equalTo(propertyId)) {
            return new I18nFormViewField(account.getIndustry(), AccountIndustry.class);
        }

        return null;
    }

}

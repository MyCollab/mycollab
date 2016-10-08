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
package com.mycollab.mobile.module.crm.view.account;

import com.mycollab.common.i18n.ErrorI18nEnum;
import com.mycollab.mobile.module.user.ui.components.ActiveUserComboBox;
import com.mycollab.mobile.ui.CountryListSelect;
import com.mycollab.mobile.ui.IndustryListSelect;
import com.mycollab.module.crm.domain.Account;
import com.mycollab.module.crm.i18n.AccountI18nEnum;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.AbstractBeanFieldGroupEditFieldFactory;
import com.mycollab.vaadin.ui.GenericBeanForm;
import com.vaadin.ui.Field;
import com.vaadin.ui.TextArea;
import org.vaadin.viritin.fields.MTextField;

/**
 * MyCollab Ltd
 *
 * @param <B>
 * @since 1.0
 */
public class AccountEditFormFieldFactory<B extends Account> extends AbstractBeanFieldGroupEditFieldFactory<B> {
    private static final long serialVersionUID = 1L;

    public AccountEditFormFieldFactory(GenericBeanForm<B> form) {
        super(form);
    }

    @Override
    protected Field<?> onCreateField(Object propertyId) {
        if ("type".equals(propertyId)) {
            return new AccountTypeListSelect();
        } else if ("industry".equals(propertyId)) {
            return new IndustryListSelect();
        } else if ("assignuser".equals(propertyId)) {
            ActiveUserComboBox userBox = new ActiveUserComboBox();
            userBox.select(attachForm.getBean().getAssignuser());
            return userBox;
        } else if ("description".equals(propertyId)) {
            TextArea textArea = new TextArea("", "");
            textArea.setNullRepresentation("");
            return textArea;
        } else if ("billingcountry".equals(propertyId) || "shippingcountry".equals(propertyId)) {
            return new CountryListSelect();
        } else if (propertyId.equals("accountname")) {
            MTextField tf = new MTextField();
            if (isValidateForm) {
                tf.withNullRepresentation("").withRequired(true)
                .withRequiredError(UserUIContext.getMessage(ErrorI18nEnum.FIELD_MUST_NOT_NULL,
                        UserUIContext.getMessage(AccountI18nEnum.FORM_ACCOUNT_NAME)));
            }

            return tf;
        }

        return null;
    }

}

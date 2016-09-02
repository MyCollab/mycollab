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
package com.mycollab.module.crm.view.account;

import com.mycollab.common.i18n.ErrorI18nEnum;
import com.mycollab.module.crm.domain.Account;
import com.mycollab.module.crm.i18n.AccountI18nEnum;
import com.mycollab.module.crm.ui.components.IndustryComboBox;
import com.mycollab.module.user.ui.components.ActiveUserComboBox;
import com.mycollab.vaadin.AppContext;
import com.mycollab.vaadin.ui.AbstractBeanFieldGroupEditFieldFactory;
import com.mycollab.vaadin.ui.GenericBeanForm;
import com.mycollab.vaadin.web.ui.CountryComboBox;
import com.vaadin.ui.Field;
import com.vaadin.ui.RichTextArea;
import org.vaadin.viritin.fields.MTextField;

/**
 * @param <B>
 * @author MyCollab Ltd.
 * @since 3.0
 */
public class AccountEditFormFieldFactory<B extends Account> extends AbstractBeanFieldGroupEditFieldFactory<B> {
    private static final long serialVersionUID = 1L;

    public AccountEditFormFieldFactory(GenericBeanForm<B> form) {
        super(form);
    }

    public AccountEditFormFieldFactory(GenericBeanForm<B> form, boolean isValidateForm) {
        super(form, isValidateForm);
    }

    @Override
    protected Field<?> onCreateField(Object propertyId) {
        if (Account.Field.type.equalTo(propertyId)) {
            return new AccountTypeComboBox();
        } else if (Account.Field.industry.equalTo(propertyId)) {
            return new IndustryComboBox();
        } else if (Account.Field.assignuser.equalTo(propertyId)) {
            ActiveUserComboBox userBox = new ActiveUserComboBox();
            userBox.select(attachForm.getBean().getAssignuser());
            return userBox;
        } else if (Account.Field.description.equalTo(propertyId)) {
            return new RichTextArea();
        } else if (Account.Field.billingcountry.equalTo(propertyId) || Account.Field.shippingcountry.equalTo(propertyId)) {
            return new CountryComboBox();
        } else if (Account.Field.accountname.equalTo(propertyId)) {
            MTextField tf = new MTextField();
            if (isValidateForm) {
                tf.withNullRepresentation("").withRequired(true)
                        .withRequiredError(AppContext.getMessage(ErrorI18nEnum.ERROR_USER_IS_NOT_EXISTED,
                                AppContext.getMessage(AccountI18nEnum.FORM_ACCOUNT_NAME)));
            }

            return tf;
        }

        return null;
    }

}

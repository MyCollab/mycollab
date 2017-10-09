package com.mycollab.module.crm.view.account;

import com.mycollab.common.i18n.ErrorI18nEnum;
import com.mycollab.module.crm.domain.Account;
import com.mycollab.module.crm.i18n.AccountI18nEnum;
import com.mycollab.module.crm.ui.components.IndustryComboBox;
import com.mycollab.module.user.ui.components.ActiveUserComboBox;
import com.mycollab.vaadin.UserUIContext;
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
                        .withRequiredError(UserUIContext.getMessage(ErrorI18nEnum.ERROR_USER_IS_NOT_EXISTED,
                                UserUIContext.getMessage(AccountI18nEnum.FORM_ACCOUNT_NAME)));
            }

            return tf;
        }

        return null;
    }

}

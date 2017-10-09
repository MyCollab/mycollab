package com.mycollab.module.crm.view.account;

import com.mycollab.module.crm.domain.Account;
import com.mycollab.module.crm.domain.SimpleAccount;
import com.mycollab.module.crm.i18n.OptionI18nEnum.AccountIndustry;
import com.mycollab.module.crm.i18n.OptionI18nEnum.AccountType;
import com.mycollab.vaadin.ui.AbstractBeanFieldGroupViewFieldFactory;
import com.mycollab.vaadin.ui.GenericBeanForm;
import com.mycollab.vaadin.ui.field.*;
import com.mycollab.vaadin.web.ui.field.UserLinkViewField;
import com.vaadin.ui.Field;

/**
 * @author MyCollab Ltd.
 * @since 3.0
 */
public class AccountReadFormFieldFactory extends AbstractBeanFieldGroupViewFieldFactory<SimpleAccount> {
    private static final long serialVersionUID = 1L;

    public AccountReadFormFieldFactory(GenericBeanForm<SimpleAccount> form) {
        super(form);
    }

    @Override
    protected Field<?> onCreateField(Object propertyId) {
        SimpleAccount account = attachForm.getBean();

        if (propertyId.equals("email")) {
            return new EmailViewField(account.getEmail());
        } else if (propertyId.equals("assignuser")) {
            return new UserLinkViewField(account.getAssignuser(), account.getAssignUserAvatarId(), account.getAssignUserFullName());
        } else if (propertyId.equals("website")) {
            return new UrlLinkViewField(account.getWebsite());
        } else if (propertyId.equals("type")) {
            return new I18nFormViewField(account.getType(), AccountType.class);
        } else if (Account.Field.industry.equalTo(propertyId)) {
            return new I18nFormViewField(account.getIndustry(), AccountIndustry.class);
        } else if (propertyId.equals("description")) {
            return new RichTextViewField(account.getDescription());
        } else if (Account.Field.billingcountry.equalTo(propertyId)) {
            return new CountryViewField(account.getBillingcountry());
        } else if (Account.Field.shippingcountry.equalTo(propertyId)) {
            return new CountryViewField(account.getShippingcountry());
        }

        return null;
    }

}

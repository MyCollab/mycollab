package com.mycollab.mobile.module.crm.view.account;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.form.view.builder.*;
import com.mycollab.form.view.builder.type.DynaForm;
import com.mycollab.form.view.builder.type.DynaSection;
import com.mycollab.form.view.builder.type.DynaSection.LayoutType;
import com.mycollab.module.crm.i18n.AccountI18nEnum;

/**
 * @author MyCollab Ltd.
 * @since 3.0
 */
public class AccountDefaultDynaFormLayoutFactory {
    public static final DynaForm defaultForm;

    static {
        defaultForm = new DynaForm();

        // Build block account information
        DynaSection accountSection = new DynaSectionBuilder().layoutType(LayoutType.TWO_COLUMN).orderIndex(0)
                .header(AccountI18nEnum.SECTION_ACCOUNT_INFORMATION).build();

        accountSection.fields(new TextDynaFieldBuilder().fieldName("accountname")
                .displayName(AccountI18nEnum.FORM_ACCOUNT_NAME)
                .customField(false).fieldIndex(0).mandatory(true)
                .required(true).build());
        accountSection.fields(new TextDynaFieldBuilder().fieldName("phoneoffice")
                .displayName(AccountI18nEnum.FORM_OFFICE_PHONE)
                .customField(false).fieldIndex(1).build());

        accountSection.fields(new TextDynaFieldBuilder().fieldName("website").fieldIndex(2)
                .displayName(AccountI18nEnum.FORM_WEBSITE)
                .customField(false).build());

        accountSection.fields(new PhoneDynaFieldBuilder().fieldName("fax").fieldIndex(3)
                .displayName(AccountI18nEnum.FORM_FAX)
                .customField(false).build());

        accountSection.fields(new IntDynaFieldBuilder().fieldName("numemployees").fieldIndex(4)
                .displayName(AccountI18nEnum.FORM_EMPLOYEES)
                .customField(false).build());

        accountSection.fields(new PhoneDynaFieldBuilder().fieldName("alternatephone").fieldIndex(5)
                .displayName(AccountI18nEnum.FORM_OTHER_PHONE)
                .customField(false).build());

        accountSection.fields(new PickListDynaFieldBuilder<String>().fieldName("industry").fieldIndex(6)
                .displayName(AccountI18nEnum.FORM_INDUSTRY)
                .customField(false).build());

        accountSection.fields(new EmailDynaFieldBuilder().fieldName("email").fieldIndex(7)
                .displayName(GenericI18Enum.FORM_EMAIL)
                .customField(false).build());

        accountSection.fields(new PickListDynaFieldBuilder<String>().fieldName("type").fieldIndex(8)
                .displayName(GenericI18Enum.FORM_TYPE)
                .customField(false).build());

        accountSection.fields(new TextDynaFieldBuilder().fieldName("ownership").fieldIndex(9)
                .displayName(AccountI18nEnum.FORM_OWNERSHIP)
                .customField(false).build());

        accountSection.fields(new TextDynaFieldBuilder().fieldName("assignuser").fieldIndex(10)
                .displayName(GenericI18Enum.FORM_ASSIGNEE)
                .customField(false).build());

        accountSection.fields(new TextDynaFieldBuilder().fieldName("annualrevenue").fieldIndex(11)
                .displayName(AccountI18nEnum.FORM_ANNUAL_REVENUE)
                .customField(false).build());

        defaultForm.sections(accountSection);

        // build block address
        DynaSection addressSection = new DynaSectionBuilder().layoutType(LayoutType.TWO_COLUMN)
                .header(AccountI18nEnum.SECTION_ADDRESS_INFORMATION)
                .orderIndex(1).build();
        addressSection.fields(new TextDynaFieldBuilder().fieldIndex(0).fieldName("billingaddress")
                .displayName(AccountI18nEnum.FORM_BILLING_ADDRESS)
                .customField(false).build());
        addressSection.fields(new TextDynaFieldBuilder().fieldIndex(1).fieldName("shippingaddress")
                .displayName(AccountI18nEnum.FORM_SHIPPING_ADDRESS)
                .customField(false).build());
        addressSection.fields(new TextDynaFieldBuilder().fieldIndex(2).fieldName("city")
                .displayName(AccountI18nEnum.FORM_BILLING_CITY)
                .customField(false).build());

        addressSection.fields(new TextDynaFieldBuilder().fieldIndex(3).fieldName("shippingcity")
                .displayName(AccountI18nEnum.FORM_SHIPPING_CITY)
                .customField(false).build());

        addressSection.fields(new TextDynaFieldBuilder().fieldIndex(4).fieldName("state")
                .displayName(AccountI18nEnum.FORM_BILLING_STATE)
                .customField(false).build());

        addressSection.fields(new TextDynaFieldBuilder().fieldIndex(5).fieldName("shippingstate")
                .displayName(AccountI18nEnum.FORM_SHIPPING_STATE)
                .customField(false).build());

        addressSection.fields(new TextDynaFieldBuilder().fieldIndex(6).fieldName("postalcode")
                .displayName(AccountI18nEnum.FORM_BILLING_POSTAL_CODE)
                .customField(false).build());

        addressSection.fields(new TextDynaFieldBuilder().fieldIndex(7).fieldName("shippingpostalcode")
                .displayName(AccountI18nEnum.FORM_SHIPPING_POSTAL_CODE)
                .customField(false).build());

        addressSection.fields(new TextDynaFieldBuilder().fieldIndex(8).fieldName("billingcountry")
                .displayName(AccountI18nEnum.FORM_BILLING_COUNTRY)
                .customField(false).build());

        addressSection.fields(new TextDynaFieldBuilder().fieldIndex(9).fieldName("shippingcountry")
                .displayName(AccountI18nEnum.FORM_SHIPPING_COUNTRY)
                .customField(false).build());

        defaultForm.sections(addressSection);

        // build block description
        DynaSection descSection = new DynaSectionBuilder().layoutType(LayoutType.ONE_COLUMN)
                .header(AccountI18nEnum.SECTION_DESCRIPTION)
                .orderIndex(2).build();

        descSection.fields(new TextDynaFieldBuilder().fieldIndex(0).fieldName("description").customField(false)
                .displayName(GenericI18Enum.FORM_DESCRIPTION)
                .build());
        defaultForm.sections(descSection);

    }

    public static DynaForm getForm() {
        return defaultForm;
    }
}

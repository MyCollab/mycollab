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
package com.esofthead.mycollab.module.crm.view.account;

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.form.view.builder.*;
import com.esofthead.mycollab.form.view.builder.type.DynaForm;
import com.esofthead.mycollab.form.view.builder.type.DynaSection;
import com.esofthead.mycollab.form.view.builder.type.DynaSection.LayoutType;
import com.esofthead.mycollab.module.crm.domain.Account;
import com.esofthead.mycollab.module.crm.i18n.AccountI18nEnum;
import com.esofthead.mycollab.vaadin.AppContext;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class AccountDefaultDynaFormLayoutFactory {
    private static final DynaForm defaultForm;

    static {
        defaultForm = new DynaForm();

        // Build block account information
        DynaSection accountSection = new DynaSectionBuilder().layoutType(LayoutType.TWO_COLUMN).orderIndex(0)
                .header(AppContext.getMessage(AccountI18nEnum.SECTION_ACCOUNT_INFORMATION))
                .build();
        accountSection.addField(new TextDynaFieldBuilder().fieldName(Account.Field.accountname)
                .displayName(AppContext.getMessage(AccountI18nEnum.FORM_ACCOUNT_NAME)).fieldIndex(0).mandatory(true)
                .required(true).build());

        accountSection.addField(new TextDynaFieldBuilder()
                .fieldName(Account.Field.phoneoffice)
                .displayName(AppContext.getMessage(AccountI18nEnum.FORM_OFFICE_PHONE)).fieldIndex(1).build());

        accountSection.addField(new TextDynaFieldBuilder()
                .fieldName(Account.Field.website)
                .fieldIndex(2)
                .displayName(AppContext.getMessage(AccountI18nEnum.FORM_WEBSITE)).build());

        accountSection.addField(new PhoneDynaFieldBuilder()
                .fieldName(Account.Field.fax).fieldIndex(3)
                .displayName(AppContext.getMessage(AccountI18nEnum.FORM_FAX)).build());

        accountSection.addField(new IntDynaFieldBuilder()
                .fieldName(Account.Field.numemployees)
                .fieldIndex(4)
                .displayName(AppContext.getMessage(AccountI18nEnum.FORM_EMPLOYEES)).build());

        accountSection.addField(new PhoneDynaFieldBuilder()
                .fieldName(Account.Field.alternatephone).fieldIndex(5)
                .displayName(AppContext.getMessage(AccountI18nEnum.FORM_OTHER_PHONE)).build());

        accountSection.addField(new PickListDynaFieldBuilder<String>()
                .fieldName(Account.Field.industry).fieldIndex(6)
                .displayName(AppContext.getMessage(AccountI18nEnum.FORM_INDUSTRY)).build());

        accountSection.addField(new EmailDynaFieldBuilder()
                .fieldName(Account.Field.email).fieldIndex(7)
                .displayName(AppContext.getMessage(AccountI18nEnum.FORM_EMAIL)).build());

        accountSection.addField(new PickListDynaFieldBuilder<String>()
                .fieldName(Account.Field.type).fieldIndex(8)
                .displayName(AppContext.getMessage(AccountI18nEnum.FORM_TYPE)).build());

        accountSection.addField(new TextDynaFieldBuilder()
                .fieldName(Account.Field.ownership)
                .fieldIndex(9)
                .displayName(AppContext.getMessage(AccountI18nEnum.FORM_OWNERSHIP)).build());

        accountSection.addField(new TextDynaFieldBuilder()
                .fieldName(Account.Field.assignuser)
                .fieldIndex(10)
                .displayName(AppContext.getMessage(GenericI18Enum.FORM_ASSIGNEE)).build());

        accountSection.addField(new TextDynaFieldBuilder()
                .fieldName(Account.Field.annualrevenue)
                .fieldIndex(11)
                .displayName(AppContext.getMessage(AccountI18nEnum.FORM_ANNUAL_REVENUE)).build());

        defaultForm.addSection(accountSection);

        // build block address
        DynaSection addressSection = new DynaSectionBuilder()
                .layoutType(LayoutType.TWO_COLUMN)
                .header(AppContext.getMessage(AccountI18nEnum.SECTION_ADDRESS_INFORMATION))
                .orderIndex(1).build();
        addressSection.addField(new TextDynaFieldBuilder()
                .fieldIndex(0)
                .fieldName(Account.Field.billingaddress)
                .displayName(AppContext.getMessage(AccountI18nEnum.FORM_BILLING_ADDRESS))
                .customField(false).build());
        addressSection.addField(new TextDynaFieldBuilder().fieldIndex(1)
                .fieldName(Account.Field.shippingaddress)
                .displayName(AppContext.getMessage(AccountI18nEnum.FORM_SHIPPING_ADDRESS))
                .customField(false).build());
        addressSection.addField(new TextDynaFieldBuilder()
                .fieldIndex(2)
                .fieldName(Account.Field.city)
                .displayName(AppContext.getMessage(AccountI18nEnum.FORM_BILLING_CITY))
                .customField(false).build());

        addressSection.addField(new TextDynaFieldBuilder()
                .fieldIndex(3)
                .fieldName(Account.Field.shippingcity)
                .displayName(AppContext.getMessage(AccountI18nEnum.FORM_SHIPPING_CITY))
                .customField(false).build());

        addressSection.addField(new TextDynaFieldBuilder().fieldIndex(4)
                .fieldName(Account.Field.state)
                .displayName(AppContext.getMessage(AccountI18nEnum.FORM_BILLING_STATE))
                .customField(false).build());

        addressSection.addField(new TextDynaFieldBuilder()
                .fieldIndex(5)
                .fieldName(Account.Field.shippingstate)
                .displayName(AppContext.getMessage(AccountI18nEnum.FORM_SHIPPING_STATE))
                .customField(false).build());

        addressSection.addField(new TextDynaFieldBuilder()
                .fieldIndex(6)
                .fieldName(Account.Field.postalcode)
                .displayName(AppContext.getMessage(AccountI18nEnum.FORM_BILLING_POSTAL_CODE))
                .customField(false).build());

        addressSection.addField(new TextDynaFieldBuilder()
                .fieldIndex(7)
                .fieldName(Account.Field.shippingpostalcode)
                .displayName(AppContext.getMessage(AccountI18nEnum.FORM_SHIPPING_POSTAL_CODE))
                .customField(false).build());

        addressSection.addField(new TextDynaFieldBuilder()
                .fieldIndex(8)
                .fieldName(Account.Field.billingcountry)
                .displayName(AppContext.getMessage(AccountI18nEnum.FORM_BILLING_COUNTRY))
                .customField(false).build());

        addressSection.addField(new TextDynaFieldBuilder()
                .fieldIndex(9)
                .fieldName(Account.Field.shippingcountry)
                .displayName(AppContext.getMessage(AccountI18nEnum.FORM_SHIPPING_COUNTRY))
                .customField(false).build());

        defaultForm.addSection(addressSection);

        // build block description
        DynaSection descSection = new DynaSectionBuilder()
                .layoutType(LayoutType.ONE_COLUMN)
                .header(AppContext.getMessage(AccountI18nEnum.SECTION_DESCRIPTION))
                .orderIndex(2).build();

        descSection.addField(new TextDynaFieldBuilder()
                .fieldIndex(0)
                .fieldName(Account.Field.description)
                .customField(false)
                .displayName(AppContext.getMessage(GenericI18Enum.FORM_DESCRIPTION))
                .build());
        defaultForm.addSection(descSection);
    }

    public static DynaForm getForm() {
        return defaultForm;
    }
}

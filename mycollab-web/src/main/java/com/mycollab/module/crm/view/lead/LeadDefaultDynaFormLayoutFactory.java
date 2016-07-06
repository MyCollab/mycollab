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
package com.mycollab.module.crm.view.lead;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.form.view.builder.*;
import com.mycollab.form.view.builder.type.DynaForm;
import com.mycollab.form.view.builder.type.DynaSection;
import com.mycollab.form.view.builder.type.DynaSection.LayoutType;
import com.mycollab.module.crm.i18n.LeadI18nEnum;
import com.mycollab.vaadin.AppContext;

/**
 * @author MyCollab Ltd.
 * @since 2.0
 */
public class LeadDefaultDynaFormLayoutFactory {
    public static final DynaForm defaultForm;

    static {
        defaultForm = new DynaForm();

        DynaSection infoSection = new DynaSectionBuilder().layoutType(LayoutType.TWO_COLUMN).orderIndex(0)
                .header(AppContext.getMessage(LeadI18nEnum.SECTION_LEAD_INFORMATION))
                .build();

        infoSection.fields(new TextDynaFieldBuilder().fieldName("firstname")
                .displayName(AppContext.getMessage(LeadI18nEnum.FORM_FIRSTNAME))
                .fieldIndex(0).build());

        infoSection.fields(new EmailDynaFieldBuilder().fieldName("email")
                .displayName(AppContext.getMessage(LeadI18nEnum.FORM_EMAIL))
                .fieldIndex(1).build());

        infoSection.fields(new TextDynaFieldBuilder().fieldName("lastname")
                .displayName(AppContext.getMessage(LeadI18nEnum.FORM_LASTNAME))
                .fieldIndex(2).build());

        infoSection.fields(new PhoneDynaFieldBuilder().fieldName("officephone")
                .displayName(AppContext.getMessage(LeadI18nEnum.FORM_OFFICE_PHONE))
                .fieldIndex(3).build());

        infoSection.fields(new TextDynaFieldBuilder().fieldName("title")
                .displayName(AppContext.getMessage(LeadI18nEnum.FORM_TITLE))
                .fieldIndex(4).build());

        infoSection.fields(new PhoneDynaFieldBuilder().fieldName("mobile")
                .displayName(AppContext.getMessage(LeadI18nEnum.FORM_MOBILE))
                .fieldIndex(5).build());

        infoSection.fields(new TextDynaFieldBuilder().fieldName("department")
                .displayName(AppContext.getMessage(LeadI18nEnum.FORM_DEPARTMENT))
                .fieldIndex(6).build());

        infoSection.fields(new PhoneDynaFieldBuilder().fieldName("otherphone")
                .displayName(AppContext.getMessage(LeadI18nEnum.FORM_OTHER_PHONE))
                .fieldIndex(7).build());

        infoSection.fields(new TextDynaFieldBuilder().fieldName("accountname")
                .displayName(AppContext.getMessage(LeadI18nEnum.FORM_ACCOUNT_NAME))
                .fieldIndex(8).build());

        infoSection.fields(new PhoneDynaFieldBuilder().fieldName("fax")
                .displayName(AppContext.getMessage(LeadI18nEnum.FORM_FAX))
                .fieldIndex(9).build());

        infoSection.fields(new TextDynaFieldBuilder().fieldName("source")
                .displayName(AppContext.getMessage(LeadI18nEnum.FORM_LEAD_SOURCE))
                .fieldIndex(10).build());

        infoSection.fields(new UrlDynaFieldBuilder().fieldName("website")
                .displayName(AppContext.getMessage(LeadI18nEnum.FORM_WEBSITE))
                .fieldIndex(11).build());

        infoSection.fields(new TextDynaFieldBuilder().fieldName("industry")
                .displayName(AppContext.getMessage(LeadI18nEnum.FORM_INDUSTRY))
                .fieldIndex(12).build());

        infoSection.fields(new TextDynaFieldBuilder().fieldName("status")
                .displayName(AppContext.getMessage(GenericI18Enum.FORM_STATUS))
                .fieldIndex(13).build());

        infoSection.fields(new IntDynaFieldBuilder().fieldName("noemployees")
                .displayName(AppContext.getMessage(LeadI18nEnum.FORM_NO_EMPLOYEES))
                .fieldIndex(14).build());

        infoSection.fields(new TextDynaFieldBuilder().fieldName("assignuser")
                .displayName(AppContext.getMessage(GenericI18Enum.FORM_ASSIGNEE))
                .fieldIndex(15).build());

        defaultForm.sections(infoSection);

        DynaSection addressSection = new DynaSectionBuilder().layoutType(LayoutType.TWO_COLUMN).orderIndex(1)
                .header(AppContext.getMessage(LeadI18nEnum.SECTION_ADDRESS))
                .build();

        addressSection.fields(new TextDynaFieldBuilder().fieldName("primaddress")
                .displayName(AppContext.getMessage(LeadI18nEnum.FORM_PRIMARY_ADDRESS))
                .fieldIndex(0).build());

        addressSection.fields(new TextDynaFieldBuilder().fieldName("otheraddress")
                .displayName(AppContext.getMessage(LeadI18nEnum.FORM_OTHER_ADDRESS))
                .fieldIndex(1).build());

        addressSection.fields(new TextDynaFieldBuilder().fieldName("primcity")
                .displayName(AppContext.getMessage(LeadI18nEnum.FORM_PRIMARY_CITY))
                .fieldIndex(2).build());

        addressSection.fields(new TextDynaFieldBuilder().fieldName("othercity")
                .displayName(AppContext.getMessage(LeadI18nEnum.FORM_OTHER_CITY))
                .fieldIndex(3).build());

        addressSection.fields(new TextDynaFieldBuilder().fieldName("primstate")
                .displayName(AppContext.getMessage(LeadI18nEnum.FORM_PRIMARY_STATE))
                .fieldIndex(4).build());

        addressSection.fields(new TextDynaFieldBuilder().fieldName("otherstate")
                .displayName(AppContext.getMessage(LeadI18nEnum.FORM_OTHER_STATE))
                .fieldIndex(5).build());

        addressSection.fields(new TextDynaFieldBuilder().fieldName("primpostalcode")
                .displayName(AppContext.getMessage(LeadI18nEnum.FORM_PRIMARY_POSTAL_CODE))
                .fieldIndex(6).build());

        addressSection.fields(new TextDynaFieldBuilder().fieldName("otherpostalcode")
                .displayName(AppContext.getMessage(LeadI18nEnum.FORM_OTHER_POSTAL_CODE))
                .fieldIndex(7).build());

        addressSection.fields(new TextDynaFieldBuilder().fieldName("primcountry")
                .displayName(AppContext.getMessage(LeadI18nEnum.FORM_PRIMARY_COUNTRY))
                .fieldIndex(8).build());

        addressSection.fields(new TextDynaFieldBuilder().fieldName("othercountry")
                .displayName(AppContext.getMessage(LeadI18nEnum.FORM_OTHER_COUNTRY))
                .fieldIndex(9).build());

        defaultForm.sections(addressSection);

        DynaSection descSection = new DynaSectionBuilder().layoutType(LayoutType.TWO_COLUMN).orderIndex(2)
                .header(AppContext.getMessage(LeadI18nEnum.SECTION_DESCRIPTION))
                .build();

        descSection.fields(new TextAreaDynaFieldBuilder().fieldName("description")
                .displayName(AppContext.getMessage(GenericI18Enum.FORM_DESCRIPTION))
                .fieldIndex(0).build());

        defaultForm.sections(descSection);
    }

    public static DynaForm getForm() {
        return defaultForm;
    }
}

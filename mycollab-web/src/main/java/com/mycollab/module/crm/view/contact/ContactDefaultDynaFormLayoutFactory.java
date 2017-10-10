/**
 * mycollab-web - Parent pom providing dependency and plugin management for applications
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
package com.mycollab.module.crm.view.contact;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.form.view.builder.*;
import com.mycollab.form.view.builder.type.DynaForm;
import com.mycollab.form.view.builder.type.DynaSection;
import com.mycollab.form.view.builder.type.DynaSection.LayoutType;
import com.mycollab.module.crm.domain.Contact;
import com.mycollab.module.crm.i18n.ContactI18nEnum;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class ContactDefaultDynaFormLayoutFactory {
    private static final DynaForm defaultForm;

    static {
        defaultForm = new DynaForm();

        DynaSection contactSection = new DynaSectionBuilder().layoutType(LayoutType.TWO_COLUMN).orderIndex(0)
                .header(ContactI18nEnum.SECTION_INFORMATION)
                .build();

        contactSection.fields(new TextDynaFieldBuilder().fieldName(Contact.Field.firstname)
                .displayName(GenericI18Enum.FORM_FIRSTNAME)
                .fieldIndex(0).build());

        contactSection.fields(new PhoneDynaFieldBuilder().fieldName(Contact.Field.officephone)
                .displayName(ContactI18nEnum.FORM_OFFICE_PHONE)
                .fieldIndex(1).build());

        contactSection.fields(new TextDynaFieldBuilder().fieldName(Contact.Field.lastname)
                .displayName(GenericI18Enum.FORM_LASTNAME)
                .fieldIndex(2).mandatory(true).build());

        contactSection.fields(new PhoneDynaFieldBuilder().fieldName(Contact.Field.mobile)
                .displayName(ContactI18nEnum.FORM_MOBILE)
                .fieldIndex(3).build());

        contactSection.fields(new TextDynaFieldBuilder().fieldName(Contact.Field.accountid)
                .displayName(ContactI18nEnum.FORM_ACCOUNTS)
                .fieldIndex(4).build());

        contactSection.fields(new PhoneDynaFieldBuilder().fieldName(Contact.Field.homephone)
                .displayName(ContactI18nEnum.FORM_HOME_PHONE)
                .fieldIndex(5).build());

        contactSection.fields(new TextDynaFieldBuilder().fieldName(Contact.Field.title)
                .displayName(ContactI18nEnum.FORM_TITLE)
                .fieldIndex(6).build());

        contactSection.fields(new PhoneDynaFieldBuilder().fieldName(Contact.Field.otherphone)
                .displayName(ContactI18nEnum.FORM_OTHER_PHONE)
                .fieldIndex(7).build());

        contactSection.fields(new TextDynaFieldBuilder().fieldName(Contact.Field.department)
                .displayName(ContactI18nEnum.FORM_DEPARTMENT)
                .fieldIndex(8).build());

        contactSection.fields(new PhoneDynaFieldBuilder().fieldName(Contact.Field.fax)
                .displayName(ContactI18nEnum.FORM_FAX)
                .fieldIndex(9).build());

        contactSection.fields(new EmailDynaFieldBuilder().fieldName(Contact.Field.email)
                .displayName(GenericI18Enum.FORM_EMAIL)
                .fieldIndex(10).build());

        contactSection.fields(new TextDynaFieldBuilder().fieldName(Contact.Field.birthday)
                .displayName(ContactI18nEnum.FORM_BIRTHDAY)
                .fieldIndex(11).build());

        contactSection.fields(new TextDynaFieldBuilder().fieldName(Contact.Field.assistant)
                .displayName(ContactI18nEnum.FORM_ASSISTANT)
                .fieldIndex(12).build());

        contactSection.fields(new BooleanDynaFieldBuilder().fieldName(Contact.Field.iscallable)
                .displayName(ContactI18nEnum.FORM_IS_CALLABLE)
                .fieldIndex(13).build());

        contactSection.fields(new TextDynaFieldBuilder().fieldName(Contact.Field.assistantphone)
                .displayName(ContactI18nEnum.FORM_ASSISTANT_PHONE)
                .fieldIndex(14).build());

        contactSection.fields(new TextDynaFieldBuilder().fieldName(Contact.Field.assignuser)
                .displayName(GenericI18Enum.FORM_ASSIGNEE)
                .fieldIndex(15).build());

        contactSection.fields(new TextDynaFieldBuilder().fieldName(Contact.Field.leadsource)
                .displayName(ContactI18nEnum.FORM_LEAD_SOURCE)
                .fieldIndex(16).build());

        defaultForm.sections(contactSection);

        DynaSection addressSection = new DynaSectionBuilder().layoutType(LayoutType.TWO_COLUMN).orderIndex(1)
                .header(ContactI18nEnum.SECTION_ADDRESS)
                .build();

        addressSection.fields(new TextDynaFieldBuilder().fieldName(Contact.Field.primaddress)
                .displayName(ContactI18nEnum.FORM_PRIMARY_ADDRESS)
                .fieldIndex(0).build());

        addressSection.fields(new TextDynaFieldBuilder().fieldName(Contact.Field.otheraddress)
                .displayName(ContactI18nEnum.FORM_OTHER_ADDRESS)
                .fieldIndex(1).build());

        addressSection.fields(new TextDynaFieldBuilder().fieldName(Contact.Field.primcity)
                .displayName(ContactI18nEnum.FORM_PRIMARY_CITY)
                .fieldIndex(2).build());

        addressSection.fields(new TextDynaFieldBuilder().fieldName(Contact.Field.othercity)
                .displayName(ContactI18nEnum.FORM_OTHER_CITY)
                .fieldIndex(3).build());

        addressSection.fields(new TextDynaFieldBuilder().fieldName(Contact.Field.primstate)
                .displayName(ContactI18nEnum.FORM_PRIMARY_STATE)
                .fieldIndex(4).build());

        addressSection.fields(new TextDynaFieldBuilder().fieldName(Contact.Field.otherstate)
                .displayName(ContactI18nEnum.FORM_OTHER_STATE)
                .fieldIndex(5).build());

        addressSection.fields(new TextDynaFieldBuilder().fieldName(Contact.Field.primpostalcode)
                .displayName(ContactI18nEnum.FORM_PRIMARY_POSTAL_CODE)
                .fieldIndex(6).build());

        addressSection.fields(new TextDynaFieldBuilder().fieldName(Contact.Field.otherpostalcode)
                .displayName(ContactI18nEnum.FORM_OTHER_POSTAL_CODE)
                .fieldIndex(7).build());

        addressSection.fields(new TextDynaFieldBuilder().fieldName(Contact.Field.primcountry)
                .displayName(ContactI18nEnum.FORM_PRIMARY_COUNTRY)
                .fieldIndex(8).build());

        addressSection.fields(new TextDynaFieldBuilder().fieldName(Contact.Field.othercountry)
                .displayName(ContactI18nEnum.FORM_OTHER_COUNTRY)
                .fieldIndex(9).build());

        defaultForm.sections(addressSection);

        DynaSection descSection = new DynaSectionBuilder().layoutType(LayoutType.ONE_COLUMN).orderIndex(2)
                .header(ContactI18nEnum.SECTION_DESCRIPTION)
                .build();

        descSection.fields(new TextDynaFieldBuilder().fieldName(Contact.Field.description)
                .displayName(GenericI18Enum.FORM_DESCRIPTION)
                .fieldIndex(0).build());

        defaultForm.sections(descSection);
    }

    public static DynaForm getForm() {
        return defaultForm;
    }
}

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
package com.mycollab.mobile.module.crm.view.cases;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.form.view.builder.*;
import com.mycollab.form.view.builder.type.DynaForm;
import com.mycollab.form.view.builder.type.DynaSection;
import com.mycollab.form.view.builder.type.DynaSection.LayoutType;
import com.mycollab.module.crm.i18n.CaseI18nEnum;

/**
 * @author MyCollab Ltd.
 * @since 4.1
 */
public class CaseDefaultFormLayoutFactory {
    public static final DynaForm defaultForm;

    static {
        defaultForm = new DynaForm();

        DynaSection infoSection = new DynaSectionBuilder().layoutType(LayoutType.TWO_COLUMN).orderIndex(0)
                .header(CaseI18nEnum.SECTION_CASE_INFORMATION)
                .build();

        infoSection.fields(new TextDynaFieldBuilder().fieldName("priority")
                .displayName(CaseI18nEnum.FORM_PRIORITY)
                .fieldIndex(0).build());

        infoSection.fields(new TextDynaFieldBuilder().fieldName("type")
                .displayName(GenericI18Enum.FORM_TYPE)
                .fieldIndex(1).build());

        infoSection.fields(new TextDynaFieldBuilder().fieldName("status")
                .displayName(GenericI18Enum.FORM_STATUS)
                .fieldIndex(2).build());

        infoSection.fields(new TextDynaFieldBuilder().fieldName("reason")
                .displayName(CaseI18nEnum.FORM_REASON)
                .fieldIndex(3).build());

        infoSection.fields(new TextDynaFieldBuilder().fieldName("accountid")
                .displayName(CaseI18nEnum.FORM_ACCOUNT)
                .fieldIndex(4).mandatory(true).build());

        infoSection.fields(new TextDynaFieldBuilder().fieldName("subject")
                .displayName(CaseI18nEnum.FORM_SUBJECT)
                .fieldIndex(5).mandatory(true).build());

        infoSection.fields(new PhoneDynaFieldBuilder().fieldName("phonenumber")
                .displayName(GenericI18Enum.FORM_PHONE)
                .fieldIndex(6).build());

        infoSection.fields(new EmailDynaFieldBuilder().fieldName("email")
                .displayName(GenericI18Enum.FORM_EMAIL)
                .fieldIndex(7).build());

        infoSection.fields(new TextDynaFieldBuilder().fieldName("origin")
                .displayName(CaseI18nEnum.FORM_ORIGIN)
                .fieldIndex(8).build());

        infoSection.fields(new TextDynaFieldBuilder().fieldName("assignuser")
                .displayName(GenericI18Enum.FORM_ASSIGNEE)
                .fieldIndex(9).build());

        defaultForm.sections(infoSection);

        DynaSection descSection = new DynaSectionBuilder().layoutType(LayoutType.ONE_COLUMN).orderIndex(1)
                .header(CaseI18nEnum.SECTION_DESCRIPTION)
                .build();

        descSection.fields(new TextAreaDynaFieldBuilder().fieldName("description")
                .displayName(GenericI18Enum.FORM_DESCRIPTION)
                .fieldIndex(0).build());
        descSection.fields(new TextAreaDynaFieldBuilder().fieldName("resolution")
                .displayName(CaseI18nEnum.FORM_RESOLUTION)
                .fieldIndex(1).build());

        defaultForm.sections(descSection);
    }

    public static DynaForm getForm() {
        return defaultForm;
    }
}

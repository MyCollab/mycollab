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
package com.mycollab.mobile.module.crm.view.activity;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.form.view.builder.DateTimeDynaFieldBuilder;
import com.mycollab.form.view.builder.DynaSectionBuilder;
import com.mycollab.form.view.builder.TextAreaDynaFieldBuilder;
import com.mycollab.form.view.builder.TextDynaFieldBuilder;
import com.mycollab.form.view.builder.type.DynaForm;
import com.mycollab.form.view.builder.type.DynaSection;
import com.mycollab.form.view.builder.type.DynaSection.LayoutType;
import com.mycollab.module.crm.i18n.CallI18nEnum;

/**
 * @author MyCollab Ltd.
 * @since 4.1
 */
public class CallDefaultFormLayoutFactory {
    public static final DynaForm defaultForm;

    static {
        defaultForm = new DynaForm();

        DynaSection callSection = new DynaSectionBuilder()
                .layoutType(LayoutType.TWO_COLUMN).orderIndex(0)
                .header(CallI18nEnum.SECTION_INFORMATION).build();

        callSection.fields(new TextDynaFieldBuilder().fieldName("subject")
                .displayName(CallI18nEnum.FORM_SUBJECT).fieldIndex(0).mandatory(true).build());

        callSection.fields(new TextDynaFieldBuilder().fieldName("status")
                .displayName(GenericI18Enum.FORM_STATUS).fieldIndex(1).build());

        callSection.fields(new DateTimeDynaFieldBuilder()
                .fieldName("startdate").displayName(CallI18nEnum.FORM_START_DATE_TIME)
                .fieldIndex(2).build());

        callSection.fields(new TextDynaFieldBuilder().fieldName("typeid")
                .displayName(CallI18nEnum.FORM_RELATED).fieldIndex(3).build());

        callSection.fields(new TextDynaFieldBuilder()
                .fieldName("durationinseconds").displayName(GenericI18Enum.FORM_DURATION)
                .fieldIndex(4).build());

        callSection.fields(new TextDynaFieldBuilder().fieldName("purpose")
                .displayName(CallI18nEnum.FORM_PURPOSE).fieldIndex(5).build());

        defaultForm.sections(callSection);

        DynaSection descSection = new DynaSectionBuilder()
                .layoutType(LayoutType.ONE_COLUMN).orderIndex(1)
                .header(GenericI18Enum.FORM_DESCRIPTION).build();
        descSection.fields(new TextAreaDynaFieldBuilder()
                .fieldName("description").displayName(GenericI18Enum.FORM_DESCRIPTION)
                .fieldIndex(0).build());
        defaultForm.sections(descSection);

        DynaSection resultSection = new DynaSectionBuilder()
                .layoutType(LayoutType.ONE_COLUMN).orderIndex(2)
                .header(CallI18nEnum.SECTION_RESULT).build();
        resultSection.fields(new TextAreaDynaFieldBuilder()
                .fieldName("result").displayName(CallI18nEnum.FORM_RESULT).fieldIndex(0)
                .build());

        defaultForm.sections(resultSection);
    }

    public static DynaForm getForm() {
        return defaultForm;
    }
}

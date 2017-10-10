/**
 * mycollab-mobile - Parent pom providing dependency and plugin management for applications
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
import com.mycollab.module.crm.i18n.MeetingI18nEnum;

/**
 * @author MyCollab Ltd.
 * @since 4.1
 */
public class MeetingDefaultFormLayoutFactory {
    private static final DynaForm defaultForm;

    static {
        defaultForm = new DynaForm();

        DynaSection meetingSection = new DynaSectionBuilder()
                .layoutType(LayoutType.TWO_COLUMN).orderIndex(0)
                .header(MeetingI18nEnum.SECTION_INFORMATION).build();

        meetingSection.fields(new TextDynaFieldBuilder().fieldName("subject")
                .displayName(MeetingI18nEnum.FORM_SUBJECT).mandatory(true).fieldIndex(0).build());

        meetingSection.fields(new TextDynaFieldBuilder().fieldName("status")
                .displayName(GenericI18Enum.FORM_STATUS).fieldIndex(1).build());

        meetingSection.fields(new DateTimeDynaFieldBuilder()
                .fieldName("startdate").displayName(MeetingI18nEnum.FORM_START_DATE_TIME)
                .fieldIndex(2).build());

        meetingSection.fields(new TextDynaFieldBuilder().fieldName("typeid")
                .displayName(CallI18nEnum.FORM_RELATED).fieldIndex(3).build());

        meetingSection.fields(new DateTimeDynaFieldBuilder()
                .fieldName("enddate").displayName(MeetingI18nEnum.FORM_END_DATE_TIME)
                .fieldIndex(4).build());

        meetingSection.fields(new TextDynaFieldBuilder()
                .fieldName("location").displayName(MeetingI18nEnum.FORM_LOCATION).fieldIndex(5)
                .build());
        // meetingSection.addField(new CheckBoxDynaFieldBuilder()
        // .fieldName("isrecurrence").displayName("Recurring Activity")
        // .fieldIndex(6).build());

        defaultForm.sections(meetingSection);

        DynaSection descSection = new DynaSectionBuilder()
                .layoutType(LayoutType.ONE_COLUMN).orderIndex(1)
                .header(GenericI18Enum.FORM_DESCRIPTION).build();
        descSection.fields(new TextAreaDynaFieldBuilder()
                .fieldName("description").displayName(GenericI18Enum.FORM_DESCRIPTION)
                .fieldIndex(0).build());

        defaultForm.sections(descSection);
    }

    public static DynaForm getForm() {
        return defaultForm;
    }
}

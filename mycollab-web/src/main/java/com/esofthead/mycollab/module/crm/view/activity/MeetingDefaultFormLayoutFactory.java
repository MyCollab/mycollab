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
package com.esofthead.mycollab.module.crm.view.activity;

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.form.view.builder.DateTimeDynaFieldBuilder;
import com.esofthead.mycollab.form.view.builder.DynaSectionBuilder;
import com.esofthead.mycollab.form.view.builder.TextAreaDynaFieldBuilder;
import com.esofthead.mycollab.form.view.builder.TextDynaFieldBuilder;
import com.esofthead.mycollab.form.view.builder.type.DynaForm;
import com.esofthead.mycollab.form.view.builder.type.DynaSection;
import com.esofthead.mycollab.form.view.builder.type.DynaSection.LayoutType;
import com.esofthead.mycollab.module.crm.i18n.MeetingI18nEnum;
import com.esofthead.mycollab.vaadin.AppContext;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class MeetingDefaultFormLayoutFactory {
    public static final DynaForm defaultForm;

    static {
        defaultForm = new DynaForm();

        DynaSection meetingSection = new DynaSectionBuilder().layoutType(LayoutType.TWO_COLUMN).orderIndex(0)
                .header("Meeting Information").build();

        meetingSection.fields(new TextDynaFieldBuilder().fieldName("subject")
                .displayName(AppContext.getMessage(MeetingI18nEnum.FORM_SUBJECT))
                .mandatory(true).fieldIndex(0).build());

        meetingSection.fields(new TextDynaFieldBuilder().fieldName("status")
                .displayName(AppContext.getMessage(GenericI18Enum.FORM_STATUS))
                .fieldIndex(1).build());

        meetingSection.fields(new DateTimeDynaFieldBuilder().fieldName("startdate")
                .displayName(AppContext.getMessage(MeetingI18nEnum.FORM_START_DATE_TIME))
                .fieldIndex(2).build());

        meetingSection.fields(new TextDynaFieldBuilder().fieldName("type")
                .displayName("Related To").fieldIndex(3).build());

        meetingSection.fields(new DateTimeDynaFieldBuilder().fieldName("enddate")
                .displayName(AppContext.getMessage(MeetingI18nEnum.FORM_END_DATE_TIME))
                .fieldIndex(4).build());

        meetingSection.fields(new TextDynaFieldBuilder().fieldName("location")
                .displayName(AppContext.getMessage(MeetingI18nEnum.FORM_LOCATION))
                .fieldIndex(5).build());
        // meetingSection.addField(new CheckBoxDynaFieldBuilder()
        // .fieldName("isrecurrence").displayName("Recurring Activity")
        // .fieldIndex(6).build());

        defaultForm.sections(meetingSection);

        DynaSection descSection = new DynaSectionBuilder()
                .layoutType(LayoutType.ONE_COLUMN).orderIndex(1)
                .header("Description").build();
        descSection.fields(new TextAreaDynaFieldBuilder()
                .fieldName("description").displayName("Description")
                .fieldIndex(0).build());

        defaultForm.sections(descSection);
    }

    public static DynaForm getForm() {
        return defaultForm;
    }
}

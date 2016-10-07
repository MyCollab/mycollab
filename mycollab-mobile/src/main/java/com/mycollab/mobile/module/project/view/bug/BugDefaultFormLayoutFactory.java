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
package com.mycollab.mobile.module.project.view.bug;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.form.view.builder.DynaSectionBuilder;
import com.mycollab.form.view.builder.TextDynaFieldBuilder;
import com.mycollab.form.view.builder.type.DynaForm;
import com.mycollab.form.view.builder.type.DynaSection;
import com.mycollab.module.project.i18n.BugI18nEnum;
import com.mycollab.module.project.i18n.MilestoneI18nEnum;
import com.mycollab.module.tracker.domain.BugWithBLOBs;

/**
 * @author MyCollab Ltd
 * @since 5.4.3
 */
public class BugDefaultFormLayoutFactory {
    public static DynaForm getForm() {
        DynaForm defaultForm = new DynaForm();
        DynaSection bugSection = new DynaSectionBuilder().layoutType(DynaSection.LayoutType.ONE_COLUMN).orderIndex(0).build();

        bugSection.fields(new TextDynaFieldBuilder().fieldName(BugWithBLOBs.Field.name.name())
                .displayName(GenericI18Enum.FORM_NAME)
                .customField(false).fieldIndex(0).mandatory(true)
                .required(true).build());

        bugSection.fields(new TextDynaFieldBuilder().fieldName(BugWithBLOBs.Field.priority.name())
                .displayName(GenericI18Enum.FORM_PRIORITY)
                .customField(false).fieldIndex(1).mandatory(false)
                .required(false).build());

        bugSection.fields(new TextDynaFieldBuilder().fieldName(BugWithBLOBs.Field.assignuser.name())
                .displayName(GenericI18Enum.FORM_ASSIGNEE)
                .customField(false).fieldIndex(2).mandatory(false)
                .required(false).build());

        bugSection.fields(new TextDynaFieldBuilder().fieldName(BugWithBLOBs.Field.severity.name())
                .displayName(BugI18nEnum.FORM_SEVERITY)
                .customField(false).fieldIndex(3).mandatory(false)
                .required(false).build());

        bugSection.fields(new TextDynaFieldBuilder().fieldName(BugWithBLOBs.Field.startdate.name())
                .displayName(GenericI18Enum.FORM_START_DATE)
                .customField(false).fieldIndex(4).mandatory(false)
                .required(false).build());

        bugSection.fields(new TextDynaFieldBuilder().fieldName(BugWithBLOBs.Field.enddate.name())
                .displayName(GenericI18Enum.FORM_END_DATE)
                .customField(false).fieldIndex(5).mandatory(false)
                .required(false).build());

        bugSection.fields(new TextDynaFieldBuilder().fieldName(BugWithBLOBs.Field.duedate.name())
                .displayName(GenericI18Enum.FORM_DUE_DATE)
                .customField(false).fieldIndex(6).mandatory(false)
                .required(false).build());

        bugSection.fields(new TextDynaFieldBuilder().fieldName(BugWithBLOBs.Field.milestoneid.name())
                .displayName(MilestoneI18nEnum.SINGLE)
                .customField(false).fieldIndex(7).mandatory(false)
                .required(false).build());

        bugSection.fields(new TextDynaFieldBuilder().fieldName(BugWithBLOBs.Field.environment.name())
                .displayName(BugI18nEnum.FORM_ENVIRONMENT)
                .customField(false).fieldIndex(8).mandatory(false)
                .required(false).build());

        bugSection.fields(new TextDynaFieldBuilder().fieldName(BugWithBLOBs.Field.description.name())
                .displayName(GenericI18Enum.FORM_DESCRIPTION)
                .customField(false).fieldIndex(9).mandatory(false)
                .required(false).build());

        defaultForm.sections(bugSection);
        return defaultForm;
    }
}

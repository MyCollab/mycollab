/**
 * Copyright © MyCollab
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.project.view.milestone;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.form.view.builder.DynaSectionBuilder;
import com.mycollab.form.view.builder.TextDynaFieldBuilder;
import com.mycollab.form.view.builder.type.DynaForm;
import com.mycollab.form.view.builder.type.DynaSection;
import com.mycollab.module.project.domain.Milestone;
import com.mycollab.module.project.i18n.MilestoneI18nEnum;
import com.mycollab.module.project.i18n.ProjectCommonI18nEnum;

/**
 * @author MyCollab Ltd.
 * @since 4.6.0
 */
public class MilestoneDefaultFormLayoutFactory {
    private static final DynaForm defaultForm;

    static {
        defaultForm = new DynaForm();

        DynaSection mainSection = new DynaSectionBuilder().layoutType(
                DynaSection.LayoutType.TWO_COLUMN).build();

        mainSection.fields(new TextDynaFieldBuilder().fieldName(Milestone.Field.name).displayName(GenericI18Enum.FORM_NAME)
                .fieldIndex(0).mandatory(true).required(true).colSpan(true).build());

        mainSection.fields(new TextDynaFieldBuilder().fieldName(Milestone.Field.startdate).displayName(GenericI18Enum.FORM_START_DATE)
                .fieldIndex(1).build());

        mainSection.fields(new TextDynaFieldBuilder().fieldName(Milestone.Field.enddate).displayName(GenericI18Enum.FORM_END_DATE)
                .fieldIndex(2).build());

        mainSection.fields(new TextDynaFieldBuilder().fieldName(Milestone.Field.assignuser).displayName(GenericI18Enum.FORM_ASSIGNEE)
                .fieldIndex(3).build());

        mainSection.fields(new TextDynaFieldBuilder().fieldName(Milestone.Field.status).displayName(GenericI18Enum.FORM_STATUS)
                .contextHelp(MilestoneI18nEnum.FORM_STATUS_FIELD_HELP).fieldIndex(4).build());

        mainSection.fields(new TextDynaFieldBuilder().fieldName(Milestone.Field.description).displayName(GenericI18Enum.FORM_DESCRIPTION)
                .fieldIndex(5).colSpan(true).build());

        mainSection.fields(new TextDynaFieldBuilder().fieldName(Milestone.Field.id).displayName(ProjectCommonI18nEnum.OPT_ASSIGNMENT_LIST)
                .fieldIndex(6).colSpan(true).build());

        mainSection.fields(new TextDynaFieldBuilder().fieldName(Milestone.Field.saccountid).displayName(GenericI18Enum.FORM_ATTACHMENTS)
                .fieldIndex(7).colSpan(true).build());

        defaultForm.sections(mainSection);
    }

    public static DynaForm getForm() {
        return defaultForm;
    }
}

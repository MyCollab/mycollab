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
package com.mycollab.module.project.view.bug;

import com.mycollab.common.i18n.FollowerI18nEnum;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.form.view.builder.DynaSectionBuilder;
import com.mycollab.form.view.builder.TextDynaFieldBuilder;
import com.mycollab.form.view.builder.type.DynaForm;
import com.mycollab.form.view.builder.type.DynaSection;
import com.mycollab.module.project.i18n.BugI18nEnum;
import com.mycollab.module.project.i18n.MilestoneI18nEnum;
import com.mycollab.module.tracker.domain.BugWithBLOBs;
import com.mycollab.module.tracker.domain.SimpleBug;

/**
 * @author MyCollab Ltd.
 * @since 5.0.1
 */
public class BugDefaultFormLayoutFactory {
    public static DynaForm getForm() {
        DynaForm defaultForm = new DynaForm();
        DynaSection mainSection = new DynaSectionBuilder().layoutType(DynaSection.LayoutType.TWO_COLUMN).build();

        //Row 1
        mainSection.fields(new TextDynaFieldBuilder().fieldName(BugWithBLOBs.Field.name)
                .displayName(BugI18nEnum.FORM_SUMMARY)
                .fieldIndex(0).mandatory(true).required(true).colSpan(true).build());

        //Row 2
        mainSection.fields(new TextDynaFieldBuilder().fieldName(BugWithBLOBs.Field.priority)
                .displayName(GenericI18Enum.FORM_PRIORITY)
                .contextHelp(GenericI18Enum.FORM_PRIORITY_HELP)
                .fieldIndex(1).build());

        mainSection.fields(new TextDynaFieldBuilder().fieldName(BugWithBLOBs.Field.assignuser)
                .displayName(GenericI18Enum.FORM_ASSIGNEE)
                .fieldIndex(2).build());

        //Row 3
        mainSection.fields(new TextDynaFieldBuilder().fieldName(BugWithBLOBs.Field.severity)
                .displayName(BugI18nEnum.FORM_SEVERITY)
                .fieldIndex(3).build());

        mainSection.fields(new TextDynaFieldBuilder().fieldName(SimpleBug.Field.components)
                .displayName(BugI18nEnum.FORM_COMPONENTS)
                .contextHelp(BugI18nEnum.FORM_COMPONENTS_HELP)
                .fieldIndex(4).build());

        //Row 4
        mainSection.fields(new TextDynaFieldBuilder().fieldName(BugWithBLOBs.Field.startdate)
                .displayName(GenericI18Enum.FORM_START_DATE).fieldIndex(5).build());

        mainSection.fields(new TextDynaFieldBuilder().fieldName(SimpleBug.Field.affectedVersions)
                .displayName(BugI18nEnum.FORM_AFFECTED_VERSIONS)
                .contextHelp(BugI18nEnum.FORM_AFFECTED_VERSIONS_HELP)
                .fieldIndex(6).build());


        mainSection.fields(new TextDynaFieldBuilder().fieldName(BugWithBLOBs.Field.enddate)
                .displayName(GenericI18Enum.FORM_END_DATE)
                .fieldIndex(7).build());

        mainSection.fields(new TextDynaFieldBuilder().fieldName(SimpleBug.Field.fixedVersions)
                .displayName(BugI18nEnum.FORM_FIXED_VERSIONS)
                .contextHelp(BugI18nEnum.FORM_FIXED_VERSIONS_HELP)
                .fieldIndex(8).build());

        //Row 5
        mainSection.fields(new TextDynaFieldBuilder().fieldName(BugWithBLOBs.Field.duedate)
                .displayName(GenericI18Enum.FORM_DUE_DATE)
                .fieldIndex(9).build());

        mainSection.fields(new TextDynaFieldBuilder().fieldName(BugWithBLOBs.Field.milestoneid)
                .displayName(MilestoneI18nEnum.SINGLE)
                .fieldIndex(10).build());

        mainSection.fields(new TextDynaFieldBuilder().fieldName(BugWithBLOBs.Field.originalestimate)
                .displayName(BugI18nEnum.FORM_ORIGINAL_ESTIMATE)
                .contextHelp(BugI18nEnum.FORM_ORIGINAL_ESTIMATE_HELP)
                .fieldIndex(11).build());

        mainSection.fields(new TextDynaFieldBuilder().fieldName(BugWithBLOBs.Field.remainestimate)
                .displayName(BugI18nEnum.FORM_REMAIN_ESTIMATE)
                .contextHelp(BugI18nEnum.FORM_REMAIN_ESTIMATE_HELP)
                .fieldIndex(12).build());


        mainSection.fields(new TextDynaFieldBuilder().fieldName(BugWithBLOBs.Field.environment)
                .displayName(BugI18nEnum.FORM_ENVIRONMENT)
                .fieldIndex(13).colSpan(true).build());

        mainSection.fields(new TextDynaFieldBuilder().fieldName(BugWithBLOBs.Field.description)
                .displayName(GenericI18Enum.FORM_DESCRIPTION)
                .fieldIndex(14).colSpan(true).build());

        mainSection.fields(new TextDynaFieldBuilder().fieldName(BugWithBLOBs.Field.id)
                .displayName(GenericI18Enum.FORM_ATTACHMENTS)
                .fieldIndex(15).colSpan(true).build());

        mainSection.fields(new TextDynaFieldBuilder().fieldName(SimpleBug.Field.selected)
                .displayName(FollowerI18nEnum.OPT_SUB_INFO_WATCHERS)
                .contextHelp(FollowerI18nEnum.FOLLOWER_EXPLAIN_HELP).fieldIndex(16).colSpan(true).build());

        defaultForm.sections(mainSection);
        return defaultForm;
    }
}

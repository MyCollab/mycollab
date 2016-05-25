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
package com.esofthead.mycollab.module.project.view.bug;

import com.esofthead.mycollab.common.i18n.FollowerI18nEnum;
import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.form.view.builder.DynaSectionBuilder;
import com.esofthead.mycollab.form.view.builder.TextDynaFieldBuilder;
import com.esofthead.mycollab.form.view.builder.type.DynaForm;
import com.esofthead.mycollab.form.view.builder.type.DynaSection;
import com.esofthead.mycollab.module.project.i18n.BugI18nEnum;
import com.esofthead.mycollab.module.tracker.domain.BugWithBLOBs;
import com.esofthead.mycollab.module.tracker.domain.SimpleBug;
import com.esofthead.mycollab.vaadin.AppContext;

/**
 * @author MyCollab Ltd.
 * @since 5.0.1
 */
public class BugDefaultFormLayoutFactory {
    public static DynaForm getForm() {
        DynaForm defaultForm = new DynaForm();
        DynaSection mainSection = new DynaSectionBuilder().layoutType(DynaSection.LayoutType.TWO_COLUMN).build();

        //Row 1
        mainSection.fields(new TextDynaFieldBuilder().fieldName(BugWithBLOBs.Field.summary)
                .displayName(AppContext.getMessage(BugI18nEnum.FORM_SUMMARY))
                .fieldIndex(0).mandatory(true).required(true).colSpan(true).build());

        //Row 2
        mainSection.fields(new TextDynaFieldBuilder()
                .fieldName(BugWithBLOBs.Field.priority)
                .displayName(AppContext.getMessage(BugI18nEnum.FORM_PRIORITY))
                .contextHelp(AppContext.getMessage(BugI18nEnum.FORM_PRIORITY_HELP))
                .fieldIndex(1).build());

        mainSection.fields(new TextDynaFieldBuilder().fieldName(BugWithBLOBs.Field.assignuser)
                .displayName(AppContext.getMessage(GenericI18Enum.FORM_ASSIGNEE))
                .fieldIndex(2).build());

        //Row 3
        mainSection.fields(new TextDynaFieldBuilder().fieldName(BugWithBLOBs.Field.severity)
                .displayName(AppContext.getMessage(BugI18nEnum.FORM_SEVERITY))
                .fieldIndex(3).build());

        mainSection.fields(new TextDynaFieldBuilder().fieldName(SimpleBug.Field.components)
                .displayName(AppContext.getMessage(BugI18nEnum.FORM_COMPONENTS))
                .contextHelp(AppContext.getMessage(BugI18nEnum.FORM_COMPONENTS_HELP))
                .fieldIndex(4).build());

        //Row 4
        mainSection.fields(new TextDynaFieldBuilder().fieldName(BugWithBLOBs.Field.startdate)
                .displayName(AppContext.getMessage(GenericI18Enum.FORM_START_DATE)).fieldIndex(5).build());

        mainSection.fields(new TextDynaFieldBuilder().fieldName(SimpleBug.Field.affectedVersions)
                .displayName(AppContext.getMessage(BugI18nEnum.FORM_AFFECTED_VERSIONS))
                .contextHelp(AppContext.getMessage(BugI18nEnum.FORM_AFFECTED_VERSIONS_HELP))
                .fieldIndex(6).build());


        mainSection.fields(new TextDynaFieldBuilder().fieldName(BugWithBLOBs.Field.enddate)
                .displayName(AppContext.getMessage(GenericI18Enum.FORM_END_DATE))
                .fieldIndex(7).build());

        mainSection.fields(new TextDynaFieldBuilder().fieldName(SimpleBug.Field.fixedVersions)
                .displayName(AppContext.getMessage(BugI18nEnum.FORM_FIXED_VERSIONS))
                .contextHelp(AppContext.getMessage(BugI18nEnum.FORM_FIXED_VERSIONS_HELP))
                .fieldIndex(8).build());

        //Row 5
        mainSection.fields(new TextDynaFieldBuilder().fieldName(BugWithBLOBs.Field.duedate)
                .displayName(AppContext.getMessage(GenericI18Enum.FORM_DUE_DATE))
                .fieldIndex(9).build());

        mainSection.fields(new TextDynaFieldBuilder().fieldName(BugWithBLOBs.Field.milestoneid)
                .displayName(AppContext.getMessage(BugI18nEnum.FORM_PHASE))
                .fieldIndex(10).build());

        mainSection.fields(new TextDynaFieldBuilder().fieldName(BugWithBLOBs.Field.estimatetime)
                .displayName(AppContext.getMessage(BugI18nEnum.FORM_ORIGINAL_ESTIMATE))
                .contextHelp(AppContext.getMessage(BugI18nEnum.FORM_ORIGINAL_ESTIMATE_HELP))
                .fieldIndex(11).build());

        mainSection.fields(new TextDynaFieldBuilder().fieldName(BugWithBLOBs.Field.estimateremaintime)
                .displayName(AppContext.getMessage(BugI18nEnum.FORM_REMAIN_ESTIMATE))
                .contextHelp(AppContext.getMessage(BugI18nEnum.FORM_REMAIN_ESTIMATE_HELP))
                .fieldIndex(12).build());


        mainSection.fields(new TextDynaFieldBuilder().fieldName(BugWithBLOBs.Field.environment)
                .displayName(AppContext.getMessage(BugI18nEnum.FORM_ENVIRONMENT))
                .fieldIndex(13).colSpan(true).build());

        mainSection.fields(new TextDynaFieldBuilder().fieldName(BugWithBLOBs.Field.description)
                .displayName(AppContext.getMessage(GenericI18Enum.FORM_DESCRIPTION))
                .fieldIndex(14).colSpan(true).build());

        mainSection.fields(new TextDynaFieldBuilder().fieldName(BugWithBLOBs.Field.id)
                .displayName(AppContext.getMessage(GenericI18Enum.FORM_ATTACHMENTS))
                .fieldIndex(15).colSpan(true).build());

        mainSection.fields(new TextDynaFieldBuilder().fieldName(SimpleBug.Field.selected)
                .displayName(AppContext.getMessage(FollowerI18nEnum.OPT_SUB_INFO_WATCHERS))
                .contextHelp(AppContext.getMessage(FollowerI18nEnum.FOLLOWER_EXPLAIN_HELP)).fieldIndex(16).colSpan(true).build());

        defaultForm.sections(mainSection);
        return defaultForm;
    }
}

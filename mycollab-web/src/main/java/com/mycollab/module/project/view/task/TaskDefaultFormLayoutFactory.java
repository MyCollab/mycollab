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
package com.mycollab.module.project.view.task;

import com.mycollab.common.i18n.FollowerI18nEnum;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.form.view.builder.DynaSectionBuilder;
import com.mycollab.form.view.builder.TextDynaFieldBuilder;
import com.mycollab.form.view.builder.type.DynaForm;
import com.mycollab.form.view.builder.type.DynaSection;
import com.mycollab.form.view.builder.type.DynaSection.LayoutType;
import com.mycollab.module.project.domain.SimpleTask;
import com.mycollab.module.project.domain.Task;
import com.mycollab.module.project.i18n.MilestoneI18nEnum;
import com.mycollab.module.project.i18n.TaskI18nEnum;

/**
 * @author MyCollab Ltd.
 * @since 4.5.4
 */
public class TaskDefaultFormLayoutFactory {

    public static DynaForm getForm() {
        DynaForm defaultForm = new DynaForm();

        DynaSection mainSection = new DynaSectionBuilder().layoutType(LayoutType.TWO_COLUMN).build();

        mainSection.fields(new TextDynaFieldBuilder().fieldName(Task.Field.name)
                .displayName(GenericI18Enum.FORM_NAME)
                .fieldIndex(0).mandatory(true).required(true)
                .colSpan(true).build());

        mainSection.fields(new TextDynaFieldBuilder().fieldName(Task.Field.startdate)
                .displayName(GenericI18Enum.FORM_START_DATE).fieldIndex(1).build());


        mainSection.fields(new TextDynaFieldBuilder().fieldName(Task.Field.enddate)
                .displayName(GenericI18Enum.FORM_END_DATE)
                .fieldIndex(2).build());

        mainSection.fields(new TextDynaFieldBuilder().fieldName(Task.Field.duedate)
                .displayName(GenericI18Enum.FORM_DUE_DATE)
                .fieldIndex(3).build());

        mainSection.fields(new TextDynaFieldBuilder().fieldName(Task.Field.priority)
                .displayName(GenericI18Enum.FORM_PRIORITY)
                .contextHelp(GenericI18Enum.FORM_PRIORITY_HELP)
                .fieldIndex(4).build());

        mainSection.fields(new TextDynaFieldBuilder().fieldName(Task.Field.duration)
                .displayName(GenericI18Enum.FORM_DURATION)
                .fieldIndex(5).build());

        mainSection.fields(new TextDynaFieldBuilder().fieldName(Task.Field.isestimated)
                .displayName(TaskI18nEnum.FORM_IS_ESTIMATED)
                .contextHelp(TaskI18nEnum.FORM_IS_ESTIMATED_HELP)
                .fieldIndex(6).build());

        mainSection.fields(new TextDynaFieldBuilder().fieldName(Task.Field.assignuser)
                .displayName(GenericI18Enum.FORM_ASSIGNEE)
                .fieldIndex(7).build());

        mainSection.fields(new TextDynaFieldBuilder().fieldName(Task.Field.milestoneid).required(true)
                .displayName(MilestoneI18nEnum.SINGLE)
                .fieldIndex(8).build());

        mainSection.fields(new TextDynaFieldBuilder().fieldName(Task.Field.percentagecomplete)
                .displayName(TaskI18nEnum.FORM_PERCENTAGE_COMPLETE)
                .fieldIndex(9).build());

        mainSection.fields(new TextDynaFieldBuilder().fieldName(Task.Field.status)
                .displayName(GenericI18Enum.FORM_STATUS)
                .contextHelp(TaskI18nEnum.FORM_STATUS_HELP).fieldIndex(10).build());

        mainSection.fields(new TextDynaFieldBuilder().fieldName(Task.Field.originalestimate)
                .displayName(TaskI18nEnum.FORM_ORIGINAL_ESTIMATE)
                .contextHelp(TaskI18nEnum.FORM_ORIGINAL_ESTIMATE_HELP).fieldIndex(11).build());

        mainSection.fields(new TextDynaFieldBuilder().fieldName(Task.Field.remainestimate)
                .displayName(TaskI18nEnum.FORM_REMAIN_ESTIMATE)
                .contextHelp(TaskI18nEnum.FORM_REMAIN_ESTIMATE_HELP).fieldIndex(12).build());

        mainSection.fields(new TextDynaFieldBuilder().fieldName(Task.Field.description)
                .displayName(GenericI18Enum.FORM_DESCRIPTION)
                .colSpan(true).fieldIndex(13).build());

        mainSection.fields(new TextDynaFieldBuilder().fieldName(Task.Field.id)
                .displayName(GenericI18Enum.FORM_ATTACHMENTS)
                .colSpan(true).fieldIndex(14).build());

        mainSection.fields(new TextDynaFieldBuilder().fieldName(Task.Field.parenttaskid)
                .displayName(TaskI18nEnum.FORM_SUB_TASKS)
                .contextHelp(TaskI18nEnum.FORM_SUB_TASKS_HELP)
                .colSpan(true).fieldIndex(15).build());

        mainSection.fields(new TextDynaFieldBuilder().fieldName(SimpleTask.Field.selected)
                .displayName(FollowerI18nEnum.OPT_SUB_INFO_WATCHERS)
                .contextHelp(FollowerI18nEnum.FOLLOWER_EXPLAIN_HELP).fieldIndex(16).colSpan(true).build());

        defaultForm.sections(mainSection);
        return defaultForm;
    }
}

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
package com.esofthead.mycollab.module.project.view.task;

import com.esofthead.mycollab.common.i18n.FollowerI18nEnum;
import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.form.view.builder.DynaSectionBuilder;
import com.esofthead.mycollab.form.view.builder.TextDynaFieldBuilder;
import com.esofthead.mycollab.form.view.builder.type.DynaForm;
import com.esofthead.mycollab.form.view.builder.type.DynaSection;
import com.esofthead.mycollab.form.view.builder.type.DynaSection.LayoutType;
import com.esofthead.mycollab.module.project.domain.SimpleTask;
import com.esofthead.mycollab.module.project.domain.Task;
import com.esofthead.mycollab.module.project.i18n.TaskI18nEnum;
import com.esofthead.mycollab.vaadin.AppContext;

/**
 * @author MyCollab Ltd.
 * @since 4.5.4
 */
public class TaskDefaultFormLayoutFactory {

    public static DynaForm getForm() {
        DynaForm defaultForm = new DynaForm();

        DynaSection mainSection = new DynaSectionBuilder().layoutType(LayoutType.TWO_COLUMN).build();

        mainSection.fields(new TextDynaFieldBuilder().fieldName(Task.Field.taskname)
                .displayName(AppContext.getMessage(GenericI18Enum.FORM_NAME))
                .fieldIndex(0).mandatory(true).required(true)
                .colSpan(true).build());

        mainSection.fields(new TextDynaFieldBuilder().fieldName(Task.Field.startdate)
                .displayName(AppContext.getMessage(GenericI18Enum.FORM_START_DATE)).fieldIndex(1).build());


        mainSection.fields(new TextDynaFieldBuilder().fieldName(Task.Field.enddate)
                .displayName(AppContext.getMessage(GenericI18Enum.FORM_END_DATE))
                .fieldIndex(2).build());

        mainSection.fields(new TextDynaFieldBuilder().fieldName(Task.Field.deadline)
                .displayName(AppContext.getMessage(GenericI18Enum.FORM_DUE_DATE))
                .fieldIndex(3).build());

        mainSection.fields(new TextDynaFieldBuilder().fieldName(Task.Field.priority)
                .displayName(AppContext.getMessage(TaskI18nEnum.FORM_PRIORITY))
                .contextHelp(AppContext.getMessage(TaskI18nEnum.FORM_PRIORITY_HELP))
                .fieldIndex(4).build());

        mainSection.fields(new TextDynaFieldBuilder().fieldName(Task.Field.duration)
                .displayName(AppContext.getMessage(TaskI18nEnum.FORM_DURATION))
                .fieldIndex(5).build());

        mainSection.fields(new TextDynaFieldBuilder().fieldName(Task.Field.isestimated)
                .displayName(AppContext.getMessage(TaskI18nEnum.FORM_IS_ESTIMATED))
                .contextHelp(AppContext.getMessage(TaskI18nEnum.FORM_IS_ESTIMATED_HELP))
                .fieldIndex(6).build());

        mainSection.fields(new TextDynaFieldBuilder().fieldName(Task.Field.assignuser)
                .displayName(AppContext.getMessage(GenericI18Enum.FORM_ASSIGNEE))
                .fieldIndex(7).build());

        mainSection.fields(new TextDynaFieldBuilder().fieldName(Task.Field.milestoneid).required(true)
                .displayName(AppContext.getMessage(TaskI18nEnum.FORM_PHASE))
                .fieldIndex(8).build());

        mainSection.fields(new TextDynaFieldBuilder().fieldName(Task.Field.percentagecomplete)
                .displayName(AppContext.getMessage(TaskI18nEnum.FORM_PERCENTAGE_COMPLETE))
                .fieldIndex(9).build());

        mainSection.fields(new TextDynaFieldBuilder().fieldName(Task.Field.status)
                .displayName(AppContext.getMessage(GenericI18Enum.FORM_STATUS))
                .contextHelp(AppContext.getMessage(TaskI18nEnum.FORM_STATUS_HELP)).fieldIndex(10).build());

        mainSection.fields(new TextDynaFieldBuilder().fieldName(Task.Field.originalestimate)
                .displayName(AppContext.getMessage(TaskI18nEnum.FORM_ORIGINAL_ESTIMATE))
                .contextHelp(AppContext.getMessage(TaskI18nEnum.FORM_ORIGINAL_ESTIMATE_HELP)).fieldIndex(11).build());

        mainSection.fields(new TextDynaFieldBuilder().fieldName(Task.Field.remainestimate)
                .displayName(AppContext.getMessage(TaskI18nEnum.FORM_REMAIN_ESTIMATE))
                .contextHelp(AppContext.getMessage(TaskI18nEnum.FORM_REMAIN_ESTIMATE_HELP)).fieldIndex(12).build());

        mainSection.fields(new TextDynaFieldBuilder().fieldName(Task.Field.notes)
                .displayName(AppContext.getMessage(TaskI18nEnum.FORM_NOTES))
                .colSpan(true).fieldIndex(13).build());

        mainSection.fields(new TextDynaFieldBuilder().fieldName(Task.Field.id)
                .displayName(AppContext.getMessage(GenericI18Enum.FORM_ATTACHMENTS))
                .colSpan(true).fieldIndex(14).build());

        mainSection.fields(new TextDynaFieldBuilder().fieldName(Task.Field.parenttaskid)
                .displayName(AppContext.getMessage(TaskI18nEnum.FORM_SUB_TASKS))
                .contextHelp(AppContext.getMessage(TaskI18nEnum.FORM_SUB_TASKS_HELP))
                .colSpan(true).fieldIndex(15).build());

        mainSection.fields(new TextDynaFieldBuilder().fieldName(SimpleTask.Field.selected)
                .displayName(AppContext.getMessage(FollowerI18nEnum.OPT_SUB_INFO_WATCHERS))
                .contextHelp(AppContext.getMessage(FollowerI18nEnum.FOLLOWER_EXPLAIN_HELP)).fieldIndex(16).colSpan(true).build());

        defaultForm.sections(mainSection);
        return defaultForm;
    }
}

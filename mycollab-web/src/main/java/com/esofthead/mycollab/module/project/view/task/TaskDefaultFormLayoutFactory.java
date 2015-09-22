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

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.form.view.builder.DynaSectionBuilder;
import com.esofthead.mycollab.form.view.builder.TextDynaFieldBuilder;
import com.esofthead.mycollab.form.view.builder.type.DynaForm;
import com.esofthead.mycollab.form.view.builder.type.DynaSection;
import com.esofthead.mycollab.form.view.builder.type.DynaSection.LayoutType;
import com.esofthead.mycollab.module.project.domain.Task;
import com.esofthead.mycollab.module.project.i18n.TaskI18nEnum;
import com.esofthead.mycollab.vaadin.AppContext;

/**
 * @author MyCollab Ltd.
 * @since 4.5.4
 */
public class TaskDefaultFormLayoutFactory {

    private static final DynaForm defaultForm;

    static {
        defaultForm = new DynaForm();

        DynaSection mainSection = new DynaSectionBuilder().layoutType(LayoutType.TWO_COLUMN).build();

        mainSection.addField(new TextDynaFieldBuilder().fieldName(Task.Field.taskname)
                .displayName(AppContext.getMessage(TaskI18nEnum.FORM_TASK_NAME))
                .fieldIndex(0).mandatory(true).required(true)
                .colSpan(true).build());

        mainSection.addField(new TextDynaFieldBuilder().fieldName(Task.Field.startdate)
                .displayName(AppContext.getMessage(TaskI18nEnum.FORM_START_DATE)).fieldIndex(1).build());

        mainSection.addField(new TextDynaFieldBuilder().fieldName(Task.Field.actualstartdate)
                .displayName(AppContext.getMessage(TaskI18nEnum.FORM_ACTUAL_START_DATE))
                .fieldIndex(2).build());

        mainSection.addField(new TextDynaFieldBuilder().fieldName(Task.Field.enddate)
                .displayName(AppContext.getMessage(TaskI18nEnum.FORM_END_DATE))
                .fieldIndex(3).build());

        mainSection.addField(new TextDynaFieldBuilder().fieldName(Task.Field.actualenddate)
                .displayName(AppContext.getMessage(TaskI18nEnum.FORM_ACTUAL_END_DATE))
                .fieldIndex(4).build());

        mainSection.addField(new TextDynaFieldBuilder().fieldName(Task.Field.deadline)
                .displayName(AppContext.getMessage(TaskI18nEnum.FORM_DEADLINE))
                .fieldIndex(5).build());

        mainSection.addField(new TextDynaFieldBuilder().fieldName(Task.Field.priority)
                .displayName(AppContext.getMessage(TaskI18nEnum.FORM_PRIORITY))
                .fieldIndex(6).build());

        mainSection.addField(new TextDynaFieldBuilder().fieldName(Task.Field.duration)
                .displayName(AppContext.getMessage(TaskI18nEnum.FORM_DURATION))
                .fieldIndex(7).build());

        mainSection.addField(new TextDynaFieldBuilder().fieldName(Task.Field.isestimated)
                .displayName(AppContext.getMessage(TaskI18nEnum.FORM_IS_ESTIMATED))
                .fieldIndex(8).build());

        mainSection.addField(new TextDynaFieldBuilder().fieldName(Task.Field.assignuser)
                .displayName(AppContext.getMessage(GenericI18Enum.FORM_ASSIGNEE))
                .fieldIndex(9).build());

        mainSection.addField(new TextDynaFieldBuilder().fieldName(Task.Field.milestoneid).required(true)
                .displayName(AppContext.getMessage(TaskI18nEnum.FORM_MILESTONE))
                .fieldIndex(10).build());

        mainSection.addField(new TextDynaFieldBuilder().fieldName(Task.Field.percentagecomplete)
                .displayName(AppContext.getMessage(TaskI18nEnum.FORM_PERCENTAGE_COMPLETE))
               .fieldIndex(11).build());

        mainSection.addField(new TextDynaFieldBuilder().fieldName(Task.Field.status)
                .displayName(AppContext.getMessage(TaskI18nEnum.FORM_STATUS))
                .fieldIndex(12).build());

        mainSection.addField(new TextDynaFieldBuilder().fieldName(Task.Field.notes)
                .displayName(AppContext.getMessage(TaskI18nEnum.FORM_NOTES))
                .colSpan(true).fieldIndex(13).build());

        mainSection.addField(new TextDynaFieldBuilder().fieldName(Task.Field.id)
                .displayName(AppContext.getMessage(TaskI18nEnum.FORM_ATTACHMENT))
                .colSpan(true).fieldIndex(14).build());

        mainSection.addField(new TextDynaFieldBuilder().fieldName(Task.Field.parenttaskid)
                .displayName(AppContext.getMessage(TaskI18nEnum.FORM_SUB_TASKS))
                .colSpan(true).fieldIndex(15).build());

        defaultForm.addSection(mainSection);
    }

    public static DynaForm getForm() {
        return defaultForm;
    }
}

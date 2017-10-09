package com.mycollab.mobile.module.project.view.milestone;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.form.view.builder.DynaSectionBuilder;
import com.mycollab.form.view.builder.TextDynaFieldBuilder;
import com.mycollab.form.view.builder.type.DynaForm;
import com.mycollab.form.view.builder.type.DynaSection;
import com.mycollab.module.project.domain.Milestone;

/**
 * @author MyCollab Ltd
 * @since 5.4.3
 */
public class MilestoneDefaultFormLayoutFactory {
    public static DynaForm getForm() {
        DynaForm defaultForm = new DynaForm();
        DynaSection milestoneSection = new DynaSectionBuilder().layoutType(DynaSection.LayoutType.ONE_COLUMN).orderIndex(0).build();

        milestoneSection.fields(new TextDynaFieldBuilder().fieldName(Milestone.Field.name.name())
                .displayName(GenericI18Enum.FORM_NAME)
                .customField(false).fieldIndex(0).mandatory(true)
                .required(true).build());

        milestoneSection.fields(new TextDynaFieldBuilder().fieldName(Milestone.Field.status.name())
                .displayName(GenericI18Enum.FORM_STATUS)
                .customField(false).fieldIndex(1).mandatory(true)
                .required(true).build());

        milestoneSection.fields(new TextDynaFieldBuilder().fieldName(Milestone.Field.assignuser.name())
                .displayName(GenericI18Enum.FORM_ASSIGNEE)
                .customField(false).fieldIndex(2).mandatory(true)
                .required(true).build());

        milestoneSection.fields(new TextDynaFieldBuilder().fieldName(Milestone.Field.startdate.name())
                .displayName(GenericI18Enum.FORM_START_DATE)
                .customField(false).fieldIndex(3).mandatory(false)
                .required(false).build());

        milestoneSection.fields(new TextDynaFieldBuilder().fieldName(Milestone.Field.enddate.name())
                .displayName(GenericI18Enum.FORM_END_DATE)
                .customField(false).fieldIndex(4).mandatory(false)
                .required(false).build());

        milestoneSection.fields(new TextDynaFieldBuilder().fieldName(Milestone.Field.description.name())
                .displayName(GenericI18Enum.FORM_DESCRIPTION)
                .customField(false).fieldIndex(5).mandatory(false)
                .required(false).build());

        defaultForm.sections(milestoneSection);
        return defaultForm;
    }
}

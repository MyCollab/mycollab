package com.mycollab.module.project.view.user;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.form.view.LayoutType;
import com.mycollab.form.view.builder.DynaSectionBuilder;
import com.mycollab.form.view.builder.TextDynaFieldBuilder;
import com.mycollab.form.view.builder.type.DynaForm;
import com.mycollab.form.view.builder.type.DynaSection;
import com.mycollab.module.project.domain.Project;
import com.mycollab.module.project.i18n.ProjectI18nEnum;

/**
 * @author MyCollab Ltd
 * @since 7.0.0
 */
public class ProjectDefaultFormLayoutFactory {
    private static DynaSection mainSection() {
        DynaSection mainSection = new DynaSectionBuilder().layoutType(LayoutType.TWO_COLUMN).header(ProjectI18nEnum.SECTION_PROJECT_INFO).build();

        //Row 1
        mainSection.fields(new TextDynaFieldBuilder().fieldName(Project.Field.name)
                .displayName(GenericI18Enum.FORM_NAME)
                .fieldIndex(0).mandatory(true).required(true).build());

        mainSection.fields(new TextDynaFieldBuilder().fieldName(Project.Field.homepage)
                .displayName(ProjectI18nEnum.FORM_HOME_PAGE)
                .fieldIndex(1).build());

        // Row 2
        mainSection.fields(new TextDynaFieldBuilder().fieldName(Project.Field.shortname)
                .displayName(ProjectI18nEnum.FORM_SHORT_NAME).contextHelp(ProjectI18nEnum.FORM_SHORT_NAME_HELP)
                .fieldIndex(2).mandatory(true).required(true).build());

        mainSection.fields(new TextDynaFieldBuilder().fieldName(Project.Field.status)
                .displayName(GenericI18Enum.FORM_STATUS)
                .fieldIndex(3).build());

        mainSection.fields(new TextDynaFieldBuilder().fieldName(Project.Field.memlead)
                .displayName(ProjectI18nEnum.FORM_LEADER)
                .fieldIndex(4).build());

        return mainSection;
    }

    private static DynaSection financialSection() {
        DynaSection financialSection = new DynaSectionBuilder().layoutType(LayoutType.TWO_COLUMN).header(ProjectI18nEnum.SECTION_FINANCE_SCHEDULE).build();

        //Row 1
        financialSection.fields(new TextDynaFieldBuilder().fieldName(Project.Field.planstartdate)
                .displayName(GenericI18Enum.FORM_START_DATE)
                .fieldIndex(0).build());

        financialSection.fields(new TextDynaFieldBuilder().fieldName(Project.Field.clientid)
                .displayName(ProjectI18nEnum.FORM_ACCOUNT_NAME).contextHelp(ProjectI18nEnum.FORM_ACCOUNT_NAME_HELP)
                .fieldIndex(1).build());

        // Row 2
        financialSection.fields(new TextDynaFieldBuilder().fieldName(Project.Field.planenddate)
                .displayName(GenericI18Enum.FORM_END_DATE)
                .fieldIndex(2).build());

        financialSection.fields(new TextDynaFieldBuilder().fieldName(Project.Field.currencyid)
                .displayName(GenericI18Enum.FORM_CURRENCY)
                .fieldIndex(3).build());

        // Row 3
        financialSection.fields(new TextDynaFieldBuilder().fieldName(Project.Field.defaultbillingrate)
                .displayName(ProjectI18nEnum.FORM_BILLING_RATE).contextHelp(ProjectI18nEnum.FORM_OVERTIME_BILLING_RATE_HELP)
                .fieldIndex(4).build());

        financialSection.fields(new TextDynaFieldBuilder().fieldName(Project.Field.defaultovertimebillingrate)
                .displayName(ProjectI18nEnum.FORM_OVERTIME_BILLING_RATE).contextHelp(ProjectI18nEnum.FORM_OVERTIME_BILLING_RATE_HELP)
                .fieldIndex(5).build());

        return financialSection;
    }

    private static DynaSection descriptionForm() {
        DynaSection descSection = new DynaSectionBuilder().layoutType(LayoutType.ONE_COLUMN).header(GenericI18Enum.FORM_DESCRIPTION).build();
        descSection.fields(new TextDynaFieldBuilder().fieldName(Project.Field.description)
                .fieldIndex(0).build());
        return descSection;
    }

    public static DynaForm getAddForm() {
        return new DynaForm(mainSection(), financialSection(), descriptionForm());
    }

}

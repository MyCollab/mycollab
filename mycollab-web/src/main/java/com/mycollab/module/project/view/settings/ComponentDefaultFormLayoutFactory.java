package com.mycollab.module.project.view.settings;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.form.view.builder.DynaSectionBuilder;
import com.mycollab.form.view.builder.TextDynaFieldBuilder;
import com.mycollab.form.view.builder.type.DynaForm;
import com.mycollab.form.view.builder.type.DynaSection;
import com.mycollab.form.view.builder.type.DynaSection.LayoutType;
import com.mycollab.module.project.i18n.BugI18nEnum;
import com.mycollab.module.project.i18n.ComponentI18nEnum;
import com.mycollab.module.tracker.domain.Component;

/**
 * @author MyCollab Ltd.
 * @since 4.5.4
 */
public class ComponentDefaultFormLayoutFactory {
    private static final DynaForm defaultForm;

    static {
        defaultForm = new DynaForm();
        DynaSection mainSection = new DynaSectionBuilder().layoutType(LayoutType.ONE_COLUMN).build();

        mainSection.fields(new TextDynaFieldBuilder().fieldName(Component.Field.name)
                .displayName(GenericI18Enum.FORM_NAME)
                .required(true).mandatory(true).fieldIndex(0).build());

        mainSection.fields(new TextDynaFieldBuilder().fieldName(Component.Field.description)
                .displayName(GenericI18Enum.FORM_DESCRIPTION)
                .fieldIndex(1).build());

        mainSection.fields(new TextDynaFieldBuilder().fieldName(Component.Field.userlead)
                .displayName(ComponentI18nEnum.FORM_LEAD)
                .fieldIndex(2).build());

        mainSection.fields(new TextDynaFieldBuilder().fieldName(Component.Field.id).displayName(BugI18nEnum.LIST).fieldIndex(3).build());

        defaultForm.sections(mainSection);
    }

    public static DynaForm getForm() {
        return defaultForm;
    }
}

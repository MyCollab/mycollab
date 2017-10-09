package com.mycollab.mobile.module.crm.view.opportunity;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.form.view.builder.*;
import com.mycollab.form.view.builder.type.DynaForm;
import com.mycollab.form.view.builder.type.DynaSection;
import com.mycollab.form.view.builder.type.DynaSection.LayoutType;
import com.mycollab.module.crm.i18n.OpportunityI18nEnum;

/**
 * @author MyCollab Ltd.
 * @since 4.1
 */
public class OpportunityDefaultDynaFormLayoutFactory {
    public static final DynaForm defaultForm;

    static {
        defaultForm = new DynaForm();

        DynaSection infoSection = new DynaSectionBuilder().layoutType(LayoutType.TWO_COLUMN).orderIndex(0)
                .header(OpportunityI18nEnum.SECTION_OPPORTUNITY_INFORMATION)
                .build();

        infoSection.fields(new TextDynaFieldBuilder().fieldName("opportunityname")
                .displayName(GenericI18Enum.FORM_NAME)
                .mandatory(true).fieldIndex(0).build());

        infoSection.fields(new TextDynaFieldBuilder().fieldName("accountid")
                .displayName(OpportunityI18nEnum.FORM_ACCOUNT_NAME)
                .fieldIndex(1).build());

        infoSection.fields(new CurrencyDynaFieldBuilder().fieldName("currencyid")
                .displayName(GenericI18Enum.FORM_CURRENCY)
                .fieldIndex(2).build());

        infoSection.fields(new DateDynaFieldBuilder().fieldName("expectedcloseddate")
                .displayName(OpportunityI18nEnum.FORM_EXPECTED_CLOSE_DATE)
                .fieldIndex(3).build());

        infoSection.fields(new NumberDynaFieldBuilder().fieldName("amount")
                .displayName(OpportunityI18nEnum.FORM_AMOUNT)
                .fieldIndex(4).build());

        infoSection.fields(new TextDynaFieldBuilder().fieldName("opportunitytype")
                .displayName(GenericI18Enum.FORM_TYPE)
                .fieldIndex(5).build());

        infoSection.fields(new TextDynaFieldBuilder().fieldName("salesstage")
                .displayName(OpportunityI18nEnum.FORM_SALE_STAGE)
                .fieldIndex(6).build());

        infoSection.fields(new TextDynaFieldBuilder().fieldName("source")
                .displayName(OpportunityI18nEnum.FORM_SOURCE)
                .fieldIndex(7).build());

        infoSection.fields(new PercentageDynaFieldBuilder().fieldName("probability")
                .displayName(OpportunityI18nEnum.FORM_PROBABILITY)
                .fieldIndex(8).build());

        infoSection.fields(new TextDynaFieldBuilder().fieldName("campaignid")
                .displayName(OpportunityI18nEnum.FORM_CAMPAIGN_NAME)
                .fieldIndex(9).build());

        infoSection.fields(new TextDynaFieldBuilder().fieldName("nextstep")
                .displayName(OpportunityI18nEnum.FORM_NEXT_STEP)
                .fieldIndex(10).build());

        infoSection.fields(new TextDynaFieldBuilder().fieldName("assignuser")
                .displayName(GenericI18Enum.FORM_ASSIGNEE)
                .fieldIndex(11).build());

        defaultForm.sections(infoSection);

        DynaSection descSection = new DynaSectionBuilder().layoutType(LayoutType.ONE_COLUMN).orderIndex(1)
                .header(OpportunityI18nEnum.SECTION_OPPORTUNITY_INFORMATION)
                .build();

        descSection.fields(new TextAreaDynaFieldBuilder().fieldName("description")
                .displayName(GenericI18Enum.FORM_DESCRIPTION)
                .fieldIndex(0).build());

        defaultForm.sections(descSection);
    }

    public static DynaForm getForm() {
        return defaultForm;
    }
}

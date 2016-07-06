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
package com.mycollab.module.crm.view.opportunity;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.form.view.builder.*;
import com.mycollab.form.view.builder.type.DynaForm;
import com.mycollab.form.view.builder.type.DynaSection;
import com.mycollab.form.view.builder.type.DynaSection.LayoutType;
import com.mycollab.module.crm.i18n.OpportunityI18nEnum;
import com.mycollab.vaadin.AppContext;

/**
 * @author MyCollab Ltd.
 * @since 2.0
 */
public class OpportunityDefaultDynaFormLayoutFactory {
    public static final DynaForm defaultForm;

    static {
        defaultForm = new DynaForm();

        DynaSection infoSection = new DynaSectionBuilder().layoutType(LayoutType.TWO_COLUMN).orderIndex(0)
                .header(AppContext.getMessage(OpportunityI18nEnum.SECTION_OPPORTUNITY_INFORMATION))
                .build();

        infoSection.fields(new TextDynaFieldBuilder().fieldName("opportunityname")
                .displayName(AppContext.getMessage(GenericI18Enum.FORM_NAME))
                .mandatory(true).fieldIndex(0).build());

        infoSection.fields(new TextDynaFieldBuilder().fieldName("accountid")
                .displayName(AppContext.getMessage(OpportunityI18nEnum.FORM_ACCOUNT_NAME))
                .fieldIndex(1).build());

        infoSection.fields(new CurrencyDynaFieldBuilder().fieldName("currencyid")
                .displayName(AppContext.getMessage(GenericI18Enum.FORM_CURRENCY))
                .fieldIndex(2).build());

        infoSection.fields(new DateDynaFieldBuilder().fieldName("expectedcloseddate")
                .displayName(AppContext.getMessage(OpportunityI18nEnum.FORM_EXPECTED_CLOSE_DATE))
                .fieldIndex(3).build());

        infoSection.fields(new NumberDynaFieldBuilder().fieldName("amount")
                .displayName(AppContext.getMessage(OpportunityI18nEnum.FORM_AMOUNT))
                .fieldIndex(4).build());

        infoSection.fields(new TextDynaFieldBuilder().fieldName("opportunitytype")
                .displayName(AppContext.getMessage(GenericI18Enum.FORM_TYPE))
                .fieldIndex(5).build());

        infoSection.fields(new TextDynaFieldBuilder().fieldName("salesstage")
                .displayName(AppContext.getMessage(OpportunityI18nEnum.FORM_SALE_STAGE))
                .fieldIndex(6).build());

        infoSection.fields(new TextDynaFieldBuilder().fieldName("source")
                .displayName(AppContext.getMessage(OpportunityI18nEnum.FORM_LEAD_SOURCE))
                .fieldIndex(7).build());

        infoSection.fields(new PercentageDynaFieldBuilder().fieldName("probability")
                .displayName(AppContext.getMessage(OpportunityI18nEnum.FORM_PROBABILITY))
                .fieldIndex(8).build());

        infoSection.fields(new TextDynaFieldBuilder().fieldName("campaignid")
                .displayName(AppContext.getMessage(OpportunityI18nEnum.FORM_CAMPAIGN_NAME))
                .fieldIndex(9).build());

        infoSection.fields(new TextDynaFieldBuilder().fieldName("nextstep")
                .displayName(AppContext.getMessage(OpportunityI18nEnum.FORM_NEXT_STEP))
                .fieldIndex(10).build());

        infoSection.fields(new TextDynaFieldBuilder().fieldName("assignuser")
                .displayName(AppContext.getMessage(GenericI18Enum.FORM_ASSIGNEE))
                .fieldIndex(11).build());

        defaultForm.sections(infoSection);

        DynaSection descSection = new DynaSectionBuilder().layoutType(LayoutType.ONE_COLUMN).orderIndex(1)
                .header(AppContext.getMessage(OpportunityI18nEnum.SECTION_DESCRIPTION))
                .build();

        descSection.fields(new TextAreaDynaFieldBuilder().fieldName("description")
                .displayName(AppContext.getMessage(GenericI18Enum.FORM_DESCRIPTION))
                .fieldIndex(0).build());

        defaultForm.sections(descSection);
    }

    public static DynaForm getForm() {
        return defaultForm;
    }
}

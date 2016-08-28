/**
 * This file is part of mycollab-mobile.
 *
 * mycollab-mobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-mobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-mobile.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.mobile.module.crm.view.campaign;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.form.view.builder.*;
import com.mycollab.form.view.builder.type.DynaForm;
import com.mycollab.form.view.builder.type.DynaSection;
import com.mycollab.form.view.builder.type.DynaSection.LayoutType;
import com.mycollab.module.crm.i18n.CampaignI18nEnum;

/**
 * @author MyCollab Ltd.
 * @since 4.1
 */
public class CampaignDefaultDynaFormLayoutFactory {
    public static final DynaForm defaultForm;

    static {
        defaultForm = new DynaForm();

        DynaSection campaignSection = new DynaSectionBuilder().layoutType(LayoutType.TWO_COLUMN).orderIndex(0)
                .header(CampaignI18nEnum.SECTION_CAMPAIGN_INFORMATION)
                .build();

        campaignSection.fields(new TextDynaFieldBuilder().fieldName("campaignname")
                .displayName(GenericI18Enum.FORM_NAME)
                .mandatory(true).fieldIndex(0).build());

        campaignSection.fields(new TextDynaFieldBuilder().fieldName("status")
                .displayName(GenericI18Enum.FORM_STATUS)
                .fieldIndex(1).build());

        campaignSection.fields(new DateDynaFieldBuilder().fieldName("startdate")
                .displayName(GenericI18Enum.FORM_START_DATE)
                .fieldIndex(2).build());

        campaignSection.fields(new TextDynaFieldBuilder().fieldName("type")
                .displayName(GenericI18Enum.FORM_TYPE)
                .fieldIndex(3).build());

        campaignSection.fields(new DateDynaFieldBuilder().fieldName("enddate")
                .displayName(GenericI18Enum.FORM_END_DATE)
                .fieldIndex(4).build());

        campaignSection.fields(new TextDynaFieldBuilder().fieldName("assignuser")
                .displayName(GenericI18Enum.FORM_ASSIGNEE)
                .fieldIndex(5).build());

        defaultForm.sections(campaignSection);

        DynaSection goalSection = new DynaSectionBuilder().layoutType(LayoutType.TWO_COLUMN).orderIndex(1)
                .header(CampaignI18nEnum.SECTION_GOAL).build();

        goalSection.fields(new CurrencyDynaFieldBuilder().fieldName("currencyid")
                .displayName(GenericI18Enum.FORM_CURRENCY)
                .fieldIndex(0).build());

        goalSection.fields(new NumberDynaFieldBuilder().fieldName("expectedcost")
                .displayName(CampaignI18nEnum.FORM_EXPECTED_COST)
                .fieldIndex(1).build());

        goalSection.fields(new NumberDynaFieldBuilder().fieldName("budget")
                .displayName(CampaignI18nEnum.FORM_BUDGET)
                .fieldIndex(2).build());

        goalSection.fields(new NumberDynaFieldBuilder().fieldName("expectedrevenue")
                .displayName(CampaignI18nEnum.FORM_EXPECTED_REVENUE)
                .fieldIndex(3).build());

        goalSection.fields(new NumberDynaFieldBuilder().fieldName("actualcost")
                .displayName(CampaignI18nEnum.FORM_ACTUAL_COST)
                .fieldIndex(4).build());

        defaultForm.sections(goalSection);

        DynaSection descSection = new DynaSectionBuilder().layoutType(LayoutType.ONE_COLUMN).orderIndex(2)
                .header(CampaignI18nEnum.SECTION_DESCRIPTION).build();

        descSection.fields(new TextAreaDynaFieldBuilder().fieldName("description")
                .displayName(GenericI18Enum.FORM_DESCRIPTION)
                .fieldIndex(0).build());

        defaultForm.sections(descSection);
    }

    public static DynaForm getForm() {
        return defaultForm;
    }
}

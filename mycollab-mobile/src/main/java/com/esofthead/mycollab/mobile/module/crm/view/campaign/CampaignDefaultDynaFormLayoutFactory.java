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
package com.esofthead.mycollab.mobile.module.crm.view.campaign;

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.form.view.builder.*;
import com.esofthead.mycollab.form.view.builder.type.DynaForm;
import com.esofthead.mycollab.form.view.builder.type.DynaSection;
import com.esofthead.mycollab.form.view.builder.type.DynaSection.LayoutType;
import com.esofthead.mycollab.module.crm.i18n.CampaignI18nEnum;
import com.esofthead.mycollab.vaadin.AppContext;

/**
 * @author MyCollab Ltd.
 * @since 4.1
 */
public class CampaignDefaultDynaFormLayoutFactory {
    public static final DynaForm defaultForm;

    static {
        defaultForm = new DynaForm();

        DynaSection campaignSection = new DynaSectionBuilder().layoutType(LayoutType.TWO_COLUMN).orderIndex(0)
                .header(AppContext.getMessage(CampaignI18nEnum.SECTION_CAMPAIGN_INFORMATION))
                .build();

        campaignSection.addField(new TextDynaFieldBuilder().fieldName("campaignname")
                .displayName(AppContext.getMessage(GenericI18Enum.FORM_NAME))
                .mandatory(true).fieldIndex(0).build());

        campaignSection.addField(new TextDynaFieldBuilder().fieldName("status")
                .displayName(AppContext.getMessage(GenericI18Enum.FORM_STATUS))
                .fieldIndex(1).build());

        campaignSection.addField(new DateDynaFieldBuilder().fieldName("startdate")
                .displayName(AppContext.getMessage(GenericI18Enum.FORM_START_DATE))
                .fieldIndex(2).build());

        campaignSection.addField(new TextDynaFieldBuilder().fieldName("type")
                .displayName(AppContext.getMessage(GenericI18Enum.FORM_TYPE))
                .fieldIndex(3).build());

        campaignSection.addField(new DateDynaFieldBuilder().fieldName("enddate")
                .displayName(AppContext.getMessage(GenericI18Enum.FORM_END_DATE))
                .fieldIndex(4).build());

        campaignSection.addField(new TextDynaFieldBuilder().fieldName("assignuser")
                .displayName(AppContext.getMessage(GenericI18Enum.FORM_ASSIGNEE))
                .fieldIndex(5).build());

        defaultForm.addSection(campaignSection);

        DynaSection goalSection = new DynaSectionBuilder()
                .layoutType(LayoutType.TWO_COLUMN).orderIndex(1)
                .header(AppContext.getMessage(CampaignI18nEnum.SECTION_GOAL))
                .build();

        goalSection.addField(new CurrencyDynaFieldBuilder().fieldName("currencyid")
                .displayName(AppContext.getMessage(GenericI18Enum.FORM_CURRENCY))
                .fieldIndex(0).build());

        goalSection.addField(new NumberDynaFieldBuilder().fieldName("expectedcost")
                .displayName(AppContext.getMessage(CampaignI18nEnum.FORM_EXPECTED_COST))
                .fieldIndex(1).build());

        goalSection.addField(new NumberDynaFieldBuilder().fieldName("budget")
                .displayName(AppContext.getMessage(CampaignI18nEnum.FORM_BUDGET))
                .fieldIndex(2).build());

        goalSection.addField(new NumberDynaFieldBuilder().fieldName("expectedrevenue")
                .displayName(AppContext.getMessage(CampaignI18nEnum.FORM_EXPECTED_REVENUE))
                .fieldIndex(3).build());

        goalSection.addField(new NumberDynaFieldBuilder().fieldName("actualcost")
                .displayName(AppContext.getMessage(CampaignI18nEnum.FORM_ACTUAL_COST))
                .fieldIndex(4).build());

        defaultForm.addSection(goalSection);

        DynaSection descSection = new DynaSectionBuilder().layoutType(LayoutType.ONE_COLUMN).orderIndex(2)
                .header(AppContext.getMessage(CampaignI18nEnum.SECTION_DESCRIPTION)).build();

        descSection.addField(new TextAreaDynaFieldBuilder().fieldName("description")
                .displayName(AppContext.getMessage(GenericI18Enum.FORM_DESCRIPTION))
                .fieldIndex(0).build());

        defaultForm.addSection(descSection);
    }

    public static DynaForm getForm() {
        return defaultForm;
    }
}

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
package com.esofthead.mycollab.module.crm.ui.format;

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.module.crm.i18n.CampaignI18nEnum;
import com.esofthead.mycollab.module.user.ui.components.UserHistoryFieldFormat;
import com.esofthead.mycollab.utils.FieldGroupFormatter;

/**
 * @author MyCollab Ltd
 * @since 5.1.4
 */
public class CampaignFieldFormatter extends FieldGroupFormatter {
    private static final CampaignFieldFormatter _instance = new CampaignFieldFormatter();

    private CampaignFieldFormatter() {
        generateFieldDisplayHandler("campaignname", CampaignI18nEnum.FORM_CAMPAIGN_NAME);
        generateFieldDisplayHandler("startdate", CampaignI18nEnum.FORM_START_DATE,
                FieldGroupFormatter.DATE_FIELD);
        generateFieldDisplayHandler("enddate", CampaignI18nEnum.FORM_END_DATE, FieldGroupFormatter.DATE_FIELD);
        generateFieldDisplayHandler("status", CampaignI18nEnum.FORM_STATUS);
        generateFieldDisplayHandler("type", CampaignI18nEnum.FORM_TYPE);
        generateFieldDisplayHandler("currencyid", CampaignI18nEnum.FORM_CURRENCY, FieldGroupFormatter.CURRENCY_FIELD);
        generateFieldDisplayHandler("budget", CampaignI18nEnum.FORM_BUDGET);
        generateFieldDisplayHandler("expectedcost", CampaignI18nEnum.FORM_EXPECTED_COST);
        generateFieldDisplayHandler("actualcost", CampaignI18nEnum.FORM_ACTUAL_COST);
        generateFieldDisplayHandler("expectedrevenue", CampaignI18nEnum.FORM_EXPECTED_REVENUE);
        generateFieldDisplayHandler("assignuser", GenericI18Enum.FORM_ASSIGNEE, new UserHistoryFieldFormat());
        generateFieldDisplayHandler("description", GenericI18Enum.FORM_DESCRIPTION);
    }

    public static CampaignFieldFormatter instance() {
        return _instance;
    }
}

/**
 * This file is part of mycollab-ui.
 *
 * mycollab-ui is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-ui is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-ui.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.project.ui.format;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.common.i18n.OptionI18nEnum.StatusI18nEnum;
import com.mycollab.module.project.domain.Risk;
import com.mycollab.module.project.i18n.OptionI18nEnum.RiskConsequence;
import com.mycollab.module.project.i18n.OptionI18nEnum.RiskProbability;
import com.mycollab.module.project.i18n.RiskI18nEnum;
import com.mycollab.vaadin.ui.formatter.FieldGroupFormatter;
import com.mycollab.vaadin.ui.formatter.I18nHistoryFieldFormat;

/**
 * @author MyCollab Ltd.
 * @since 4.3.3
 */
public final class RiskFieldFormatter extends FieldGroupFormatter {
    private static final RiskFieldFormatter _instance = new RiskFieldFormatter();

    public RiskFieldFormatter() {
        super();

        this.generateFieldDisplayHandler("riskname", GenericI18Enum.FORM_NAME);
        this.generateFieldDisplayHandler("description", GenericI18Enum.FORM_DESCRIPTION, TRIM_HTMLS);
        this.generateFieldDisplayHandler("raisedbyuser", RiskI18nEnum.FORM_RAISED_BY, new ProjectMemberHistoryFieldFormat());
        this.generateFieldDisplayHandler("assigntouser", GenericI18Enum.FORM_ASSIGNEE, new ProjectMemberHistoryFieldFormat());
        this.generateFieldDisplayHandler("consequence", RiskI18nEnum.FORM_CONSEQUENCE, new I18nHistoryFieldFormat(RiskConsequence.class));
        this.generateFieldDisplayHandler("datedue", GenericI18Enum.FORM_DUE_DATE, DATE_FIELD);
        this.generateFieldDisplayHandler(Risk.Field.startdate.name(), GenericI18Enum.FORM_START_DATE, DATE_FIELD);
        this.generateFieldDisplayHandler(Risk.Field.enddate.name(), GenericI18Enum.FORM_END_DATE, DATE_FIELD);
        this.generateFieldDisplayHandler(Risk.Field.milestoneid.name(), RiskI18nEnum.FORM_PHASE, new MilestoneHistoryFieldFormat());
        this.generateFieldDisplayHandler(Risk.Field.probalitity.name(), RiskI18nEnum.FORM_PROBABILITY,
                new I18nHistoryFieldFormat(RiskProbability.class));
        this.generateFieldDisplayHandler("status", GenericI18Enum.FORM_STATUS, new I18nHistoryFieldFormat(StatusI18nEnum.class));
        this.generateFieldDisplayHandler("priority", GenericI18Enum.FORM_PRIORITY);
        this.generateFieldDisplayHandler("response", RiskI18nEnum.FORM_RESPONSE, TRIM_HTMLS);
    }

    public static RiskFieldFormatter instance() {
        return _instance;
    }
}

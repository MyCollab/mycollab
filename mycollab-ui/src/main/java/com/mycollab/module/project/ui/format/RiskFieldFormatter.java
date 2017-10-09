package com.mycollab.module.project.ui.format;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.common.i18n.OptionI18nEnum.StatusI18nEnum;
import com.mycollab.module.project.domain.Risk;
import com.mycollab.module.project.i18n.MilestoneI18nEnum;
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
        this.generateFieldDisplayHandler(Risk.Field.name.name(), GenericI18Enum.FORM_NAME);
        this.generateFieldDisplayHandler(Risk.Field.description.name(), GenericI18Enum.FORM_DESCRIPTION, TRIM_HTMLS);
        this.generateFieldDisplayHandler(Risk.Field.createduser.name(), RiskI18nEnum.FORM_RAISED_BY, new ProjectMemberHistoryFieldFormat());
        this.generateFieldDisplayHandler(Risk.Field.assignuser.name(), GenericI18Enum.FORM_ASSIGNEE, new ProjectMemberHistoryFieldFormat());
        this.generateFieldDisplayHandler(Risk.Field.consequence.name(), RiskI18nEnum.FORM_CONSEQUENCE,
                new I18nHistoryFieldFormat(RiskConsequence.class));
        this.generateFieldDisplayHandler(Risk.Field.duedate.name(), GenericI18Enum.FORM_DUE_DATE, DATE_FIELD);
        this.generateFieldDisplayHandler(Risk.Field.startdate.name(), GenericI18Enum.FORM_START_DATE, DATE_FIELD);
        this.generateFieldDisplayHandler(Risk.Field.enddate.name(), GenericI18Enum.FORM_END_DATE, DATE_FIELD);
        this.generateFieldDisplayHandler(Risk.Field.milestoneid.name(), MilestoneI18nEnum.SINGLE, new MilestoneHistoryFieldFormat());
        this.generateFieldDisplayHandler(Risk.Field.probalitity.name(), RiskI18nEnum.FORM_PROBABILITY,
                new I18nHistoryFieldFormat(RiskProbability.class));
        this.generateFieldDisplayHandler(Risk.Field.status.name(), GenericI18Enum.FORM_STATUS,
                new I18nHistoryFieldFormat(StatusI18nEnum.class));
        this.generateFieldDisplayHandler(Risk.Field.priority.name(), GenericI18Enum.FORM_PRIORITY);
        this.generateFieldDisplayHandler(Risk.Field.response.name(), RiskI18nEnum.FORM_RESPONSE, TRIM_HTMLS);
    }

    public static RiskFieldFormatter instance() {
        return _instance;
    }
}

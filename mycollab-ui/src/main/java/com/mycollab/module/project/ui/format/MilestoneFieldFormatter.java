package com.mycollab.module.project.ui.format;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.module.project.domain.Milestone;
import com.mycollab.module.project.i18n.OptionI18nEnum;
import com.mycollab.vaadin.ui.formatter.FieldGroupFormatter;
import com.mycollab.vaadin.ui.formatter.I18nHistoryFieldFormat;

/**
 * @author MyCollab Ltd
 * @since 5.1.4
 */
public final class MilestoneFieldFormatter extends FieldGroupFormatter {
    private static MilestoneFieldFormatter _instance = new MilestoneFieldFormatter();

    private MilestoneFieldFormatter() {
        generateFieldDisplayHandler("name", GenericI18Enum.FORM_NAME);
        generateFieldDisplayHandler("status", GenericI18Enum.FORM_STATUS,
                new I18nHistoryFieldFormat(OptionI18nEnum.MilestoneStatus.class));
        generateFieldDisplayHandler(Milestone.Field.assignuser.name(), GenericI18Enum.FORM_ASSIGNEE,
                new ProjectMemberHistoryFieldFormat());
        generateFieldDisplayHandler("startdate", GenericI18Enum.FORM_START_DATE, DATE_FIELD);
        generateFieldDisplayHandler("enddate", GenericI18Enum.FORM_END_DATE, DATE_FIELD);
        generateFieldDisplayHandler(Milestone.Field.description.name(), GenericI18Enum.FORM_DESCRIPTION, TRIM_HTMLS);
    }

    public static MilestoneFieldFormatter instance() {
        return _instance;
    }
}

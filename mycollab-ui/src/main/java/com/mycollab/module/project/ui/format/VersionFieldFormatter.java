package com.mycollab.module.project.ui.format;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.common.i18n.OptionI18nEnum.StatusI18nEnum;
import com.mycollab.vaadin.ui.formatter.FieldGroupFormatter;
import com.mycollab.vaadin.ui.formatter.I18nHistoryFieldFormat;

/**
 * @author MyCollab Ltd
 * @since 5.1.4
 */
public final class VersionFieldFormatter extends FieldGroupFormatter {
    private static VersionFieldFormatter _instance = new VersionFieldFormatter();

    private VersionFieldFormatter() {
        generateFieldDisplayHandler("name", GenericI18Enum.FORM_NAME);
        generateFieldDisplayHandler("status", GenericI18Enum.FORM_STATUS, new I18nHistoryFieldFormat(StatusI18nEnum.class));
        generateFieldDisplayHandler("description", GenericI18Enum.FORM_DESCRIPTION, TRIM_HTMLS);
        generateFieldDisplayHandler("duedate", GenericI18Enum.FORM_DUE_DATE, DATE_FIELD);
    }

    public static VersionFieldFormatter instance() {
        return _instance;
    }
}

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
import com.mycollab.module.project.i18n.BugI18nEnum;
import com.mycollab.module.project.i18n.MilestoneI18nEnum;
import com.mycollab.module.project.i18n.OptionI18nEnum.BugResolution;
import com.mycollab.module.project.i18n.OptionI18nEnum.BugSeverity;
import com.mycollab.module.project.i18n.OptionI18nEnum.BugStatus;
import com.mycollab.module.project.i18n.OptionI18nEnum.Priority;
import com.mycollab.module.tracker.domain.BugWithBLOBs;
import com.mycollab.vaadin.ui.formatter.FieldGroupFormatter;
import com.mycollab.vaadin.ui.formatter.I18nHistoryFieldFormat;

/**
 * @author MyCollab Ltd
 * @since 5.1.4
 */
public final class BugFieldFormatter extends FieldGroupFormatter {
    private static BugFieldFormatter _instance = new BugFieldFormatter();

    private BugFieldFormatter() {
        generateFieldDisplayHandler("description", GenericI18Enum.FORM_DESCRIPTION, TRIM_HTMLS);
        generateFieldDisplayHandler("environment", BugI18nEnum.FORM_ENVIRONMENT, TRIM_HTMLS);
        generateFieldDisplayHandler("name", BugI18nEnum.FORM_SUMMARY);
        generateFieldDisplayHandler("status", GenericI18Enum.FORM_STATUS, new I18nHistoryFieldFormat(BugStatus.class));
        generateFieldDisplayHandler("priority", GenericI18Enum.FORM_PRIORITY, new I18nHistoryFieldFormat(Priority.class));
        generateFieldDisplayHandler("severity", BugI18nEnum.FORM_SEVERITY, new I18nHistoryFieldFormat(BugSeverity.class));
        generateFieldDisplayHandler("resolution", BugI18nEnum.FORM_RESOLUTION, new I18nHistoryFieldFormat(BugResolution.class));
        generateFieldDisplayHandler(BugWithBLOBs.Field.remainestimate.name(), BugI18nEnum.FORM_REMAIN_ESTIMATE);
        generateFieldDisplayHandler(BugWithBLOBs.Field.originalestimate.name(), BugI18nEnum.FORM_ORIGINAL_ESTIMATE);
        generateFieldDisplayHandler("startdate", GenericI18Enum.FORM_START_DATE, DATETIME_FIELD);
        generateFieldDisplayHandler("enddate", GenericI18Enum.FORM_END_DATE, DATETIME_FIELD);
        generateFieldDisplayHandler("duedate", GenericI18Enum.FORM_DUE_DATE, DATETIME_FIELD);
        generateFieldDisplayHandler("createdTime", GenericI18Enum.FORM_CREATED_TIME, PRETTY_DATE_TIME_FIELD);
        generateFieldDisplayHandler("loguserFullName", BugI18nEnum.FORM_LOG_BY, new ProjectMemberHistoryFieldFormat());
        generateFieldDisplayHandler("assignuser", GenericI18Enum.FORM_ASSIGNEE, new ProjectMemberHistoryFieldFormat());
        generateFieldDisplayHandler("milestoneid", MilestoneI18nEnum.SINGLE, new MilestoneHistoryFieldFormat());
    }

    public static BugFieldFormatter instance() {
        return _instance;
    }
}

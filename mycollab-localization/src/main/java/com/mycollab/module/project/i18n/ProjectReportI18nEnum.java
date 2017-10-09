package com.mycollab.module.project.i18n;

import ch.qos.cal10n.BaseName;
import ch.qos.cal10n.Locale;
import ch.qos.cal10n.LocaleData;

@BaseName("project-reporting")
@LocaleData(value = {@Locale("en-US")}, defaultCharset = "UTF-8")
public enum ProjectReportI18nEnum {
    REPORT_STANDUP,
    REPORT_STANDUP_HELP,
    REPORT_HOURS_WEEKLY,
    REPORT_HOURS_WEEKLY_HELP,
    REPORT_USERS_WORKLOAD,
    REPORT_USERS_WORKLOAD_HELP,
    REPORT_TIMESHEET,
    REPORT_TIMESHEET_HELP
}

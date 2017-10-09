package com.mycollab.common.i18n;

import ch.qos.cal10n.BaseName;
import ch.qos.cal10n.Locale;
import ch.qos.cal10n.LocaleData;

/**
 * @author MyCollab Ltd.
 * @since 4.1.1
 */
@BaseName("common-day")
@LocaleData(value = {@Locale("en-US")}, defaultCharset = "UTF-8")
public enum DayI18nEnum {
    LAST_UPDATED_ON,
    TIME_FORMAT,
    HOUR_SUFFIX,
    HOUR_PLURAL_SUFFIX,
    MINUTE_SUFFIX,
    MINUTE_PLURAL_SUFFIX,
    SECOND_SUFFIX,
    SECOND_PLURAL_SUFFIX,

    OPT_MONDAY,
    OPT_TUESDAY,
    OPT_WEDNESDAY,
    OPT_THURSDAY,
    OPT_FRIDAY,
    OPT_SATURDAY,
    OPT_SUNDAY,
    OPT_TODAY,
    OPT_WEEK,
    OPT_MONTH,
    OPT_YEAR,
    OPT_DATE,
    OPT_HOURS,
    OPT_FROM,
    OPT_TO,
    OPT_DAILY,
    OPT_WEEKLY,
    OPT_MONTHLY,

    OPT_NO_DATE_SET
}

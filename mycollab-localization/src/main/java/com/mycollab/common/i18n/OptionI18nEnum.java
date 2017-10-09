package com.mycollab.common.i18n;

import ch.qos.cal10n.BaseName;
import ch.qos.cal10n.Locale;
import ch.qos.cal10n.LocaleData;

/**
 * @author MyCollab Ltd.
 * @since 4.3.3
 */
public class OptionI18nEnum {

    @BaseName("common-generic-status")
    @LocaleData(value = {@Locale("en-US")}, defaultCharset = "UTF-8")
    public enum StatusI18nEnum {
        Open, Overdue, Closed, Archived, Pending, InProgress, Unresolved
    }
}

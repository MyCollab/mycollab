package com.mycollab.common.i18n;

import ch.qos.cal10n.BaseName;
import ch.qos.cal10n.Locale;
import ch.qos.cal10n.LocaleData;

@BaseName("common-mail")
@LocaleData(value = {@Locale("en-US")}, defaultCharset = "UTF-8")
public enum MailI18nEnum {
    Changes,
    Last_Comments_Value,
    Field,
    Old_Value,
    New_Value,
    Copyright,
    Project_Notification_Setting,
    Project_Footer
}

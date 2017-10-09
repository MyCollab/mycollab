package com.mycollab.module.crm.i18n;

import ch.qos.cal10n.BaseName;
import ch.qos.cal10n.Locale;
import ch.qos.cal10n.LocaleData;

@BaseName("crm-meeting")
@LocaleData(value = {@Locale("en-US")}, defaultCharset = "UTF-8")
public enum MeetingI18nEnum {
    NEW,
    SINGLE,

    MY_ITEMS,
    SECTION_INFORMATION,

    FORM_SUBJECT,
    FORM_START_DATE_TIME,
    FORM_END_DATE_TIME,
    FORM_LOCATION,

    MAIL_CREATE_ITEM_SUBJECT,
    MAIL_UPDATE_ITEM_SUBJECT,
    MAIL_COMMENT_ITEM_SUBJECT,
    MAIL_CREATE_ITEM_HEADING,
    MAIL_UPDATE_ITEM_HEADING,
    MAIL_COMMENT_ITEM_HEADING
}

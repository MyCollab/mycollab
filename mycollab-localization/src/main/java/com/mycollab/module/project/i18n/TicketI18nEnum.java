package com.mycollab.module.project.i18n;

import ch.qos.cal10n.BaseName;
import ch.qos.cal10n.Locale;
import ch.qos.cal10n.LocaleData;

/**
 * @author MyCollab Ltd
 * @since 5.2.5
 */
@BaseName("project-ticket")
@LocaleData(value = {@Locale("en-US")}, defaultCharset = "UTF-8")
public enum TicketI18nEnum {
    LIST,
    NEW,
    SINGLE,
    EDIT,

    OPT_TICKETS_VALUE,
    OPT_SELECT_TICKET,

    VAL_ALL_TICKETS,
    VAL_ALL_OPEN_TICKETS,
    VAL_ALL_CLOSED_TICKETS,
    VAL_OVERDUE_TICKETS,
    VAL_MY_TICKETS,
    VAL_TICKETS_CREATED_BY_ME,
    VAL_NEW_THIS_WEEK,
    VAL_UPDATE_THIS_WEEK,
    VAL_NEW_LAST_WEEK,
    VAL_UPDATE_LAST_WEEK,
}

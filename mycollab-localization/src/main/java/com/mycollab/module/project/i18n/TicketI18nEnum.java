/**
 * This file is part of mycollab-localization.
 *
 * mycollab-localization is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-localization is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-localization.  If not, see <http://www.gnu.org/licenses/>.
 */
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

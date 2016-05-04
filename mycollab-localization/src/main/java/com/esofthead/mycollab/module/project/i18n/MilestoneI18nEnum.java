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
package com.esofthead.mycollab.module.project.i18n;

import ch.qos.cal10n.BaseName;
import ch.qos.cal10n.Locale;
import ch.qos.cal10n.LocaleData;

@BaseName("localization/project-milestone")
@LocaleData(value = {@Locale("en-US"), @Locale("ja-JP")}, defaultCharset = "UTF-8")
public enum MilestoneI18nEnum {
    VIEW_LIST_TITLE,
    VIEW_NEW_TITLE,
    VIEW_DETAIL_TITLE,
    VIEW_NO_ITEM_TITLE,
    VIEW_NO_ITEM_HINT,

    BUTTON_NEW_PHASE,

    WIDGET_CLOSED_PHASE_TITLE,
    WIDGET_INPROGRESS_PHASE_TITLE,
    WIDGET_FUTURE_PHASE_TITLE,

    FORM_NEW_TITLE,
    FORM_EDIT_TITLE,
    FORM_NAME_FIELD,
    FORM_START_DATE_FIELD,
    FORM_END_DATE_FIELD,
    FORM_STATUS_FIELD,
    FORM_STATUS_FIELD_HELP,
    FORM_TASK_FIELD,
    FORM_BUG_FIELD,
    FORM_ASSIGNMENTS,

    MAIL_CREATE_ITEM_SUBJECT,
    MAIL_UPDATE_ITEM_SUBJECT,
    MAIL_COMMENT_ITEM_SUBJECT,
    MAIL_CREATE_ITEM_HEADING,
    MAIL_UPDATE_ITEM_HEADING,
    MAIL_COMMENT_ITEM_HEADING,

    M_LIST_DATE_INFO,
    M_LIST_TASK_BUG_INFO,
    M_FORM_READ_TITLE
}

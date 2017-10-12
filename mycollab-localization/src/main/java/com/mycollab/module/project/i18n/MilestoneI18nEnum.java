/**
 * Copyright © MyCollab
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.project.i18n;

import ch.qos.cal10n.BaseName;
import ch.qos.cal10n.Locale;
import ch.qos.cal10n.LocaleData;

@BaseName("project-milestone")
@LocaleData(value = {@Locale("en-US")}, defaultCharset = "UTF-8")
public enum MilestoneI18nEnum {
    LIST,
    NEW,
    DETAIL,
    SINGLE,

    WIDGET_CLOSED_PHASE_TITLE,
    WIDGET_INPROGRESS_PHASE_TITLE,
    WIDGET_FUTURE_PHASE_TITLE,

    OPT_ROADMAP_VALUE,
    OPT_TIMELINE,
    OPT_EDIT_PHASE_NAME,
    OPT_HIDE_CLOSED_MILESTONES,
    OPT_SHOW_CLOSED_MILESTONES,

    FORM_STATUS_FIELD_HELP,

    MAIL_CREATE_ITEM_SUBJECT,
    MAIL_UPDATE_ITEM_SUBJECT,
    MAIL_COMMENT_ITEM_SUBJECT,
    MAIL_CREATE_ITEM_HEADING,
    MAIL_UPDATE_ITEM_HEADING,
    MAIL_COMMENT_ITEM_HEADING
}

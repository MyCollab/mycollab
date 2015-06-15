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

@BaseName("localization/project-page")
@LocaleData(value = {@Locale("en-US"), @Locale("ja-JP")}, defaultCharset = "UTF-8")
public enum Page18InEnum {
    VIEW_NO_ITEM_TITLE,
    VIEW_NO_ITEM_HINT,
    VIEW_LIST_TITLE,
    VIEW_NEW_TITLE,
    VIEW_READ_TITLE,
    VIEW_EDIT_TITLE,

    DIALOG_NEW_GROUP_TITLE,
    DIALOG_EDIT_GROUP_TITLE,

    BUTTON_NEW_GROUP,
    BUTTON_NEW_PAGE,

    FORM_GROUP,

    FORM_SUBJECT,
    FORM_VISIBILITY,
    FORM_CATEGORY,

    OPT_CREATED_USER,
    OPT_SORT_LABEL,
    OPT_SORT_BY_DATE,
    OPT_SORT_BY_NAME,
    OPT_SORT_BY_KIND,

    LABEL_LAST_UPDATE,

    MAIL_CREATE_ITEM_SUBJECT,
    MAIL_UPDATE_ITEM_SUBJECT,
    MAIL_COMMENT_ITEM_SUBJECT,
    MAIL_CREATE_ITEM_HEADING,
    MAIL_UPDATE_ITEM_HEADING,
    MAIL_COMMENT_ITEM_HEADING
}
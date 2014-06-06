/**
 * This file is part of mycollab-web.
 *
 * mycollab-web is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-web is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-web.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.module.project.localization;

import ch.qos.cal10n.BaseName;
import ch.qos.cal10n.Locale;
import ch.qos.cal10n.LocaleData;

@BaseName("localization/project/milestone")
@LocaleData({ @Locale("en_US"), @Locale("ja_JP") })
public enum MilestoneI18nEnum {
	LIST_VIEW_TITLE,
	NEW_PHASE_ACTION,
	
	CLOSED_PHASE_TITLE,
	INPROGRESS_PHASE_TITLE,
	FUTURE_PHASE_TITLE,
	
	FORM_NEW_TITLE,
	FORM_EDIT_TITLE,
	FORM_NAME_FIELD,
	FORM_START_DATE_FIELD,
	FORM_END_DATE_FIELD,
	FORM_STATUS_FIELD, 
	FORM_TASK_FIELD,
	FORM_BUG_FIELD,
	FORM_DESCRIPTION_FIELD,
	
	RELATED_TASKS_TAB,
	RELATED_BUGS_TAB,
	
	NO_ITEM_VIEW_TITLE,
	NO_ITEM_VIEW_HINT
}

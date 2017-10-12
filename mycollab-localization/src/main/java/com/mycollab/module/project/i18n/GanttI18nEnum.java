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

@BaseName("project-gantt")
@LocaleData(value = {@Locale("en-US")}, defaultCharset = "UTF-8")
public enum GanttI18nEnum {
    ACTION_DETAIL,
    ACTION_INDENT,
    ACTION_OUTDENT,
    ACTION_INSERT_ROW_BEFORE,
    ACTION_INSERT_ROW_AFTER,
    ACTION_DELETE_ROW,

    OPT_PREDECESSORS,
    OPT_EDIT_PREDECESSORS,
    OPT_PERCENTAGE_COMPLETE,
    OPT_START,
    OPT_END,
    OPT_FS,
    OPT_SF,
    OPT_SS,
    OPT_FF,
    OPT_LAG,
    OPT_DEPENDENCY,
    OPT_ROW,

    ERROR_CIRCULAR_DEPENDENCY,
    ERROR_INVALID_CONSTRAINT,
    ERROR_CAN_NOT_CHANGE_PARENT_TASK_DATES
}

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

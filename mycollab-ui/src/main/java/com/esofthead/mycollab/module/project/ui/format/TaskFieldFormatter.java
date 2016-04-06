/**
 * This file is part of mycollab-ui.
 *
 * mycollab-ui is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-ui is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-ui.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.module.project.ui.format;

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.core.SimpleLogging;
import com.esofthead.mycollab.core.utils.HumanTime;
import com.esofthead.mycollab.core.utils.StringUtils;
import com.esofthead.mycollab.module.project.domain.Task;
import com.esofthead.mycollab.module.project.i18n.OptionI18nEnum;
import com.esofthead.mycollab.module.project.i18n.TaskI18nEnum;
import com.esofthead.mycollab.utils.FieldGroupFormatter;
import com.esofthead.mycollab.utils.HistoryFieldFormat;
import com.esofthead.mycollab.vaadin.AppContext;

/**
 * @author MyCollab Ltd
 * @since 5.1.4
 */
public final class TaskFieldFormatter extends FieldGroupFormatter {
    private static TaskFieldFormatter _instance = new TaskFieldFormatter();

    private TaskFieldFormatter() {
        generateFieldDisplayHandler("taskname", TaskI18nEnum.FORM_TASK_NAME);
        generateFieldDisplayHandler("startdate", TaskI18nEnum.FORM_START_DATE, DATE_FIELD);
        generateFieldDisplayHandler("enddate", TaskI18nEnum.FORM_END_DATE, DATE_FIELD);
        generateFieldDisplayHandler("deadline", TaskI18nEnum.FORM_DEADLINE, DATE_FIELD);
        generateFieldDisplayHandler("priority", TaskI18nEnum.FORM_PRIORITY,
                new I18nHistoryFieldFormat(OptionI18nEnum.TaskPriority.class));
        generateFieldDisplayHandler("status", TaskI18nEnum.FORM_STATUS,
                new I18nHistoryFieldFormat(com.esofthead.mycollab.common.i18n.OptionI18nEnum.StatusI18nEnum.class));
        generateFieldDisplayHandler("isestimated", TaskI18nEnum.FORM_IS_ESTIMATED);
        generateFieldDisplayHandler("remainestimate", TaskI18nEnum.FORM_REMAIN_ESTIMATE);
        generateFieldDisplayHandler("assignuser", GenericI18Enum.FORM_ASSIGNEE, new ProjectMemberHistoryFieldFormat());
        generateFieldDisplayHandler("milestoneid", TaskI18nEnum.FORM_PHASE, new MilestoneHistoryFieldFormat());
        generateFieldDisplayHandler("percentagecomplete", TaskI18nEnum.FORM_PERCENTAGE_COMPLETE);
        generateFieldDisplayHandler("parenttaskid", TaskI18nEnum.FORM_PARENT_TASK, new TaskHistoryFieldFormat());
        generateFieldDisplayHandler("notes", TaskI18nEnum.FORM_NOTES, TRIM_HTMLS);
        generateFieldDisplayHandler(Task.Field.duration.name(), TaskI18nEnum.FORM_DURATION, new DurationFieldFormat());
    }

    private static class DurationFieldFormat implements HistoryFieldFormat {

        @Override
        public String toString(String value) {
            return toString(value, true, AppContext.getMessage(GenericI18Enum.FORM_EMPTY));
        }

        @Override
        public String toString(String value, Boolean displayAsHtml, String msgIfBlank) {
            if (StringUtils.isNotBlank(value)) {
                try {
                    long duration = Long.parseLong(value);
                    HumanTime humanTime = new HumanTime(duration);
                    return humanTime.getExactly();
                } catch (Exception e) {
                    SimpleLogging.error("Parse value failed " + value, e);
                    return msgIfBlank;
                }
            }
            return msgIfBlank;
        }
    }

    public static TaskFieldFormatter instance() {
        return _instance;
    }
}

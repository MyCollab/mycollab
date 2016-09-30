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
package com.mycollab.module.project.ui.format;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.core.utils.HumanTime;
import com.mycollab.core.utils.StringUtils;
import com.mycollab.module.project.domain.Task;
import com.mycollab.module.project.i18n.MilestoneI18nEnum;
import com.mycollab.module.project.i18n.OptionI18nEnum;
import com.mycollab.module.project.i18n.TaskI18nEnum;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.formatter.FieldGroupFormatter;
import com.mycollab.vaadin.ui.formatter.HistoryFieldFormat;
import com.mycollab.vaadin.ui.formatter.I18nHistoryFieldFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author MyCollab Ltd
 * @since 5.1.4
 */
public final class TaskFieldFormatter extends FieldGroupFormatter {
    private static Logger LOG = LoggerFactory.getLogger(TaskFieldFormatter.class);
    private static TaskFieldFormatter _instance = new TaskFieldFormatter();

    private TaskFieldFormatter() {
        generateFieldDisplayHandler(Task.Field.name.name(), GenericI18Enum.FORM_NAME);
        generateFieldDisplayHandler(Task.Field.startdate.name(), GenericI18Enum.FORM_START_DATE, DATETIME_FIELD);
        generateFieldDisplayHandler(Task.Field.enddate.name(), GenericI18Enum.FORM_END_DATE, DATETIME_FIELD);
        generateFieldDisplayHandler(Task.Field.duedate.name(), GenericI18Enum.FORM_DUE_DATE, DATETIME_FIELD);
        generateFieldDisplayHandler(Task.Field.priority.name(), GenericI18Enum.FORM_PRIORITY,
                new I18nHistoryFieldFormat(OptionI18nEnum.Priority.class));
        generateFieldDisplayHandler(Task.Field.status.name(), GenericI18Enum.FORM_STATUS,
                new I18nHistoryFieldFormat(com.mycollab.common.i18n.OptionI18nEnum.StatusI18nEnum.class));
        generateFieldDisplayHandler(Task.Field.isestimated.name(), TaskI18nEnum.FORM_IS_ESTIMATED);
        generateFieldDisplayHandler(Task.Field.remainestimate.name(), TaskI18nEnum.FORM_REMAIN_ESTIMATE);
        generateFieldDisplayHandler(Task.Field.originalestimate.name(), TaskI18nEnum.FORM_ORIGINAL_ESTIMATE);
        generateFieldDisplayHandler(Task.Field.assignuser.name(), GenericI18Enum.FORM_ASSIGNEE, new ProjectMemberHistoryFieldFormat());
        generateFieldDisplayHandler(Task.Field.milestoneid.name(), MilestoneI18nEnum.SINGLE, new MilestoneHistoryFieldFormat());
        generateFieldDisplayHandler(Task.Field.percentagecomplete.name(), TaskI18nEnum.FORM_PERCENTAGE_COMPLETE);
        generateFieldDisplayHandler(Task.Field.parenttaskid.name(), TaskI18nEnum.FORM_PARENT_TASK, new TaskHistoryFieldFormat());
        generateFieldDisplayHandler(Task.Field.description.name(), GenericI18Enum.FORM_DESCRIPTION, TRIM_HTMLS);
        generateFieldDisplayHandler(Task.Field.duration.name(), GenericI18Enum.FORM_DURATION, new DurationFieldFormat());
    }

    private static class DurationFieldFormat implements HistoryFieldFormat {

        @Override
        public String toString(String value) {
            return toString(value, true, UserUIContext.getMessage(GenericI18Enum.FORM_EMPTY));
        }

        @Override
        public String toString(String value, Boolean displayAsHtml, String msgIfBlank) {
            if (StringUtils.isNotBlank(value)) {
                try {
                    long duration = Long.parseLong(value);
                    HumanTime humanTime = new HumanTime(duration);
                    return humanTime.getExactly();
                } catch (Exception e) {
                    LOG.error("Parse value failed " + value, e);
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

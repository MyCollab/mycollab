/**
 * Copyright © MyCollab
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.project.view.task;

import com.mycollab.core.utils.BusinessDayTimeUtils;
import com.mycollab.core.utils.DateTimeUtils;
import com.mycollab.module.file.AttachmentUtils;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.domain.SimpleTask;
import com.mycollab.module.project.domain.Task;
import com.mycollab.module.project.i18n.TaskI18nEnum;
import com.mycollab.module.project.ui.components.DurationEditField;
import com.mycollab.module.project.ui.components.PriorityComboBox;
import com.mycollab.module.project.ui.components.ProjectSubscribersComp;
import com.mycollab.module.project.ui.components.TaskSliderField;
import com.mycollab.module.project.view.milestone.MilestoneComboBox;
import com.mycollab.module.project.view.settings.component.ProjectMemberSelectionField;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.AbstractBeanFieldGroupEditFieldFactory;
import com.mycollab.vaadin.ui.GenericBeanForm;
import com.mycollab.vaadin.web.ui.WebThemes;
import com.mycollab.vaadin.web.ui.field.AttachmentUploadField;
import com.vaadin.data.HasValue;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.RichTextArea;
import org.vaadin.viritin.fields.DoubleField;
import org.vaadin.viritin.fields.MTextField;

import java.time.LocalDate;

/**
 * @author MyCollab Ltd
 * @since 5.1.1
 */
class TaskEditFormFieldFactory extends AbstractBeanFieldGroupEditFieldFactory<SimpleTask> {
    private static final long serialVersionUID = 1L;

    private ProjectSubscribersComp subscribersComp;
    private AttachmentUploadField attachmentUploadField;

    private DateField startDateField, endDateField;

    TaskEditFormFieldFactory(GenericBeanForm<SimpleTask> form, Integer projectId) {
        super(form);
        subscribersComp = new ProjectSubscribersComp(false, projectId, UserUIContext.getUsername());
    }

    @Override
    protected HasValue<?> onCreateField(final Object propertyId) {
        if (Task.Field.assignuser.equalTo(propertyId)) {
            ProjectMemberSelectionField field = new ProjectMemberSelectionField();
            field.addValueChangeListener(valueChangeEvent -> {
                String username = valueChangeEvent.getValue();
                if (username != null) {
                    subscribersComp.addFollower(username);
                }
            });
            return field;
        } else if (Task.Field.milestoneid.equalTo(propertyId)) {
            return new MilestoneComboBox();
        } else if (Task.Field.description.equalTo(propertyId)) {
            return new RichTextArea();
        } else if (Task.Field.name.equalTo(propertyId)) {
            return new MTextField().withRequiredIndicatorVisible(true);
        } else if (Task.Field.status.equalTo(propertyId)) {
            return new TaskStatusComboBox();
        } else if (Task.Field.percentagecomplete.equalTo(propertyId)) {
            return new TaskSliderField();
        } else if (Task.Field.priority.equalTo(propertyId)) {
            return new PriorityComboBox();
        } else if (Task.Field.duration.equalTo(propertyId)) {
            DurationEditField field = new DurationEditField();
            field.setWidth(WebThemes.FORM_CONTROL_WIDTH);
            final SimpleTask beanItem = attachForm.getBean();
            if (beanItem.getNumSubTasks() != null && beanItem.getNumSubTasks() > 0) {
                field.setEnabled(false);
                field.setDescription(UserUIContext.getMessage(TaskI18nEnum.ERROR_CAN_NOT_EDIT_PARENT_TASK_FIELD));
            }

            field.addValueChangeListener((HasValue.ValueChangeListener<Long>) event -> {
                long duration = event.getValue();
                LocalDate startDateVal = startDateField.getValue();
                if (duration > 0 && startDateVal != null) {
                    int daysDuration = (int) (duration / DateTimeUtils.MILLISECONDS_IN_A_DAY);
                    if (daysDuration > 0) {
                        LocalDate endDateVal = BusinessDayTimeUtils.plusDays(startDateVal, daysDuration);
                        endDateField.setValue(endDateVal);
                    }
                }
            });
            return field;
        } else if (Task.Field.originalestimate.equalTo(propertyId) || Task.Field.remainestimate.equalTo(propertyId)) {
            return new DoubleField().withWidth(WebThemes.FORM_CONTROL_WIDTH);
        } else if (Task.Field.duedate.equalTo(propertyId)) {
            return new DateField();
        } else if (Task.Field.startdate.equalTo(propertyId)) {
            startDateField = new DateField();
            return startDateField;
        } else if (Task.Field.enddate.equalTo(propertyId)) {
            endDateField = new DateField();
            return endDateField;
        } else if ("section-attachments".equals(propertyId)) {
            Task beanItem = attachForm.getBean();
            if (beanItem.getId() != null) {
                String attachmentPath = AttachmentUtils.getProjectEntityAttachmentPath(AppUI.getAccountId(),
                        beanItem.getProjectid(), ProjectTypeConstants.TASK, "" + beanItem.getId());
                attachmentUploadField = new AttachmentUploadField(attachmentPath);
            } else {
                attachmentUploadField = new AttachmentUploadField();
            }
            return attachmentUploadField;
        } else if (Task.Field.isestimated.equalTo(propertyId)) {
            return new CheckBox();
        } else if ("section-followers".equals(propertyId)) {
            return subscribersComp;
        }
        return null;
    }

    private void calculateDurationBaseOnStartAndEndDates() {
//        DateTimeOptionField startDateField = (DateTimeOptionField) fieldGroup.getField(Task.Field.startdate.name());
//        DateTimeOptionField endDateField = (DateTimeOptionField) fieldGroup.getField(Task.Field.enddate.name());
//        TextField durationField = (TextField) fieldGroup.getField(Task.Field.duration.name());
//        Date startDate = null, endDate = null;
//        if (startDateField != null) {
//            startDate = startDateField.getValue();
//        }
//        if (endDateField != null) {
//            endDate = endDateField.getValue();
//        }
//        if (startDate != null && endDate != null && startDate.before(endDate) && durationField != null) {
//            LocalDate jodaStartDate = new LocalDate(startDate);
//            LocalDate jodaEndDate = new LocalDate(endDate);
//            int durationInDays = BusinessDayTimeUtils.duration(jodaStartDate, jodaEndDate);
//            durationField.setValue(durationInDays + " d");
//        }
    }

    public AttachmentUploadField getAttachmentUploadField() {
        return attachmentUploadField;
    }

    public ProjectSubscribersComp getSubscribersComp() {
        return subscribersComp;
    }
}

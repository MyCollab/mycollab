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
package com.esofthead.mycollab.module.project.view.task;

import com.esofthead.mycollab.common.i18n.ErrorI18nEnum;
import com.esofthead.mycollab.core.utils.BusinessDayTimeUtils;
import com.esofthead.mycollab.core.utils.DateTimeUtils;
import com.esofthead.mycollab.core.utils.HumanTime;
import com.esofthead.mycollab.module.file.AttachmentUtils;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.domain.SimpleTask;
import com.esofthead.mycollab.module.project.domain.Task;
import com.esofthead.mycollab.module.project.ui.components.HumanTimeConverter;
import com.esofthead.mycollab.module.project.ui.components.TaskCompleteStatusSelection;
import com.esofthead.mycollab.module.project.view.milestone.MilestoneComboBox;
import com.esofthead.mycollab.module.project.view.settings.component.ProjectMemberSelectionField;
import com.esofthead.mycollab.module.project.view.task.components.TaskPriorityComboBox;
import com.esofthead.mycollab.module.project.view.task.components.TaskStatusComboBox;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.AbstractBeanFieldGroupEditFieldFactory;
import com.esofthead.mycollab.vaadin.ui.GenericBeanForm;
import com.esofthead.mycollab.vaadin.web.ui.field.AttachmentUploadField;
import com.vaadin.data.Property;
import com.vaadin.event.FieldEvents;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Field;
import com.vaadin.ui.RichTextArea;
import com.vaadin.ui.TextField;
import org.joda.time.LocalDate;

import java.util.Date;

/**
 * @author MyCollab Ltd
 * @since 5.1.1
 */
class TaskEditFormFieldFactory extends AbstractBeanFieldGroupEditFieldFactory<SimpleTask> {
    private static final long serialVersionUID = 1L;

    private AttachmentUploadField attachmentUploadField;

    TaskEditFormFieldFactory(GenericBeanForm<SimpleTask> form) {
        super(form);
    }

    @Override
    protected Field<?> onCreateField(final Object propertyId) {
        if (Task.Field.assignuser.equalTo(propertyId)) {
            return new ProjectMemberSelectionField();
        } else if (Task.Field.milestoneid.equalTo(propertyId)) {
            return new MilestoneComboBox();
        } else if (Task.Field.notes.equalTo(propertyId)) {
            final RichTextArea richTextArea = new RichTextArea();
            richTextArea.setNullRepresentation("");
            return richTextArea;
        } else if (Task.Field.taskname.equalTo(propertyId)) {
            final TextField tf = new TextField();
            tf.setNullRepresentation("");
            tf.setRequired(true);
            tf.setRequiredError(AppContext.getMessage(ErrorI18nEnum.FIELD_MUST_NOT_NULL, "Name"));
            return tf;
        } else if (Task.Field.status.equalTo(propertyId)) {
            return new TaskStatusComboBox();
        } else if (Task.Field.percentagecomplete.equalTo(propertyId)) {
            return new TaskCompleteStatusSelection();
        } else if (Task.Field.priority.equalTo(propertyId)) {
            return new TaskPriorityComboBox();
        } else if (Task.Field.duration.equalTo(propertyId)) {
            final TextField field = new TextField();
            field.setConverter(new HumanTimeConverter());
            final SimpleTask beanItem = attachForm.getBean();
            if (beanItem.getNumSubTasks() != null && beanItem.getNumSubTasks() > 0) {
                field.setEnabled(false);
                field.setDescription("Because this row has sub-tasks, this cell " +
                        "is a summary value and can not be edited directly. You can edit cells " +
                        "beneath this row to change its value");
            }

            //calculate the end date if the start date is set
            field.addBlurListener(new FieldEvents.BlurListener() {
                @Override
                public void blur(FieldEvents.BlurEvent event) {
                    HumanTime humanTime = HumanTime.eval(field.getValue());
                    Integer duration = new Integer(humanTime.getDelta() + "");
                    DateField startDateField = (DateField) fieldGroup.getField(Task.Field.startdate.name());
                    Date startDateVal = startDateField.getValue();
                    if (duration > 0 && startDateVal != null) {
                        int durationIndays = duration/ (int) DateTimeUtils.MILLISECONDS_IN_A_DAY;
                        if (durationIndays > 0) {
                            LocalDate startDateJoda = new LocalDate(startDateVal);
                            LocalDate endDateJoda = BusinessDayTimeUtils.plusDays(startDateJoda, durationIndays);
                            DateField endDateField = (DateField) fieldGroup.getField(Task.Field.enddate.name());
                            endDateField.setValue(endDateJoda.toDate());
                        }
                    }
                }
            });
            return field;
        } else if (Task.Field.startdate.equalTo(propertyId)) {
            final DateField startDateField = new DateField();
            startDateField.addValueChangeListener(new Property.ValueChangeListener() {
                @Override
                public void valueChange(Property.ValueChangeEvent event) {
                    calculateDurationBaseOnStartAndEndDates();
                }
            });
            return startDateField;
        } else if (Task.Field.enddate.equalTo(propertyId)) {
            DateField endDateField = new DateField();
            endDateField.addValueChangeListener(new Property.ValueChangeListener() {
                @Override
                public void valueChange(Property.ValueChangeEvent event) {
                    calculateDurationBaseOnStartAndEndDates();
                }
            });
            return endDateField;
        } else if (Task.Field.id.equalTo(propertyId)) {
            attachmentUploadField = new AttachmentUploadField();
            Task beanItem = attachForm.getBean();
            if (beanItem.getId() != null) {
                String attachmentPath = AttachmentUtils.getProjectEntityAttachmentPath(AppContext.getAccountId(),
                        beanItem.getProjectid(), ProjectTypeConstants.TASK, "" + beanItem.getId());
                attachmentUploadField.getAttachments(attachmentPath);
            }
            return attachmentUploadField;
        }
        return null;
    }

    private void calculateDurationBaseOnStartAndEndDates() {
        DateField startDateField = (DateField) fieldGroup.getField(Task.Field.startdate.name());
        DateField endDateField = (DateField) fieldGroup.getField(Task.Field.enddate.name());
        TextField durationField = (TextField) fieldGroup.getField(Task.Field.duration.name());
        Date startDate = null;
        Date endDate = null;
        if (startDateField != null) {
            startDate = startDateField.getValue();
        }
        if (endDateField != null) {
            endDate = endDateField.getValue();
        }
        if (startDate != null && endDate != null && startDate.before(endDate) && durationField != null) {
            LocalDate startDateJoda = new LocalDate(startDate);
            LocalDate endDateJoda = new LocalDate(endDate);
            int durationInDays = BusinessDayTimeUtils.duration(startDateJoda, endDateJoda);
            durationField.setValue(durationInDays + " d");
        }
    }

    public AttachmentUploadField getAttachmentUploadField() {
        return attachmentUploadField;
    }
}

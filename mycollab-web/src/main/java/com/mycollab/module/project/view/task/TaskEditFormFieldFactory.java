/**
 * mycollab-web - Parent pom providing dependency and plugin management for applications
		built with Maven
 * Copyright © ${project.inceptionYear} MyCollab (support@mycollab.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.project.view.task;

import com.mycollab.common.i18n.ErrorI18nEnum;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.core.utils.BusinessDayTimeUtils;
import com.mycollab.core.utils.DateTimeUtils;
import com.mycollab.core.utils.HumanTime;
import com.mycollab.module.file.AttachmentUtils;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.domain.SimpleProjectMember;
import com.mycollab.module.project.domain.SimpleTask;
import com.mycollab.module.project.domain.Task;
import com.mycollab.module.project.i18n.TaskI18nEnum;
import com.mycollab.module.project.ui.components.HumanTimeConverter;
import com.mycollab.module.project.ui.components.ProjectSubscribersComp;
import com.mycollab.module.project.ui.components.TaskSliderField;
import com.mycollab.module.project.view.milestone.MilestoneComboBox;
import com.mycollab.module.project.view.settings.component.ProjectMemberSelectionField;
import com.mycollab.module.project.ui.components.PriorityComboBox;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.AbstractBeanFieldGroupEditFieldFactory;
import com.mycollab.vaadin.ui.GenericBeanForm;
import com.mycollab.vaadin.web.ui.DoubleField;
import com.mycollab.vaadin.web.ui.field.AttachmentUploadField;
import com.mycollab.vaadin.web.ui.field.DateTimeOptionField;
import com.vaadin.data.Property;
import com.vaadin.data.util.MethodProperty;
import com.vaadin.data.util.TransactionalPropertyWrapper;
import com.vaadin.ui.Field;
import com.vaadin.ui.RichTextArea;
import com.vaadin.ui.TextField;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.vaadin.viritin.fields.MTextField;

import java.util.Date;

/**
 * @author MyCollab Ltd
 * @since 5.1.1
 */
class TaskEditFormFieldFactory extends AbstractBeanFieldGroupEditFieldFactory<SimpleTask> {
    private static final long serialVersionUID = 1L;

    private ProjectSubscribersComp subscribersComp;
    private AttachmentUploadField attachmentUploadField;

    TaskEditFormFieldFactory(GenericBeanForm<SimpleTask> form, Integer projectId) {
        super(form);
        subscribersComp = new ProjectSubscribersComp(false, projectId, UserUIContext.getUsername());
    }

    @Override
    protected Field<?> onCreateField(final Object propertyId) {
        if (Task.Field.assignuser.equalTo(propertyId)) {
            ProjectMemberSelectionField field = new ProjectMemberSelectionField();
            field.addValueChangeListener(valueChangeEvent -> {
                Property property = valueChangeEvent.getProperty();
                SimpleProjectMember member = (SimpleProjectMember) property.getValue();
                if (member != null) {
                    subscribersComp.addFollower(member.getUsername());
                }
            });
            return field;
        } else if (Task.Field.milestoneid.equalTo(propertyId)) {
            return new MilestoneComboBox();
        } else if (Task.Field.description.equalTo(propertyId)) {
            final RichTextArea richTextArea = new RichTextArea();
            richTextArea.setNullRepresentation("");
            return richTextArea;
        } else if (Task.Field.name.equalTo(propertyId)) {
            return new MTextField().withNullRepresentation("").withRequired(true).withRequiredError(UserUIContext
                    .getMessage(ErrorI18nEnum.FIELD_MUST_NOT_NULL, UserUIContext.getMessage(GenericI18Enum.FORM_NAME)));
        } else if (Task.Field.status.equalTo(propertyId)) {
            return new TaskStatusComboBox();
        } else if (Task.Field.percentagecomplete.equalTo(propertyId)) {
            return new TaskSliderField();
        } else if (Task.Field.priority.equalTo(propertyId)) {
            return new PriorityComboBox();
        } else if (Task.Field.duration.equalTo(propertyId)) {
            final TextField field = new TextField();
            field.setConverter(new HumanTimeConverter());
            final SimpleTask beanItem = attachForm.getBean();
            if (beanItem.getNumSubTasks() != null && beanItem.getNumSubTasks() > 0) {
                field.setEnabled(false);
                field.setDescription(UserUIContext.getMessage(TaskI18nEnum.ERROR_CAN_NOT_EDIT_PARENT_TASK_FIELD));
            }

            //calculate the end date if the start date is set
            field.addBlurListener(blurEvent -> {
                HumanTime humanTime = HumanTime.eval(field.getValue());
                long duration = Long.valueOf(humanTime.getDelta() + "");
                DateTimeOptionField startDateField = (DateTimeOptionField) fieldGroup.getField(Task.Field.startdate.name());
                Date startDateVal = startDateField.getValue();
                if (duration > 0 && startDateVal != null) {
                    int daysDuration = (int) (duration / DateTimeUtils.MILLISECONDS_IN_A_DAY);
                    if (daysDuration > 0) {
                        DateTime startDateJoda = new DateTime(startDateVal);
                        LocalDate calculatedDate = BusinessDayTimeUtils.plusDays(startDateJoda.toLocalDate(), daysDuration);
                        DateTime endDateJoda = new DateTime(calculatedDate.toDate());
                        DateTimeOptionField endDateField = (DateTimeOptionField) fieldGroup.getField(Task.Field.enddate.name());
                        beanItem.setEnddate(endDateJoda.toDate());
                        endDateField.setPropertyDataSource(new TransactionalPropertyWrapper<>(new MethodProperty(beanItem, "enddate")));
                    }
                }
            });
            return field;
        } else if (Task.Field.originalestimate.equalTo(propertyId) || Task.Field.remainestimate.equalTo(propertyId)) {
            return new DoubleField();
        } else if (Task.Field.startdate.equalTo(propertyId)) {
            final DateTimeOptionField startDateField = new DateTimeOptionField(true);
            startDateField.addValueChangeListener(valueChangeEvent -> calculateDurationBaseOnStartAndEndDates());
            return startDateField;
        } else if (Task.Field.enddate.equalTo(propertyId)) {
            DateTimeOptionField endDateField = new DateTimeOptionField(true);
            endDateField.addValueChangeListener(valueChangeEvent -> calculateDurationBaseOnStartAndEndDates());
            return endDateField;
        } else if (Task.Field.id.equalTo(propertyId)) {
            Task beanItem = attachForm.getBean();
            if (beanItem.getId() != null) {
                String attachmentPath = AttachmentUtils.getProjectEntityAttachmentPath(AppUI.getAccountId(),
                        beanItem.getProjectid(), ProjectTypeConstants.TASK, "" + beanItem.getId());
                attachmentUploadField = new AttachmentUploadField(attachmentPath);
            } else {
                attachmentUploadField = new AttachmentUploadField();
            }
            return attachmentUploadField;
        } else if (Task.Field.duedate.equalTo(propertyId)) {
            return new DateTimeOptionField(true);
        } else if (propertyId.equals("selected")) {
            return subscribersComp;
        }
        return null;
    }

    private void calculateDurationBaseOnStartAndEndDates() {
        DateTimeOptionField startDateField = (DateTimeOptionField) fieldGroup.getField(Task.Field.startdate.name());
        DateTimeOptionField endDateField = (DateTimeOptionField) fieldGroup.getField(Task.Field.enddate.name());
        TextField durationField = (TextField) fieldGroup.getField(Task.Field.duration.name());
        Date startDate = null, endDate = null;
        if (startDateField != null) {
            startDate = startDateField.getValue();
        }
        if (endDateField != null) {
            endDate = endDateField.getValue();
        }
        if (startDate != null && endDate != null && startDate.before(endDate) && durationField != null) {
            LocalDate jodaStartDate = new LocalDate(startDate);
            LocalDate jodaEndDate = new LocalDate(endDate);
            int durationInDays = BusinessDayTimeUtils.duration(jodaStartDate, jodaEndDate);
            durationField.setValue(durationInDays + " d");
        }
    }

    public AttachmentUploadField getAttachmentUploadField() {
        return attachmentUploadField;
    }

    public ProjectSubscribersComp getSubscribersComp() {
        return subscribersComp;
    }
}

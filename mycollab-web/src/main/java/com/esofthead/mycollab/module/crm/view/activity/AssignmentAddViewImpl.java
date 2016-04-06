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
package com.esofthead.mycollab.module.crm.view.activity;

import com.esofthead.mycollab.module.crm.CrmDataTypeFactory;
import com.esofthead.mycollab.module.crm.CrmTypeConstants;
import com.esofthead.mycollab.module.crm.domain.Task;
import com.esofthead.mycollab.module.crm.ui.CrmAssetsManager;
import com.esofthead.mycollab.module.crm.ui.components.AbstractEditItemComp;
import com.esofthead.mycollab.module.crm.ui.components.RelatedEditItemField;
import com.esofthead.mycollab.module.crm.view.contact.ContactSelectionField;
import com.esofthead.mycollab.module.user.ui.components.ActiveUserComboBox;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.ui.*;
import com.esofthead.mycollab.vaadin.web.ui.DynaFormLayout;
import com.esofthead.mycollab.vaadin.web.ui.EditFormControlsGenerator;
import com.esofthead.mycollab.vaadin.web.ui.ValueComboBox;
import com.vaadin.server.Resource;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Field;
import com.vaadin.ui.RichTextArea;
import com.vaadin.ui.TextField;

/**
 * @author MyCollab Ltd.
 * @since 2.0
 */
@ViewComponent
public class AssignmentAddViewImpl extends AbstractEditItemComp<Task> implements
        AssignmentAddView {
    private static final long serialVersionUID = 1L;

    @Override
    protected String initFormTitle() {
        return (beanItem.getId() == null) ? "New Task" : beanItem.getSubject();
    }

    @Override
    protected Resource initFormIconResource() {
        return CrmAssetsManager.getAsset(CrmTypeConstants.TASK);
    }

    @Override
    protected ComponentContainer createButtonControls() {
        return new EditFormControlsGenerator<>(editForm).createButtonControls();
    }

    @Override
    protected AdvancedEditBeanForm<Task> initPreviewForm() {
        return new AdvancedEditBeanForm<>();
    }

    @Override
    protected IFormLayoutFactory initFormLayoutFactory() {
        return new DynaFormLayout(CrmTypeConstants.TASK, AssignmentDefaultFormLayoutFactory.getForm());
    }

    @Override
    protected AbstractBeanFieldGroupEditFieldFactory<Task> initBeanFormFieldFactory() {
        return new AssignmentEditFormFieldFactory(editForm);
    }

    private static class AssignmentEditFormFieldFactory extends
            AbstractBeanFieldGroupEditFieldFactory<Task> {
        private static final long serialVersionUID = 1L;

        public AssignmentEditFormFieldFactory(GenericBeanForm<Task> form) {
            super(form);
        }

        @Override
        protected Field<?> onCreateField(Object propertyId) {
            if (Task.Field.startdate.equalTo(propertyId)) {
                return new DateTimePickerField();
            } else if (Task.Field.duedate.equalTo(propertyId)) {
                return new DateTimePickerField();
            } else if (Task.Field.status.equalTo(propertyId)) {
                return new TaskStatusComboBox();
            } else if (Task.Field.priority.equalTo(propertyId)) {
                return new TaskPriorityComboBox();
            } else if (Task.Field.description.equalTo(propertyId)) {
                return new RichTextArea();
            } else if (Task.Field.contactid.equalTo(propertyId)) {
                return new ContactSelectionField();
            } else if (Task.Field.subject.equalTo(propertyId)) {
                TextField tf = new TextField();
                if (isValidateForm) {
                    tf.setRequired(true);
                    tf.setRequiredError("Subject must not be null");
                    tf.setNullRepresentation("");
                }
                return tf;
            } else if (Task.Field.type.equalTo(propertyId)) {
                return new RelatedEditItemField(attachForm.getBean());
            } else if (Task.Field.typeid.equalTo(propertyId)) {
                return new DummyCustomField<Integer>();
            } else if (Task.Field.assignuser.equalTo(propertyId)) {
                ActiveUserComboBox userBox = new ActiveUserComboBox();
                userBox.select(attachForm.getBean().getAssignuser());
                return userBox;
            }
            return null;
        }
    }

    private static class TaskPriorityComboBox extends ValueComboBox {
        private static final long serialVersionUID = 1L;

        public TaskPriorityComboBox() {
            super();
            setCaption(null);
            this.loadData(CrmDataTypeFactory.getTaskPriorities());
        }
    }

    private static class TaskStatusComboBox extends ValueComboBox {
        private static final long serialVersionUID = 1L;

        TaskStatusComboBox() {
            super();
            setCaption(null);
            this.loadData(CrmDataTypeFactory.getTaskStatuses());
        }
    }
}

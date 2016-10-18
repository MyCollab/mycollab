/**
 * This file is part of mycollab-mobile.
 *
 * mycollab-mobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-mobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-mobile.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.mobile.module.crm.view.activity;

import com.mycollab.mobile.module.crm.view.contact.ContactSelectionField;
import com.mycollab.mobile.module.user.ui.components.ActiveUserComboBox;
import com.mycollab.mobile.ui.I18NValueListSelect;
import com.mycollab.module.crm.CrmDataTypeFactory;
import com.mycollab.module.crm.domain.CrmTask;
import com.mycollab.vaadin.ui.AbstractBeanFieldGroupEditFieldFactory;
import com.mycollab.vaadin.ui.DummyCustomField;
import com.mycollab.vaadin.ui.GenericBeanForm;
import com.vaadin.addon.touchkit.ui.DatePicker;
import com.vaadin.ui.Field;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;

import java.util.Arrays;

/**
 * @author MyCollab Ltd.
 * @since 4.1
 */
public class AssignmentEditFormFieldFactory extends AbstractBeanFieldGroupEditFieldFactory<CrmTask> {
    private static final long serialVersionUID = 1L;

    public AssignmentEditFormFieldFactory(GenericBeanForm<CrmTask> form) {
        super(form);
    }

    @Override
    protected Field<?> onCreateField(Object propertyId) {
        if (propertyId.equals("startdate")) {
            return new DatePicker();
        } else if (propertyId.equals("duedate")) {
            return new DatePicker();
        } else if (propertyId.equals("status")) {
            return new TaskStatusListSelect();
        } else if (propertyId.equals("priority")) {
            return new TaskPriorityListSelect();
        } else if (propertyId.equals("description")) {
            TextArea descArea = new TextArea();
            descArea.setNullRepresentation("");
            return descArea;
        } else if (propertyId.equals("contactid")) {
            return new ContactSelectionField();
        } else if (propertyId.equals("subject")) {
            TextField tf = new TextField();

            if (isValidateForm) {
                tf.setRequired(true);
                tf.setRequiredError("Subject must not be null");
                tf.setNullRepresentation("");
            }

            return tf;
        } else if (propertyId.equals("typeid")) {
            return new RelatedItemSelectionField(attachForm.getBean());
        } else if (propertyId.equals("type")) {
            return new DummyCustomField<String>();
        } else if (propertyId.equals("assignuser")) {
            ActiveUserComboBox userBox = new ActiveUserComboBox();
            userBox.select(attachForm.getBean().getAssignuser());
            return userBox;
        }
        return null;
    }

    static class TaskPriorityListSelect extends I18NValueListSelect {
        private static final long serialVersionUID = 1L;

        private TaskPriorityListSelect() {
            super();
            setCaption(null);
            this.loadData(Arrays.asList(CrmDataTypeFactory.getTaskPriorities()));
        }
    }

    static class TaskStatusListSelect extends I18NValueListSelect {
        private static final long serialVersionUID = 1L;

        private TaskStatusListSelect() {
            super();
            setCaption(null);
            this.loadData(Arrays.asList(CrmDataTypeFactory.getTaskStatuses()));
        }
    }
}
